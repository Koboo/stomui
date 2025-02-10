package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.component.ViewComponent;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This interface can be implemented by any {@link ViewComponent},
 * to get notified about any anvil input the player does.
 */
public interface AnvilInputInteraction {

    void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input);
}
