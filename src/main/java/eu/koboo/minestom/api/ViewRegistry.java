package eu.koboo.minestom.api;

import eu.koboo.minestom.api.component.ViewComponent;
import eu.koboo.minestom.api.pagination.ItemLoader;
import eu.koboo.minestom.api.pagination.ViewPagination;
import eu.koboo.minestom.api.slots.SlotBuilder;
import eu.koboo.minestom.api.slots.ViewPattern;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface ViewRegistry {

    void enable();

    void disable();

    @NotNull PlayerView open(@NotNull Player player, @NotNull ViewBuilder viewBuilder);

    @NotNull PlayerView open(@NotNull Player player, @NotNull PlayerView playerView);

    @Nullable PlayerView getCurrentView(@NotNull Player player);

    @Nullable PlayerView getLastView(@NotNull Player player);

    @Nullable PlayerView getNextView(@NotNull Player player);

    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull ViewPattern viewPattern,
                                             @NotNull Character... slotCharacters) {
        return pageable(itemLoader, viewPattern.getMergedSlots(slotCharacters), null);
    }

    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull SlotBuilder slotBuilder) {
        return pageable(itemLoader, slotBuilder.toList(), null);
    }

    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull List<Integer> slotList) {
        return pageable(itemLoader, slotList, null);
    }

    @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                     @NotNull List<Integer> slotList,
                                     @Nullable ItemStack fillerItem);

    default @NotNull ViewPagination scrollable(@NotNull ItemLoader loader,
                                               @NotNull ViewPattern viewPattern,
                                               @NotNull Character... slotCharacters) {
        return scrollable(loader, null, viewPattern.getListOfSlots(slotCharacters));
    }

    default @NotNull ViewPagination scrollable(@NotNull ItemLoader loader,
                                               @NotNull List<List<Integer>> listOfSlotLists) {
        return scrollable(loader, null, listOfSlotLists);
    }

    @NotNull ViewPagination scrollable(@NotNull ItemLoader loader,
                                       @Nullable ItemStack fillerItem,
                                       @NotNull List<List<Integer>> listOfSlotLists);

    default ViewPattern pattern(@NotNull String... pattern) {
        return pattern(List.of(pattern));
    }

    ViewPattern pattern(@NotNull Collection<String> pattern);

    void executeComponents(ViewComponent component, Consumer<ViewComponent> function);
}
