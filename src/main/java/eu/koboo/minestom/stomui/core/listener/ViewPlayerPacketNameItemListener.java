package eu.koboo.minestom.stomui.core.listener;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.stomui.core.CorePlayerView;
import eu.koboo.minestom.stomui.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientNameItemPacket;

import java.util.function.Consumer;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewPlayerPacketNameItemListener implements Consumer<PlayerPacketEvent> {

    CoreViewRegistry registry;

    @Override
    public void accept(PlayerPacketEvent event) {
        if (!(event.getPacket() instanceof ClientNameItemPacket(String itemName))) {
            return;
        }
        Player player = event.getPlayer();
        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }

        if (playerView.getType() != ViewType.ANVIL) {
            return;
        }
        playerView.getTopInventory().update();
        anvilInputExecution(playerView, itemName);
        event.setCancelled(true);
    }

    private void anvilInputExecution(PlayerView playerView, String input) {
        registry.executeComponents(
            playerView.getProvider(),
            component -> {
                if (!(component instanceof AnvilInputInteraction interaction)) {
                    return;
                }
                interaction.onAnvilInput(playerView, playerView.getPlayer(), input);
            }
        );
    }
}
