package eu.koboo.minestom.stomui.api.pagination;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.component.ViewComponent;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;
import eu.koboo.minestom.stomui.core.pagination.AbstractPaginationComponent;
import eu.koboo.minestom.stomui.core.pagination.PageComponent;
import eu.koboo.minestom.stomui.core.pagination.ScrollComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents the abstraction of any implementing {@link ViewPagination}.
 * See more information, on how to create or use it:
 * - {@link ViewRegistry#pageable(ItemLoader, List, ItemStack)}
 * - {@link ViewRegistry#scrollable(ItemLoader, ItemStack, List)}
 * To use the created instance of {@link ViewPagination} you need to add it
 * as child to any {@link ViewComponent} Using {@link ViewComponent#addChild(ViewComponent)}.
 * Implementations:
 * - {@link AbstractPaginationComponent}
 * - {@link PageComponent}
 * - {@link ScrollComponent}
 */
public abstract class ViewPagination extends ViewComponent {

    /**
     * @return the number of calculated items per page of this pagination.
     */
    public abstract int getItemsPerPage();

    /**
     * @return the currently set page. It starts from 1.
     */
    public abstract int getCurrentPage();

    /**
     * @return the number of total pages, this pagination has.
     * This method only works after the item loading was triggered.
     */
    public abstract int getTotalPages();

    /**
     * @return the number of total items, this pagination has.
     */
    public abstract int getTotalItemAmount();

    /**
     * @return true, if there is a next page.
     */
    public abstract boolean hasNextPage();

    /**
     * Increments the current page and updates the view to show the items of that page.
     *
     * @param playerView The {@link PlayerView}, which changes page of the pagination.
     */
    public abstract void toNextPage(@NotNull PlayerView playerView);

    /**
     * @return true, if there is a previous page.
     */
    public abstract boolean hasPreviousPage();

    /**
     * Decrements the current page and updates the view to show the items of that page.
     *
     * @param playerView The {@link PlayerView}, which changes page of the pagination.
     */
    public abstract void toPreviousPage(@NotNull PlayerView playerView);

    /**
     * @return true, if the current page is the first page.
     */
    public abstract boolean isFirstPage();

    /**
     * @return true, if the current page is the last page.
     */
    public abstract boolean isLastPage();

    /**
     * Navigates the pagination to the new given page.
     * If the page doesn't exist, is too high or too less,
     * an exception is thrown.
     *
     * @param newPage    must be 1 or greater.
     * @param playerView The {@link PlayerView}, which changes page of the pagination.
     */
    public abstract void toPage(@NotNull PlayerView playerView, int newPage);

    /**
     * Recalls the given {@link ItemLoader#load(Pagifier)} method
     * and resets the current page back to 1.
     * It automatically calls the method {@link ViewPagination#toPage(PlayerView, int)},
     * after {@link ItemLoader} execution.
     *
     * @param playerView The {@link PlayerView}, which reloads the pagination.
     */
    public abstract void reloadItems(@NotNull PlayerView playerView);

    // void                         addItem(PrebuiltItem item)
    // void                         addItems(Collection<PrebuiltItem> item)
    // void                         removeItem(PrebuiltItem item)
    // void                         removeItems(Collection<PrebuiltItem> item)
    // Collection<PrebuiltItem>     getItems()
    // void                         clearItems()

    /**
     * @return the given fillerItem or AIR if no filler was set.
     */
    public abstract @NotNull ItemStack getFillerItem();

    /**
     * Returns the {@link Pagifier} instance, which holds the {@link PrebuiltItem} of this pagination.
     * @return The {@link Pagifier} instance
     */
    public abstract @Nullable Pagifier<PrebuiltItem> getPager();
}
