package eu.koboo.minestom.stomui.core.pagination;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ItemRenderer;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract sealed class AbstractPaginationComponent<T> extends ViewPagination<T> permits PageComponent, ScrollComponent {

    final ItemRenderer<T> itemRenderer;
    final ItemStack fillerItem;

    final List<T> itemList;
    final List<List<T>> pagedItemList;

    Comparator<T> itemSorter;
    int currentPage;

    public AbstractPaginationComponent(@NotNull ItemRenderer<T> itemRenderer,
                                       @Nullable Comparator<T> itemSorter,
                                       @Nullable ItemStack fillerItem) {
        if (fillerItem == null) {
            fillerItem = ItemStack.of(Material.AIR);
        }
        this.fillerItem = fillerItem;
        this.itemList = new ArrayList<>();
        this.pagedItemList = new ArrayList<>();

        this.itemRenderer = itemRenderer;
        this.itemSorter = itemSorter;
        this.currentPage = 1;
    }

    @Override
    public void addItems(@NotNull Collection<T> itemCollection) {
        itemList.addAll(itemCollection);
    }

    @Override
    public void removeItems(@NotNull Collection<T> itemCollection) {
        itemList.removeAll(itemCollection);
    }

    @Override
    public void clearItems() {
        itemList.clear();
    }

    @Override
    public void update(@NotNull PlayerView playerView) {
        rebuildPages();
        if (currentPage > getTotalPages()) {
            currentPage = getTotalPages();
        }
        playerView.updateState();
        renderCurrentPage(playerView);
    }

    @Override
    public @NotNull List<T> getAllItems() {
        return Collections.unmodifiableList(itemList);
    }

    @Override
    public @NotNull List<T> getPageByIndex(int pageIndex) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("pageIndex must be set and positive. " +
                "(pageIndex=" + pageIndex + ")");
        }
        if (pageIndex >= pagedItemList.size()) {
            throw new IllegalArgumentException("pageIndex must be less than getTotalPages() " +
                "(pageIndex=" + pageIndex + " >= totalPages=" + getTotalPages() + ")");
        }
        return Collections.unmodifiableList(pagedItemList.get(pageIndex));
    }

    @Override
    public @NotNull List<T> getPageByNumber(int pageNumber) {
        return getPageByIndex(pageNumber - 1);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int getTotalPages() {
        return pagedItemList.size();
    }

    @Override
    public int getTotalItems() {
        return itemList.size();
    }

    @Override
    public boolean hasNextPage() {
        return getTotalPages() > currentPage;
    }

    @Override
    public void toNextPage(@NotNull PlayerView playerView) {
        toPage(playerView, currentPage + 1);
    }

    @Override
    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    @Override
    public void toPreviousPage(@NotNull PlayerView playerView) {
        toPage(playerView, currentPage - 1);
    }

    @Override
    public boolean isFirstPage() {
        return currentPage == 1;
    }

    @Override
    public boolean isLastPage() {
        return currentPage == getTotalPages();
    }

    @Override
    public void toPage(@NotNull PlayerView playerView, int newPage) {
        // Starts at 1, can't be zero or less.
        if (newPage < 1) {
            throw new IllegalArgumentException("newPage must be set and positive. " +
                "(newPage=" + newPage + ")");
        }
        int totalPages = getTotalPages();
        if (newPage > totalPages) {
            throw new IllegalArgumentException("newPage must be less than getTotalPages() " +
                "(newPage=" + newPage + " > totalPages=" + totalPages + ")");
        }
        currentPage = newPage;
        update(playerView);
    }

    @Override
    public @NotNull ItemStack getFillerItem() {
        return fillerItem;
    }

    @Override
    public void onOpen(@NotNull PlayerView playerView, @NotNull Player player) {
        update(playerView);
    }

    private void rebuildPages() {
        int maxItemsPerPage = getMaximumItemsPerPage();
        if (maxItemsPerPage < 1) {
            throw new IllegalArgumentException("itemsPerPage must be set and positive. " +
                "(itemsPerPage=" + maxItemsPerPage + ")");
        }

        int totalItemAmount = itemList.size();
        pagedItemList.clear();
        if (itemSorter != null) {
            itemList.sort(itemSorter);
        }

        // No items mean we don't need to rebuild the pagination.
        if (totalItemAmount > 0) {
            for (int pageIndex = 0; pageIndex < totalItemAmount; pageIndex += maxItemsPerPage) {
                int end = Math.min(pageIndex + maxItemsPerPage, totalItemAmount);
                List<T> itemSubList = itemList.subList(pageIndex, end);
                List<T> page = new ArrayList<>(itemSubList);
                pagedItemList.add(page);
            }
        }
    }

    abstract void renderCurrentPage(@NotNull PlayerView playerView);

    void setItemsInSlotsByPage(PlayerView playerView,
                               int itemsPerPage,
                               List<T> currentPageItemList,
                               List<Integer> slotList) {
        for (int pageItemIndex = 0; pageItemIndex < itemsPerPage; pageItemIndex++) {
            // Get the current slot of the page
            int itemSlot = slotList.get(pageItemIndex);
            ViewItem viewItem = ViewItem.bySlot(playerView, itemSlot);

            // Check if there is an ItemStack present in our itemLoader
            // for the currently iterated slot?
            if ((currentPageItemList.size() - 1) >= pageItemIndex) {
                T currentItem = currentPageItemList.get(pageItemIndex);
                PrebuiltItem prebuiltItem = itemRenderer.renderItem(currentItem);
                viewItem.applyPrebuilt(prebuiltItem);
                continue;
            }

            // No item, fill up.
            viewItem.item(getFillerItem());
        }
    }
}
