package eu.koboo.minestom.stomui.core.pagination;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ItemLoader;
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
public final class ScrollComponent extends AbstractPaginationComponent {

    List<List<Integer>> listOfSlotLists;

    public ScrollComponent(@NotNull ItemLoader loader,
                           @Nullable ItemStack fillerItem,
                           @NotNull List<List<Integer>> listOfSlotLists) {
        super(loader, fillerItem);
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
    public int getItemsPerPage() {
        return listOfSlotLists.getFirst().size();
    }

    @Override
    public void renderPagination(@NotNull PlayerView playerView) {
        // Define the itemsPerPage once.
        int itemsPerPage = getItemsPerPage();

        // We treat every slotList as an individual page.
        // So we keep track of the previously rendered pages.
        int scrollPageTracker = currentPage;
        for (List<Integer> slotList : listOfSlotLists) {
            // Cleanup the previous items.
            for (Integer itemSlot : slotList) {
                ViewItem.bySlot(playerView, itemSlot).material(Material.AIR);
            }

            // Sets the items of the current page.
            List<PrebuiltItem> prebuiltItemList = itemPager.getPage(scrollPageTracker);
            for (int pageItemIndex = 0; pageItemIndex < itemsPerPage; pageItemIndex++) {
                // Get the current slot of the page
                int itemSlot = slotList.get(pageItemIndex);
                ViewItem viewItem = ViewItem.bySlot(playerView, itemSlot);

                // Check if there is an ItemStack present in our itemLoader
                // for the currently iterated slot?
                if ((prebuiltItemList.size() - 1) >= pageItemIndex) {
                    PrebuiltItem prebuiltItem = prebuiltItemList.get(pageItemIndex);
                    viewItem.applyPrebuilt(prebuiltItem);
                    continue;
                }

                // No item, fill up.
                viewItem.item(getFillerItem());
            }
            scrollPageTracker += 1;
        }
    }
}
