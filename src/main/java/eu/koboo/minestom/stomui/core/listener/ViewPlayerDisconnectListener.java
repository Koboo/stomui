package eu.koboo.minestom.stomui.core.listener;

import eu.koboo.minestom.stomui.core.CorePlayerView;
import eu.koboo.minestom.stomui.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

import java.util.function.Consumer;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewPlayerDisconnectListener implements Consumer<PlayerDisconnectEvent> {

    CoreViewRegistry registry;

    @Override
    public void accept(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();

        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }
        registry.resetPlayer(player);
        playerView.closeView();
    }
}
