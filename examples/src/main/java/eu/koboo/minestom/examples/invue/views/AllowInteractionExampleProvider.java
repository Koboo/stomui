package eu.koboo.minestom.examples.invue.views;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.RootViewComponent;
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
            .title("Allow interactions example"));
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
        List<Integer> topBorderSlots = view.getType().getTopBorderSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, topBorderSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ");
        }

        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();

        List<Integer> slotList = pattern.getSlots(' ');
        for (ViewItem viewItem : ViewItem.bySlotList(view, slotList)) {
            viewItem.allowClicking();
        }
    }
}
