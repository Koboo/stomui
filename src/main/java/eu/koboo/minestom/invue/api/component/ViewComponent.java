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

/**
 * A component is a child of a {@link ViewComponent} or a {@link RootViewComponent}.
 * You can add as many children to a component as you want. The rendering/execution order of
 * all child components is defined by their specified {@link Priority}.
 * The {@link Priority} is resolved by using the {@link ViewComponent#getPriority()} method,
 * which checks can be overridden, but also checks the {@link ComponentPriority} annotation by default.
 */
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

    /**
     * Adds a new child to this component. The given instance of the {@link ViewComponent}
     * needs to be unique within the whole component tree.
     * @param child The {@link ViewComponent} you want to add as child
     * @return This {@link ViewComponent}
     */
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

    /**
     * Removes the given {@link ViewComponent} child from this {@link ViewComponent}.
     * This method doesn't revert any item/inventory changes done by the given {@link ViewComponent}.
     * @param child The {@link ViewComponent} you want to remove.
     * @return This {@link ViewComponent}
     */
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

    /**
     * Checks if the given {@link ViewComponent} is a direct child of this {@link ViewComponent}.
     * @param child The {@link ViewComponent} you want to check.
     * @return true, if the {@link ViewComponent} is direct child.
     */
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

    /**
     * Resolves the root {@link ViewComponent} of the current component tree.
     * @return The root {@link ViewComponent}.
     */
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

    /**
     * Returns a single {@link ViewComponent}, which is assignable from the given class.
     * This method searches through the whole component tree.
     * @param type The class of the {@link ViewComponent} you want to find.
     * @return An instance of a {@link ViewComponent}.
     * @param <T> The generic type reference of the searched {@link ViewComponent}.
     */
    public @Nullable <T extends ViewComponent> T findComponentByType(Class<T> type) {
        Set<T> componentSet = new LinkedHashSet<>();
        ViewComponent rootAncestor = findRootAncestor();
        ViewComponentUtils.appendComponentsOfType(rootAncestor, type, componentSet);
        return componentSet.stream()
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns a List of {@link ViewComponent}s, which are assignable from the given class.
     * This method searches through the whole component tree.
     * @param type The class of the {@link ViewComponent}s you want to find.
     * @return A List with {@link ViewComponent}s.
     * @param <T> The generic type reference of the searched {@link ViewComponent}s.
     */
    public @NotNull <T extends ViewComponent> Set<T> findComponentsByType(Class<T> type) {
        Set<T> componentSet = new LinkedHashSet<>();
        ViewComponent rootAncestor = findRootAncestor();
        ViewComponentUtils.appendComponentsOfType(rootAncestor, type, componentSet);
        return Set.copyOf(componentSet);
    }

    /**
     * The returned List is not modifiable.
     * @return The List with all direct {@link ViewComponent} children.
     */
    public @NotNull List<ViewComponent> getChildren() {
        return List.copyOf(children);
    }

    /**
     * Returns null, if this is the root {@link ViewComponent}.
     * @return The parent of this {@link ViewComponent}.
     */
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
