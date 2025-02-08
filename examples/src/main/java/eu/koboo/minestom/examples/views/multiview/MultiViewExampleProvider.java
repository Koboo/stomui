package eu.koboo.minestom.examples.views.multiview;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.ViewBuilder;
import eu.koboo.minestom.api.ViewRegistry;
import eu.koboo.minestom.api.ViewType;
import eu.koboo.minestom.api.component.RootViewComponent;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MultiViewExampleProvider extends RootViewComponent {

    public MultiViewExampleProvider(ViewRegistry viewRegistry) {
        super(viewRegistry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("MultiView"));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder builder, @NotNull Player player) {
        builder.title("Welcome, " + player.getName() + " to the MultiView!");
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Implement items by PlayerView and Player
    }
}
