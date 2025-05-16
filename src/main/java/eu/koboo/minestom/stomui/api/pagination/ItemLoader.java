package eu.koboo.minestom.stomui.api.pagination;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a loader to load a list of items into a pagination object.
 * See {@link ViewPagination#setItemLoader(ItemLoader)} for more information.
 * @param <T> Any type
 */
@FunctionalInterface
public interface ItemLoader<T> {

    /**
     * See {@link ViewPagination#setItemLoader(ItemLoader)} for more information.
     * @return A list with items
     */
    @NotNull List<T> load();
}
