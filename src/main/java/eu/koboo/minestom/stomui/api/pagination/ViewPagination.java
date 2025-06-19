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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the abstraction of any implementing {@link ViewPagination}.
 * See more information, on how to create or use it:
 * - {@link ViewRegistry#pageable(ItemRenderer, ItemStack, List)}
 * - {@link ViewRegistry#scrollable(ItemRenderer, ItemStack, List)}
 * To use the created instance of {@link ViewPagination} you need to add it
 * as child to any {@link ViewComponent} Using {@link ViewComponent#addChild(ViewComponent)}.
 * Implementations:
 * - {@link AbstractPaginationComponent}
 * - {@link PageComponent}
 * - {@link ScrollComponent}
 */
public abstract class ViewPagination<T> extends ViewComponent {

    /**
     * This method allows you to update the {@link ItemRenderer} within the pagination.
     * The {@link ItemRenderer} is responsible to create a {@link PrebuiltItem}
     * from any generic item added in the pagination.
     * We can't update the pagination without an {@link ItemRenderer},
     * that's why the argument is annotated with {@link NotNull}.
     * Keep in mind that you need to call {@link ViewPagination#update(PlayerView)}
     * to apply the changes on the {@link PlayerView}.
     *
     * @param itemRenderer A {@link Comparator} with the pagination generic type item.
     */
    public abstract void setItemRenderer(@NotNull ItemRenderer<T> itemRenderer);

    /**
     * This method allows you to change the sorting for the pagination.
     * If the itemSorter is set to null, the pagination just ignores it.
     * Keep in mind that you need to call {@link ViewPagination#update(PlayerView)}
     * to apply the changes on the {@link PlayerView}.
     *
     * @param itemSorter A {@link Comparator} with the pagination generic type item.
     */
    public abstract void setItemSorter(@Nullable Comparator<T> itemSorter);

    /**
     * This method allows you to change the filtering for the pagination.
     * If the itemFilter is set to null, the pagination just ignores it.
     * Keep in mind that you need to call {@link ViewPagination#update(PlayerView)}
     * to apply the changes on the {@link PlayerView}.
     * See {@link ItemFilter} for more information how the filter works.
     *
     * @param itemFilter A {@link ItemFilter} with the pagination generic type item.
     */
    public abstract void setItemFilter(@Nullable ItemFilter<T> itemFilter);

    /**
     * This method adds the given items to the pagination,
     * but it doesn't update the inventories of the players.
     * <p>
     * To update the items within the {@link PlayerView} you
     * need to call {@link ViewPagination#update(PlayerView)}.
     *
     * @param items A collection of paginated items, which should be added.
     */
    public abstract void addItems(@NotNull Collection<T> items);

    /**
     * This method removes the given items from the pagination,
     * but it doesn't update the inventories of the players.
     * <p>
     * To update the items within the {@link PlayerView} you
     * need to call {@link ViewPagination#update(PlayerView)}.
     *
     * @param items A collection of paginated items, which should be added.
     */
    public abstract void removeItems(@NotNull Collection<T> items);

    /**
     * This method clears all items in the pagination,
     * but it doesn't update the inventories of the players.
     * <p>
     * To update the items within the {@link PlayerView} you
     * need to call {@link ViewPagination#update(PlayerView)}.
     */
    public abstract void clearItems();

    /**
     * This method update the items within the given {@link PlayerView}.
     * Without calling that method, the inventories will not be updated,
     * no matter how often you modify or update the item list.
     *
     * @param playerView A {@link PlayerView} which should be updated.
     */
    public abstract void update(@NotNull PlayerView playerView);

    /**
     * @return An unmodifiable {@link List} of all items, within the pagination.
     */
    public abstract @NotNull List<T> getAllItems();

    /**
     * @return An unmodifiable {@link List} of all items, within the pagination,
     * after applying the set {@link ItemFilter}
     */
    public abstract @NotNull List<T> getAllFilteredItems();

    /**
     * @return An unmodifiable List of all items of the given page, using the index. Starts at 0.
     */
    public abstract @NotNull List<T> getPageByIndex(int pageIndex);

    /**
     * @return An unmodifiable List of all items of the given page, using the page number. Starts at 1.
     */
    public abstract @NotNull List<T> getPageByNumber(int pageNumber);

    /**
     * @return the currently set page. Starts with 1.
     */
    public abstract int getCurrentPage();

    /**
     * @return the next page if available. If no next page is available, it returns -1.
     */
    public abstract int getNextPage();

    /**
     * @return the previous page if available. If no previous page is available, it returns -1.
     */
    public abstract int getPreviousPage();

    /**
     * This method is also an equivalent for "getLastPage()".
     * So you can use it as that.
     *
     * @return the number of total pages this pagination has.
     */
    public abstract int getTotalPages();

    /**
     * @return the number of total items this pagination has.
     */
    public abstract int getTotalItems();

    /**
     * @return the number of total items this pagination has, after applying the {@link ItemFilter}.
     */
    public abstract int getTotalFilteredItems();

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
     * @return the given fillerItem or AIR if no filler was set.
     */
    public abstract @NotNull ItemStack getFillerItem();

    /**
     * This method returns the calculated slots amount per page
     * of the implementing pagination. For actual pages it returns
     * the whole page size. For scroll-component the page size is one scroll row/column.
     *
     * @return The number of maximum items per page/scroll row/column.
     */
    public abstract int getMaximumItemsPerPage();

    /**
     * @return true, if the pagination should execute a rebuild on page navigation / switching
     * within the opened {@link PlayerView}.
     * .
     */
    public abstract boolean rebuildsOnNavigation();

    /**
     * Allows you to disable / enable the rebuilding on a page navigation/switch.
     * @param value true, to enable rebuilding on page navigation.
     */
    public abstract void setRebuildOnNavigation(boolean value);
}
