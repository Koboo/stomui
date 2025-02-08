package eu.koboo.minestom.core.listener;

import eu.koboo.minestom.core.CorePlayerView;
import eu.koboo.minestom.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewPlayerDisconnectListener implements Consumer<PlayerDisconnectEvent> {

    CoreViewRegistry registry;

    public ViewPlayerDisconnectListener(CoreViewRegistry registry) {
        this.registry = registry;
    }

    // Only fired by the following scenarios:
    // - Client-side inventory close
    // - Server-side Player#closeInventory() call
    @Override
    public void accept(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();

        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }
        registry.clearPlayer(player);
        playerView.closeView();
    }
}
