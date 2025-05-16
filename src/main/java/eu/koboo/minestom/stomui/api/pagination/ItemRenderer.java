package eu.koboo.minestom.stomui.api.pagination;

import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

/**
 * This interface is used to "map"/create a {@link PrebuiltItem} from the object used in
 * the {@link ViewPagination}. Every item displayed in the {@link ViewPagination} gets
 * rendered through this interface.
 * @param <T>
 */
public interface ItemRenderer<T> {

    /**
     * See explanation of this interface.
     * @param item The item as part of the {@link ViewPagination}.
     * @return The {@link PrebuiltItem} build from the given item.
     */
    PrebuiltItem render(T item);
}
