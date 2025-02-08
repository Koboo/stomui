package eu.koboo.minestom.invue.core.pagination;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.item.PrebuiltItem;
import eu.koboo.minestom.invue.api.pagination.ItemLoader;
import eu.koboo.minestom.invue.api.pagination.Pagifier;
import eu.koboo.minestom.invue.api.pagination.ViewPagination;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractPaginationComponent extends ViewPagination {

    final ItemLoader itemLoader;
    final ItemStack fillerItem;

    @NonFinal
    Pagifier<PrebuiltItem> itemPager;

    @Getter
    @NonFinal
    int currentPage;

    public AbstractPaginationComponent(@NotNull ItemLoader itemLoader,
                                       @Nullable ItemStack fillerItem) {
        this.itemLoader = itemLoader;
        if (fillerItem == null) {
            fillerItem = ItemStack.of(Material.AIR);
        }
        this.fillerItem = fillerItem;
        this.currentPage = 1;
    }

    @Override
    public void reloadItems(@NotNull PlayerView playerView) {
        int itemsPerPage = getItemsPerPage();
        if (itemsPerPage < 1) {
            throw new IllegalArgumentException("itemsPerPage must be set and positive. " +
                "(itemsPerPage=" + itemsPerPage + ")");
        }
        if (itemPager == null) {
            itemPager = new Pagifier<>(itemsPerPage);
        }
        itemPager.clear();
        itemLoader.load(itemPager);

        // Maybe the item loader decided to be empty,
        // but we can always show the first empty page.
        //if (itemPager.getTotalPages() == 0) {
        //    return;
        //}

        // Render action!
        toPage(playerView, 1);
    }

    @Override
    public int getTotalItemAmount() {
        if (itemPager == null) {
            return 0;
        }
        return itemPager.getTotalItems();
    }

    @Override
    public boolean hasNextPage() {
        return itemPager.getTotalPages() > currentPage;
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
        return currentPage == itemPager.getTotalPages();
    }

    @Override
    public @NotNull ItemStack getFillerItem() {
        return fillerItem;
    }

    @Override
    public void toPage(@NotNull PlayerView playerView, int newPage) {
        // Starts at 1, can't be zero or less.
        if (newPage < 1) {
            throw new IllegalArgumentException("newPage must be set and positive. " +
                "(newPage=" + newPage + ")");
        }
        int totalPages = itemPager.getTotalPages();
        if (newPage > totalPages) {
            throw new IllegalArgumentException("newPage must be less than itemList.getTotalPages() " +
                "(" + newPage + " > " + totalPages + ")");
        }
        currentPage = newPage;
        playerView.updateState();
        renderPagination(playerView);
    }

    @Override
    public int getTotalPages() {
        if (itemPager == null) {
            throw new IllegalArgumentException("itemList is null, call reloadItems() first!");
        }
        return itemPager.getTotalPages();
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Check if we have loaded the items yet,
        // if not load them and return, since the load call will render the page for us.
        if (itemPager == null) {
            reloadItems(view);
            return;
        }

        // Just rendering the page. Nothing special.
        renderPagination(view);
    }

    @Override
    public @Nullable Pagifier<PrebuiltItem> getPager() {
        return itemPager;
    }

    public abstract void renderPagination(@NotNull PlayerView playerView);
}
