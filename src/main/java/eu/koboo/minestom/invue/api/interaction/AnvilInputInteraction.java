package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.PlayerView;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AnvilInputInteraction {

    void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input);
}
