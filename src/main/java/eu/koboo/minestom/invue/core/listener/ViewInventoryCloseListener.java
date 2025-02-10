package eu.koboo.minestom.invue.core.listener;

import eu.koboo.minestom.invue.core.CorePlayerView;
import eu.koboo.minestom.invue.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewInventoryCloseListener implements Consumer<InventoryCloseEvent> {

    CoreViewRegistry registry;

    public ViewInventoryCloseListener(CoreViewRegistry registry) {
        this.registry = registry;
    }

    // Only fired by the following scenarios:
    // - Client-side inventory close
    // - Server-side Player#closeInventory() call
    @Override
    public void accept(InventoryCloseEvent event) {
        Player player = event.getPlayer();

        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }
        registry.resetPlayer(player);
        playerView.closeView();
    }
}
