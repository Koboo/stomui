package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.item.ViewItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

/**
 * This class is a wrapper, to be passed into {@link Interaction}.
 * It just provides getters for some properties.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public final class ViewAction {

    PlayerView view;
    InventoryPreClickEvent event;
    ViewItem item;

    public ViewRegistry getRegistry() {
        return view.getRegistry();
    }

    public Player getPlayer() {
        return view.getPlayer();
    }

    public int getRawSlot() {
        return item.getRawSlot();
    }
}
