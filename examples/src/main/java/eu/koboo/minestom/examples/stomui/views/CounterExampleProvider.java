package eu.koboo.minestom.examples.stomui.views;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CounterExampleProvider extends ViewProvider {

    private int count;

    public CounterExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_5_X_1));
        this.count = 0;
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title("Count: " + count);
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        List<Integer> allSlotsOfTopInventory = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, allSlotsOfTopInventory)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .cancelClicking();
        }
    }

    @Override
    public void onRebuild(@NotNull PlayerView view, @NotNull Player player) {
        ViewItem.bySlot(view, 2)
            .material(Material.GREEN_DYE)
            .displayName("Count: " + count)
            .lore(
                "Click to increment count.",
                "Current count is: " + count
            )
            .interaction(action -> {
                count++;
                view.executeRebuild();
            });
    }
}
