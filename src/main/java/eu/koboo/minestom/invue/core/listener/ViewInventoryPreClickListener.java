package eu.koboo.minestom.invue.core.listener;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.flags.Flags;
import eu.koboo.minestom.invue.api.interaction.CooldownInteraction;
import eu.koboo.minestom.invue.api.interaction.Interaction;
import eu.koboo.minestom.invue.api.interaction.Interactions;
import eu.koboo.minestom.invue.api.interaction.ViewAction;
import eu.koboo.minestom.invue.api.item.ViewItem;
import eu.koboo.minestom.invue.api.utils.BottomSlotUtility;
import eu.koboo.minestom.invue.core.CorePlayerView;
import eu.koboo.minestom.invue.core.CoreViewRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;

import java.util.function.Consumer;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ViewInventoryPreClickListener implements Consumer<InventoryPreClickEvent> {

    private static final int OUTSIDE = -999;
    private static final int CURSOR = -1;

    CoreViewRegistry registry;

    @Override
    public void accept(InventoryPreClickEvent event) {
        Player player = event.getPlayer();
        AbstractInventory inventory = event.getInventory();
        if (inventory == null) {
            inventory = player.getInventory();
        }

        CorePlayerView playerView = registry.getCurrentView(player);
        if (playerView == null) {
            return;
        }
        event.setCancelled(true);

        int clickedSlot = event.getSlot();
        if (clickedSlot == CURSOR) {
            if (!playerView.hasFlags(Flags.CONVERT_CURSOR_TO_OUTSIDE_INTERACTION)) {
                return;
            }
            clickedSlot = OUTSIDE;
        }

        if (clickedSlot == OUTSIDE) {
            if (playerView.hasFlags(Flags.ALLOW_OUTSIDE_INTERACTION)) {
                event.setCancelled(false);
                return;
            }
            if (playerView.hasFlags(Flags.CLOSE_ON_OUTSIDE_INTERACTION)) {
                player.closeInventory();
                return;
            }
        }

        int rawSlot = clickedSlot;
        if (inventory instanceof PlayerInventory) {
            int normalizedBottomSlot = BottomSlotUtility.normalizeBottomSlot(clickedSlot);
            int topOffset = playerView.getType().getLastTopSlot() + 1;
            rawSlot = normalizedBottomSlot + topOffset;
        }

        if (playerView.hasCooldown(rawSlot)) {
            cooldownExecution(playerView, rawSlot);
            return;
        }

        if (playerView.getType().isBottomSlot(rawSlot)) {
            if (playerView.hasFlags(Flags.ALLOW_BOTTOM_INTERACTION)) {
                event.setCancelled(false);
                return;
            }
            if (playerView.hasFlags(Flags.CLOSE_ON_BOTTOM_INTERACTION)) {
                player.closeInventory();
                return;
            }
        }

        if (playerView.getDisabledClickTypes().contains(event.getClickType())) {
            event.setCancelled(true);
            return;
        }

        Interaction interaction = playerView.getInteractions().get(rawSlot);
        if (interaction == null) {
            interaction = Interactions.cancel();
        }
        ViewItem viewItem = ViewItem.bySlot(playerView, rawSlot);

        ViewAction viewAction = new ViewAction(playerView, event, rawSlot, viewItem);
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
}
