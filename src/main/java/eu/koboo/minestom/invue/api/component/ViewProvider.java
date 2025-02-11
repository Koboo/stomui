package eu.koboo.minestom.invue.api.component;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;

/**
 * This class represents the root of a component tree and provides
 * an easy {@link ViewProvider#open(Player)} method to create a new view
 * by the given {@link ViewRegistry}.
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class ViewProvider extends ViewComponent {

    ViewRegistry registry;
    ViewBuilder builder;

    /**
     * Creates a new {@link ViewProvider} by the given {@link ViewRegistry}
     * and {@link ViewType}.
     *
     * @param viewRegistry An instance of {@link ViewRegistry}.
     * @param viewType     A value of {@link ViewType}.
     */
    public ViewProvider(ViewRegistry viewRegistry, ViewType viewType) {
        this(viewRegistry, ViewBuilder.of(viewType));
    }

    /**
     * Creates a new {@link ViewProvider} by the given {@link ViewRegistry}
     * and {@link ViewBuilder}.
     * It also adds this {@link ViewProvider} as component to the given
     * {@link ViewBuilder}.
     *
     * @param viewRegistry An instance of {@link ViewRegistry}.
     * @param viewBuilder  An instance of {@link ViewBuilder}.
     */
    public ViewProvider(ViewRegistry viewRegistry, ViewBuilder viewBuilder) {
        this.registry = viewRegistry;
        this.builder = viewBuilder.provider(this);
    }

    /**
     * Creates a new {@link PlayerView} for the given {@link Player}
     * and opens it.
     *
     * @param player The {@link Player}, which gets the open inventory.
     */
    public void open(Player player) {
        registry.open(player, builder);
    }
}
