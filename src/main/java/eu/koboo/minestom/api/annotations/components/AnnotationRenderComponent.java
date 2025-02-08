package eu.koboo.minestom.api.annotations.components;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.ViewType;
import eu.koboo.minestom.api.annotations.Slot;
import eu.koboo.minestom.api.annotations.SlotPosition;
import eu.koboo.minestom.api.annotations.Stateful;
import eu.koboo.minestom.api.component.ComponentPriority;
import eu.koboo.minestom.api.component.Priority;
import eu.koboo.minestom.api.component.ViewComponent;
import eu.koboo.minestom.api.item.PrebuiltItem;
import eu.koboo.minestom.api.item.ViewItem;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Slf4j
@ComponentPriority(Priority.HIGHEST)
public final class AnnotationRenderComponent extends ViewComponent {

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        view.getRegistry().executeComponents(
            view.getRootComponent(),
            component -> executeItemMethods(view, component, false)
        );
    }

    @Override
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        view.getRegistry().executeComponents(
            view.getRootComponent(),
            component -> executeItemMethods(view, component, true)
        );
    }

    public void executeItemMethods(PlayerView view, ViewComponent component, boolean isStateUpdate) {
        for (Method method : component.getClass().getDeclaredMethods()) {

            if (isStateUpdate && !method.isAnnotationPresent(Stateful.class)) {
                continue;
            }
            if (!isStateUpdate && method.isAnnotationPresent(Stateful.class)) {
                continue;
            }

            // The important thing here is the slot itself.
            // If we can't resolve it, we stop from here on.
            int slot = resolveSlotByAnnotation(view.getType(), method);
            if (slot == -1) {
                continue;
            }

            // Check if we are even allowed to call this method.
            int modifiers = method.getModifiers();
            String methodName = method.getName();
            if (!Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException("PrebuiltItem method " + methodName + " is not " +
                    "public!");
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalArgumentException("PrebuiltItem method " + methodName + " has method " +
                    "parameters!");
            }
            Class<?> returnType = method.getReturnType();
            if (!PrebuiltItem.class.isAssignableFrom(returnType)) {
                continue;
            }

            PrebuiltItem prebuiltItem;
            try {
                prebuiltItem = (PrebuiltItem) method.invoke(component);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("Caught exception executing item rendering: ", e);
                return;
            }
            ViewItem.bySlot(view, slot)
                .applyPrebuilt(prebuiltItem);
        }
    }

    private int resolveSlotByAnnotation(ViewType viewType, AnnotatedElement element) {
        Slot slotAnnotation = element.getAnnotation(Slot.class);
        if (slotAnnotation != null) {
            int slotValue = slotAnnotation.value();
            if (slotValue > -1) {
                return slotValue;
            }
        }
        SlotPosition slotPositionAnnotation = element.getAnnotation(SlotPosition.class);
        if (slotPositionAnnotation != null) {
            int rowIndex = slotPositionAnnotation.row();
            int columnIndex = slotPositionAnnotation.column();
            if (rowIndex > -1 && columnIndex > -1) {
                return viewType.toSlot(rowIndex, columnIndex);
            }
        }
        return -1;
    }
}
