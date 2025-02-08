package eu.koboo.minestom.invue.api.component;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public abstract class ViewComponent {

    final String id;
    final List<ViewComponent> children;
    ViewComponent parent;

    public ViewComponent() {
        this.id = IdGenerator.generateRandomString(16);
        this.children = new ArrayList<>();
    }

    public @NotNull ViewComponent addChild(@NotNull ViewComponent child) {
        System.out.println(String.join(", ", children.stream().map(ViewComponent::toString).toList()));
        if (hasChild(child)) {
            throw new IllegalArgumentException("Child already exists: \n" +
                "  - this: " + this + "\n" +
                "  - child: " + child);
        }
        child.setParent(this);
        ViewComponentUtils.validateUniqueness(this);
        children.add(child);
        sortChildren();
        return this;
    }

    public @NotNull ViewComponent removeChild(@NotNull ViewComponent child) {
        if (!hasChild(child)) {
            throw new IllegalArgumentException("No child with id " + this);
        }
        child.setParent(null);
        children.remove(child);
        sortChildren();
        return this;
    }

    private void sortChildren() {
        children.sort(Comparator.comparing(ViewComponent::getPriority));
    }

    public boolean hasChild(@NotNull ViewComponent child) {
        if (children.contains(child)) {
            return true;
        }
        for (ViewComponent viewComponent : children) {
            if (!viewComponent.getId().equals(child.getId())) {
                continue;
            }
            return true;
        }
        return false;
    }

    public @NotNull ViewComponent findRootAncestor() {
        // Iterate in tree view to the beginning
        // of all nodes. We need to traverse down,
        // to validate uniqueness of components.
        ViewComponent ancestor = this;
        while (ancestor.getParent() != null) {
            ancestor = ancestor.getParent();
        }
        return ancestor;
    }

    public @Nullable <T extends ViewComponent> T findComponentByType(Class<T> type) {
        Set<T> componentSet = new LinkedHashSet<>();
        ViewComponent rootAncestor = findRootAncestor();
        ViewComponentUtils.appendComponentsOfType(rootAncestor, type, componentSet);
        return componentSet.stream()
            .findFirst()
            .orElse(null);
    }

    public @NotNull <T extends ViewComponent> Set<T> findComponentsByType(Class<T> type) {
        Set<T> componentSet = new LinkedHashSet<>();
        ViewComponent rootAncestor = findRootAncestor();
        ViewComponentUtils.appendComponentsOfType(rootAncestor, type, componentSet);
        return Set.copyOf(componentSet);
    }

    public @NotNull List<ViewComponent> getChildren() {
        return List.copyOf(children);
    }

    public @Nullable ViewComponent getParent() {
        return parent;
    }

    @Override
    public String toString() {
        String parentId = null;
        if (parent != null) {
            parentId = parent.getId();
        }
        return getClass().getSimpleName() + "{id=" + getId() + ", parentId=" + parentId + "}";
    }

    // Inventory methods, can safely be overridden.

    public @NotNull Priority getPriority() {
        ComponentPriority annotation = this.getClass().getAnnotation(ComponentPriority.class);
        if (annotation != null) {
            return annotation.value();
        }
        return Priority.MEDIUM;
    }

    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        // Default implementation
    }

    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        // Default implementation
    }

    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        // Default implementation
    }

    public abstract void onOpen(@NotNull PlayerView view, @NotNull Player player);
}
