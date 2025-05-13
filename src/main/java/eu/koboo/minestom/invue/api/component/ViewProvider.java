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
     * Creates a new instance of {@link ViewProvider} by the given {@link ViewRegistry}
     * and {@link ViewType}.
     * This constructor is just a shortcut to avoid writing out
     * the creation of a new {@link ViewBuilder} by calling {@link ViewBuilder#of(ViewType)}.
     * See {@link ViewProvider#ViewProvider(ViewRegistry, ViewBuilder)} for more and detailed information.
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
     * It also adds this {@link ViewProvider} as a component to the given
     * {@link ViewBuilder} and is the initial starting point of the component tree (hierarchy).
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
     * and opens it. You can call this method on multiple players,
     * so all players look at the same components, it will still create
     * a separate {@link PlayerView} for each player.
     *
     * @param player The {@link Player}, which gets the open inventory.
     */
    public void open(Player player) {
        registry.open(player, builder);
    }
}
