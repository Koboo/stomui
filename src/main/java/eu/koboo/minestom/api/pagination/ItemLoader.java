package eu.koboo.minestom.api.pagination;

import eu.koboo.minestom.api.item.PrebuiltItem;

@FunctionalInterface
public interface ItemLoader {

    void load(Pagifier<PrebuiltItem> pagifier);
}
