package eu.koboo.minestom.stomui.api.interaction;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

/**
 * This class is a wrapper, which holds the properties and instances, relevant for the context of
 * a clicked slot.
 * Basically, it just provides getters for several properties.
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
