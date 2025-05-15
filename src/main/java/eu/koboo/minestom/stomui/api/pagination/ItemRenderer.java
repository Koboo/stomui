package eu.koboo.minestom.stomui.api.pagination;

import eu.koboo.minestom.stomui.api.item.PrebuiltItem;

public interface ItemRenderer<T> {

    PrebuiltItem renderItem(T item);
}
