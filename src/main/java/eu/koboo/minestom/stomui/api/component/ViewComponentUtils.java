package eu.koboo.minestom.stomui.api.component;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Some utilities to keep the class size of {@link ViewComponent} smaller and cleaner.
 */
@UtilityClass
public final class ViewComponentUtils {

    IllegalArgumentException dependencyCycleException(@NotNull ViewComponent thisComponent,
                                                      @NotNull ViewComponent thatComponent) {
        return new IllegalArgumentException(
            "Component-Cycle found: \n" +
                " - component was found: " + thisComponent + "\n" +
                " -  as child of parent: " + thatComponent
        );
    }

    void validateUniqueness(ViewComponent thisComponent) {
        ViewComponent rootAncestor = thisComponent.findRootAncestor();
        // If our current component is the root,
        // we don't need to validate anything.
        if (rootAncestor.getId().equals(thisComponent.getId())) {
            return;
        }
        validateComponentChildren(thisComponent, rootAncestor);
    }

    private void validateComponentChildren(@NotNull ViewComponent thisComponent,
                                           @NotNull ViewComponent thatComponent) {
        if (thatComponent.getChildren().isEmpty()) {
            return;
        }
        for (ViewComponent child : thatComponent.getChildren()) {
            if (child.getId().equals(thisComponent.getId())) {
                throw ViewComponentUtils.dependencyCycleException(thisComponent, thatComponent);
            }
            validateComponentChildren(thisComponent, child);
        }
    }

    <T extends ViewComponent> void appendComponentsOfType(@NotNull ViewComponent component,
                                                          @NotNull Class<T> type,
                                                          @NotNull Set<T> componentSet) {
        if (type.isAssignableFrom(component.getClass())) {
            componentSet.add(type.cast(component));
        }
        for (ViewComponent child : component.getChildren()) {
            appendComponentsOfType(child, type, componentSet);
        }
    }
}
