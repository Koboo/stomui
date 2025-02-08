package eu.koboo.minestom.invue.api.pagination;

import eu.koboo.minestom.invue.api.item.PrebuiltItem;

@FunctionalInterface
public interface ItemLoader {

    void load(Pagifier<PrebuiltItem> pagifier);
}
