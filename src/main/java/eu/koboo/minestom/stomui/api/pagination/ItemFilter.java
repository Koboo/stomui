package eu.koboo.minestom.stomui.api.pagination;

import org.jetbrains.annotations.NotNull;

/**
 * This interface allows to filter the items of the pagination.
 * The pagination creates a copy of the itemList and runs the filter on
 * it. If the {@link ItemFilter#include(T)} method returns true, the item is included,
 * if it returns false, the item is not displayed in the pagination.
 *
 * @param <T> Any object of the pagination.
 */
@FunctionalInterface
public interface ItemFilter<T> {

    /**
     * Decides if the item should be displayed by the pagination.
     *
     * @param item The item of the pagination.
     * @return true if it should be included, false if it should be excluded.
     */
    boolean include(@NotNull T item);
}
