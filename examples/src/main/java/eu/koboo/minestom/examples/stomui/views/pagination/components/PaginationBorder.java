package eu.koboo.minestom.examples.stomui.views.pagination.components;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.component.ViewComponent;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaginationBorder extends ViewComponent {

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Sets the material to gray stained-glass pane for all border slots in the top inventory.
        List<Integer> topSlots = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, topSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ");
        }
    }
}
