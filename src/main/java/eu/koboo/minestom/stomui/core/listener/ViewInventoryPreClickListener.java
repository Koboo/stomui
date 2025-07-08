package eu.koboo.minestom.stomui.core.listener;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.flags.Flags;
import eu.koboo.minestom.stomui.api.interaction.*;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.utils.BottomSlotUtility;
import eu.koboo.minestom.stomui.core.CorePlayerView;
import eu.koboo.minestom.stomui.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewInventoryPreClickListener implements Consumer<InventoryPreClickEvent> {

    // This slot describes multiple clicks, e.g. outside the inventory
    // or a start drag
    private static final int MAGIC = -999;
    private static final int CURSOR = -1;

    CoreViewRegistry registry;

    @Override
    public void accept(InventoryPreClickEvent event) {
        Player player = event.getPlayer();
        AbstractInventory inventory = event.getInventory();
        /*
        if (inventory == null) {
            inventory = player.getInventory();
        }
        */

        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }
        event.setCancelled(true);

        int clickedSlot = event.getSlot();
        Click click = event.getClick();
        Class<? extends @NotNull Click> clickClass = click.getClass();
        log.trace("{} -> Clicked slot {} with type {}", player.getUsername(), clickedSlot, clickClass.getSimpleName());
        if (clickedSlot == CURSOR) {
            if (!playerView.hasFlags(Flags.CONVERT_CURSOR_TO_MAGIC_SLOT)) {
                log.trace("{} -> Detected cursor click.", player.getUsername());
                return;
            }
            log.trace("{} -> Converted {} to {}", player.getUsername(), clickedSlot, MAGIC);
            clickedSlot = MAGIC;
        }

        int rawSlot = clickedSlot;
        if (inventory instanceof PlayerInventory && clickedSlot != MAGIC) {
            int normalizedBottomSlot = BottomSlotUtility.normalizeBottomSlot(clickedSlot);
            int topOffset = playerView.getType().getLastTopSlot() + 1;
            rawSlot = normalizedBottomSlot + topOffset;
            log.trace("{} -> Converted to rawSlot {} in inventory {}", player.getUsername(), rawSlot, inventory.getClass().getSimpleName());
        }

        if (playerView.hasCooldown(rawSlot)) {
            cooldownExecution(playerView, rawSlot);
            log.trace("{} -> Cooldown execution {}", player.getUsername(),  rawSlot);
            return;
        }

        if (clickedSlot == MAGIC) {
            if(clickClass.equals(Click.LeftDropCursor.class) || clickClass.equals(Click.RightDropCursor.class)) {
                outsideClickExecution(playerView, rawSlot, click);
                event.setCancelled(true);
                return;
            }
            if(clickClass.equals(Click.LeftDrag.class) || clickClass.equals(Click.RightDrag.class)) {
                if (playerView.hasFlags(Flags.ALLOW_ITEM_DRAGGING)) {
                    event.setCancelled(false);
                    log.trace("{} -> Allowed item dragging click", player.getUsername());
                    return;
                }
                log.trace("{} -> Cancelled item dragging click", player.getUsername());
                event.setCancelled(true);
                return;
            }
        }

        if (playerView.getType().isBottomSlot(rawSlot)) {
            if (playerView.hasFlags(Flags.ALLOW_BOTTOM_INTERACTION)) {
                event.setCancelled(false);
                log.trace("{} -> Allowed bottom click", player.getUsername());
                return;
            }
            if (playerView.hasFlags(Flags.CLOSE_ON_BOTTOM_INTERACTION)) {
                player.closeInventory();
                log.trace("{} -> Closing on bottom click", player.getUsername());
                return;
            }
        }

        if (playerView.getDisabledClickTypes().contains(clickClass)) {
            event.setCancelled(true);
            log.trace("{} -> Click with disabled click type {}", player.getUsername(), clickClass.getSimpleName());
            return;
        }

        Interaction interaction = playerView.getInteractions().get(rawSlot);
        if (interaction == null) {
            interaction = Interactions.cancel();
        }
        ViewItem viewItem = ViewItem.bySlot(playerView, rawSlot);

        ViewAction viewAction = new ViewAction(playerView, event, viewItem);
        interaction.interact(viewAction);
    }

    private void cooldownExecution(PlayerView playerView, int rawSlot) {
        registry.executeComponents(
            playerView.getProvider(),
            component -> {
                if (!(component instanceof CooldownInteraction interaction)) {
                    return;
                }
                interaction.onSlotCooldown(playerView, playerView.getPlayer(), rawSlot);
            }
        );
    }

    private void outsideClickExecution(@NotNull PlayerView playerView, int rawSlot, @NotNull Click click) {
        registry.executeComponents(
            playerView.getProvider(),
            component -> {
                if (!(component instanceof OutsideInteraction interaction)) {
                    return;
                }
                interaction.onOutsideClick(playerView, playerView.getPlayer(), rawSlot, click);
            }
        );
    }
}
