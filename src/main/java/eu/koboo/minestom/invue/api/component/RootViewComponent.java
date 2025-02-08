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

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class RootViewComponent extends ViewComponent {

    ViewRegistry registry;
    ViewBuilder builder;

    public RootViewComponent(ViewRegistry registry, ViewType viewType) {
        this(registry, ViewBuilder.of(viewType));
    }

    public RootViewComponent(ViewRegistry viewRegistry, ViewBuilder viewBuilder) {
        this.registry = viewRegistry;
        this.builder = viewBuilder.component(this);
    }

    public void open(Player player) {
        registry.open(player, builder);
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Default implementation
    }
}
