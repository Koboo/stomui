package eu.koboo.minestom.api.interaction;

import eu.koboo.minestom.api.PlayerView;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AnvilInputInteraction {

    void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input);
}
