package eu.koboo.minestom.examples.invue.views.other;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.RootViewComponent;
import eu.koboo.minestom.invue.api.flags.Flags;
import eu.koboo.minestom.invue.api.item.ViewItem;
import eu.koboo.minestom.invue.api.slots.ViewPattern;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class AllowInteractionExampleProvider extends RootViewComponent {

    ViewPattern pattern;

    public AllowInteractionExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("Allow interactions example")
            // This Flag makes it easier to allow bottom interactions
            // instead of iterating over every bottom slot
            // and marking it interactable/allowing interactions.
            .withFlags(Flags.ALLOW_BOTTOM_INTERACTION));

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
