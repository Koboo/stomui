package eu.koboo.minestom.examples.invue.views.multiview;

import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.ViewProvider;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TwoPlayerComponent extends ViewProvider {

    public TwoPlayerComponent(ViewRegistry viewRegistry) {
        super(viewRegistry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("MultiView"));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder builder, @NotNull Player player) {
        // Because all methods get a provided PlayerView OR Player,
        // you can always update the PlayerView according to the provided player.
        // That means, a component is not forced to only exist within one view.
        builder.title("Welcome, " + player.getName() + " to the component of two players!");
    }
}
