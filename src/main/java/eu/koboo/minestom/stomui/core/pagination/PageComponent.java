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
public final class PageComponent extends AbstractPaginationComponent {

    List<Integer> slotList;

    public PageComponent(@NotNull ItemLoader itemLoader,
                         @NotNull List<Integer> slotList,
                         @Nullable ItemStack fillerItem) {
        super(itemLoader, fillerItem);
        if (slotList.isEmpty()) {
            throw new IllegalArgumentException("slotList is empty!");
        }
        this.slotList = slotList;
    }

    @Override
    public int getItemsPerPage() {
        return slotList.size();
    }

    @Override
    public void renderPagination(@NotNull PlayerView playerView) {
        // Cleanup the previous mess.
        for (Integer itemSlot : slotList) {
            ViewItem.bySlot(playerView, itemSlot).material(Material.AIR);
        }

        // Define the itemsPerPage once.
        int itemsPerPage = getItemsPerPage();

        // Sets the items of the current page.
        List<PrebuiltItem> prebuiltItemList = itemPager.getPage(currentPage);
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
    }
}
