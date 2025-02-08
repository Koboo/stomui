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
