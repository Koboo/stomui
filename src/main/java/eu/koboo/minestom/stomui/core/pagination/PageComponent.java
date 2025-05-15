package eu.koboo.minestom.stomui.core.pagination;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ItemRenderer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ApiStatus.Internal
public final class PageComponent<T> extends AbstractPaginationComponent<T> {

    List<Integer> slotList;

    public PageComponent(@NotNull ItemRenderer<T> itemRenderer,
                         @Nullable Comparator<T> itemSorter,
                         @Nullable ItemStack fillerItem,
                         @NotNull List<Integer> slotList) {
        super(itemRenderer, itemSorter, fillerItem);
        if (slotList.isEmpty()) {
            throw new IllegalArgumentException("slotList is empty!");
        }
        this.slotList = slotList;
    }

    @Override
    public int getMaximumItemsPerPage() {
        return slotList.size();
    }

    @Override
    void renderCurrentPage(@NotNull PlayerView playerView) {
        // Clean up the previous mess.
        for (Integer itemSlot : slotList) {
            ViewItem.bySlot(playerView, itemSlot).material(Material.AIR);
        }

        // Define the itemsPerPage once.
        int itemsPerPage = getMaximumItemsPerPage();

        // Sets the items of the current page.
        List<T> currentPageItemList = getPageByNumber(currentPage);
        setItemsInSlotsByPage(playerView, itemsPerPage, currentPageItemList, slotList);
    }
}
