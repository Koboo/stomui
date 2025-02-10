package eu.koboo.minestom.invue.api.pagination;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.item.PrebuiltItem;

/**
 * This interface is used in {@link ViewPagination} to load any {@link PrebuiltItem}s into
 * the given {@link Pagifier}.
 */
@FunctionalInterface
public interface ItemLoader {

    /**
     * Gets called by the {@link ViewPagination#reloadItems(PlayerView)} method,
     * to load all items in the given {@link Pagifier}. The given {@link Pagifier}
     * is always empty and needs to be filled by the user.
     *
     * @param pagifier An instance of {@link Pagifier}, which is used in {@link ViewPagination}.
     */
    void load(Pagifier<PrebuiltItem> pagifier);
}
