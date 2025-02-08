package eu.koboo.minestom.api.interaction;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.ViewRegistry;
import eu.koboo.minestom.api.item.ViewItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public final class ViewAction {

    PlayerView view;
    InventoryPreClickEvent event;
    int rawSlot;
    ViewItem item;

    public ViewRegistry getRegistry() {
        return view.getRegistry();
    }

    public Player getPlayer() {
        return view.getPlayer();
    }
}
