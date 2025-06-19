package eu.koboo.minestom.examples.stomui.views.other;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.flags.Flags;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class AllowInteractionExampleProvider extends ViewProvider {

    ViewPattern pattern;

    public AllowInteractionExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("Allow interactions example")
            // These Flags make it easier to allow overall interactions
            // instead of iterating over every slot
            // and marking it interactable / allowing their interactions.
            .withFlags(
                Flags.ALLOW_BOTTOM_INTERACTION,
                Flags.ALLOW_ITEM_DRAGGING
            ));

        // This is the pattern, we want to fill with logic and items.
        // '#' = Placeholder item
        // ' ' = Interactable slot (Items from player can be placed here.)
        pattern = registry.pattern(
            "#########",
            "#       #",
            "#       #",
            "#       #",
            "#       #",
            "#########"
        );
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Getting all top border slots by the predefined pattern.
        List<Integer> topBorderSlots = pattern.getSlots('#');

        // Setting all placeholder items, based on the slotList from above.
        for (ViewItem viewItem : ViewItem.bySlotList(view, topBorderSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ");
        }

        // A simple close item, at the first slot of the top inventory.
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();

        // Allowing interactions/placing on all slots of the character ' ' (a whitespace).
        List<Integer> slotList = pattern.getSlots(' ');
        for (ViewItem viewItem : ViewItem.bySlotList(view, slotList)) {
            viewItem.allowClicking();
        }
    }
}
