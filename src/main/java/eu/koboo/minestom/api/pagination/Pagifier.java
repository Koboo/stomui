package eu.koboo.minestom.api.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Pagifier<T> {

    int maxItemsPerPage;
    List<List<T>> pages;

    public Pagifier(int maxItemsPerPage) {
        this.maxItemsPerPage = maxItemsPerPage;
        this.pages = new ArrayList<>();
    }

    public void addItem(@NotNull T item) {
        List<T> pageItemList;
        if (pages.isEmpty()) {
            pageItemList = new ArrayList<>();
            pages.add(pageItemList);
        } else {
            int pageIndex = pages.size() - 1;
            pageItemList = pages.get(pageIndex);
        }

        // Add a new page if last one is full
        if (pageItemList.size() >= maxItemsPerPage) {
            pageItemList = new ArrayList<>();
            pages.add(pageItemList);
        }

        pageItemList.add(item);
    }

    public void addItems(@NotNull Collection<T> itemCollection) {
        for (T item : itemCollection) {
            addItem(item);
        }
    }

    public @NotNull List<T> getPage(int pageNum) {
        if (pages.isEmpty()) {
            return Collections.emptyList();
        }
        int pageIndex = pageNum - 1;
        if (pageIndex < 0) {
            return Collections.emptyList();
        }
        int lastPageIndex = pages.size() - 1;
        if (pageIndex > lastPageIndex) {
            return Collections.emptyList();
        }
        return pages.get(pageIndex);
    }

    public void clear() {
        for (List<T> page : pages) {
            page.clear();
        }
        pages.clear();
        pages.add(new ArrayList<>());
    }

    public int getTotalItems() {
        int totalAmount = 0;
        for (List<T> page : pages) {
            totalAmount += page.size();
        }
        return totalAmount;
    }

    public int getTotalPages() {
        return pages.size();
    }

    public @NotNull List<List<T>> getAllPages() {
        return pages;
    }

    public @NotNull List<T> getAllItems() {
        List<List<T>> pages = getAllPages();
        List<T> items = new ArrayList<>();
        for (List<T> page : pages) {
            items.addAll(page);
        }
        return items;
    }
}
