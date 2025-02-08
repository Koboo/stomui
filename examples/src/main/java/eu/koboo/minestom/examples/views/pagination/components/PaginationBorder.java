package eu.koboo.minestom.examples.views.pagination.components;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.component.ViewComponent;
import eu.koboo.minestom.api.item.ViewItem;
import eu.koboo.minestom.api.pagination.ViewPagination;
import eu.koboo.minestom.api.slots.ViewPattern;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaginationBorder extends ViewComponent {

    public PaginationBorder(ViewPagination pagination, ViewPattern pattern) {
        addChild(new PaginationActionButtons(pagination, pattern));
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        List<Integer> topSlots = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, topSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ");
        }
    }
}
