package eu.koboo.minestom.invue.api.component;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the root of a component tree and provides
 * an easy {@link RootViewComponent#open(Player)} method.
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class RootViewComponent extends ViewComponent {

    ViewRegistry registry;
    ViewBuilder builder;

    /**
     * Creates a new {@link RootViewComponent} by the given {@link ViewRegistry}
     * and {@link ViewType}.
     *
     * @param viewRegistry An instance of {@link ViewRegistry}.
     * @param viewType     A value of {@link ViewType}.
     */
    public RootViewComponent(ViewRegistry viewRegistry, ViewType viewType) {
        this(viewRegistry, ViewBuilder.of(viewType));
    }

    /**
     * Creates a new {@link RootViewComponent} by the given {@link ViewRegistry}
     * and {@link ViewBuilder}.
     * It also adds this {@link RootViewComponent} as component to the given
     * {@link ViewBuilder}.
     *
     * @param viewRegistry An instance of {@link ViewRegistry}.
     * @param viewBuilder  An instance of {@link ViewBuilder}.
     */
    public RootViewComponent(ViewRegistry viewRegistry, ViewBuilder viewBuilder) {
        this.registry = viewRegistry;
        this.builder = viewBuilder.component(this);
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

    /**
     * See documentation on {@link ViewComponent#onOpen(PlayerView, Player)}.
     */
    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Default implementation
    }
}
