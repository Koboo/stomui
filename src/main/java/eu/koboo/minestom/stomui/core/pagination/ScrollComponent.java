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

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ApiStatus.Internal
public final class ScrollComponent<T> extends AbstractPaginationComponent<T> {

    List<List<Integer>> listOfSlotLists;

    public ScrollComponent(@NotNull ItemRenderer<T> itemRenderer,
                           @Nullable ItemStack fillerItem,
                           @NotNull List<List<Integer>> listOfSlotLists) {
        super(itemRenderer, fillerItem);
        if (listOfSlotLists.isEmpty()) {
            throw new IllegalArgumentException("slotList is empty!");
        }
        int itemsPerPage = -1;
        for (List<Integer> slotList : listOfSlotLists) {
            if (slotList == null) {
                throw new NullPointerException("slotList is null!");
            }
            if (slotList.isEmpty()) {
                throw new IllegalArgumentException("slotList is empty!");
            }
            if (itemsPerPage == -1) {
                itemsPerPage = slotList.size();
            }
            if (itemsPerPage != slotList.size()) {
                throw new RuntimeException("The columns of a scrollable list need to be the same size!");
            }
        }
        this.listOfSlotLists = listOfSlotLists;
    }

    @Override
    public int getMaximumItemsPerPage() {
        return listOfSlotLists.getFirst().size();
    }

    @Override
    void renderCurrentPage(@NotNull PlayerView playerView, int maxItemsPerPage) {
        // We treat every slotList as an individual page.
        // So we keep track of the previously rendered pages.
        int scrollPageTracker = currentPage;
        for (List<Integer> slotList : listOfSlotLists) {
            // Clean up the previous items.
            for (Integer itemSlot : slotList) {
                ViewItem.bySlot(playerView, itemSlot).material(Material.AIR);
            }

            // Sets the items of the current page.
            List<T> currentPageItemList = getPageByNumber(scrollPageTracker);
            updatePageBySlots(playerView, maxItemsPerPage, currentPageItemList, slotList);
            scrollPageTracker += 1;
        }
    }
}
