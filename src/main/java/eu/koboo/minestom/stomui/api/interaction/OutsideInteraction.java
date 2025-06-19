package eu.koboo.minestom.stomui.api.interaction;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.component.ViewComponent;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.Click;
import org.jetbrains.annotations.NotNull;

/**
 * This interface can be implemented by any {@link ViewComponent},
 * to get notified about a click outside the inventory container.
 */
public interface OutsideInteraction {

    void onOutsideClick(@NotNull PlayerView playerView, @NotNull Player player, int slot, @NotNull Click click);
}
