package eu.koboo.minestom.invue.core.listener;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.invue.core.CorePlayerView;
import eu.koboo.minestom.invue.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientNameItemPacket;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewPlayerPacketNameItemListener implements Consumer<PlayerPacketEvent> {

    CoreViewRegistry registry;

    public ViewPlayerPacketNameItemListener(CoreViewRegistry registry) {
        this.registry = registry;
    }

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
        playerView.updateState();
        event.setCancelled(true);
    }

    private void anvilInputExecution(PlayerView playerView, String input) {
        registry.executeComponents(
            playerView.getRootComponent(),
            component -> {
                if (!(component instanceof AnvilInputInteraction interaction)) {
                    return;
                }
                interaction.onAnvilInput(playerView, playerView.getPlayer(), input);
            }
        );
    }
}
