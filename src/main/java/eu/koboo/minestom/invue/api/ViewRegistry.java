package eu.koboo.minestom.invue.api;

import eu.koboo.minestom.invue.api.component.ViewComponent;
import eu.koboo.minestom.invue.api.interaction.Interactions;
import eu.koboo.minestom.invue.api.pagination.ItemLoader;
import eu.koboo.minestom.invue.api.pagination.ViewPagination;
import eu.koboo.minestom.invue.api.slots.SlotBuilder;
import eu.koboo.minestom.invue.api.slots.ViewPattern;
import eu.koboo.minestom.invue.core.MinestomInvue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the framework starting point, for nearly all instances.
 * Create a new {@link ViewRegistry} by using the following method:
 * <p>
 * {@link MinestomInvue#create()}
 * <p>
 */
public interface ViewRegistry {

    /**
     * Registers all listeners of this framework.
     */
    void enable();

    /**
     * Unregister all listeners and closes all currently open views.
     */
    void disable();

    /**
     * Creates and opens a new {@link PlayerView} for the iven {@link Player}
     * using the provided {@link ViewBuilder}. This method also executes
     * the {@link ViewComponent#modifyBuilder(ViewBuilder, Player)} method
     * before the {@link PlayerView} created.
     * @param player The provided {@link Player}.
     * @param viewBuilder The provided {@link ViewBuilder}.
     * @return A new created instance of {@link PlayerView}.
     */
    @NotNull PlayerView open(@NotNull Player player, @NotNull ViewBuilder viewBuilder);

    /**
     * Reopens an existing {@link PlayerView}.
     * You can't open a {@link PlayerView} to another {@link Player} than the {@link Player},
     * who was provided on creating the {@link PlayerView}.
     * This method also doesn't call {@link ViewComponent#onOpen(PlayerView, Player)}.
     * @param playerView The provided {@link PlayerView}.
     * @return The provided instance of {@link PlayerView}.
     */
    @NotNull PlayerView open(@NotNull PlayerView playerView);

    /**
     * Returns the currently open {@link PlayerView} by the provided {@link Player}.
     * @param player The provided {@link Player}.
     * @return An instance of {@link PlayerView}, if the {@link Player} has any open.
     * Otherwise, null.
     */
    @Nullable PlayerView getCurrentView(@NotNull Player player);

    /**
     * @return An unmodifiable Collection of all currently open {@link PlayerView}s.
     */
    @NotNull Collection<PlayerView> getAllViews();

    /**
     * Returns the last opened {@link PlayerView} by the provided {@link Player}.
     * <p>
     * Imagine it like a browser-history.
     * Reference documentation: {@link Interactions#backToLastView()}
     * @param player The provided {@link Player}.
     * @return An instance of {@link PlayerView}, if the {@link Player} has any last view.
     * Otherwise, null.
     */
    @Nullable PlayerView getLastView(@NotNull Player player);

    /**
     * Returns the next opened {@link PlayerView} by the provided {@link Player}.
     * <p>
     * Imagine it like a browser-history.
     * Reference documentation: {@link Interactions#forwardToNextView()}
     * @param player The provided {@link Player}.
     * @return An instance of {@link PlayerView}, if the {@link Player} has any next view.
     * Otherwise, null.
     */
    @Nullable PlayerView getNextView(@NotNull Player player);

    /**
     * See {@link ViewRegistry#pageable(ItemLoader, List, ItemStack)} for more information.
     */
    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull ViewPattern viewPattern,
                                             @NotNull Character... slotCharacters) {
        return pageable(itemLoader, viewPattern.getMergedSlots(slotCharacters), null);
    }

    /**
     * See {@link ViewRegistry#pageable(ItemLoader, List, ItemStack)} for more information.
     */
    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull SlotBuilder slotBuilder) {
        return pageable(itemLoader, slotBuilder.toList(), null);
    }

    /**
     * See {@link ViewRegistry#pageable(ItemLoader, List, ItemStack)} for more information.
     */
    default @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                             @NotNull List<Integer> slotList) {
        return pageable(itemLoader, slotList, null);
    }

    /**
     * Creates a new instance of {@link ViewPagination} using pages.
     * The returned instance needs to be added as child to any {@link ViewComponent}.
     * - {@link ViewComponent#addChild(ViewComponent)}.
     * <p>
     * @param itemLoader The {@link ItemLoader}, which fills the pagination with items.
     * @param slotList The list of slots, which are part of the pagination.
     * @param fillerItem The filler item, if no item is present for the specific page. Defaults to Air.
     * @return A new instance of {@link ViewPagination}.
     */
    @NotNull ViewPagination pageable(@NotNull ItemLoader itemLoader,
                                     @NotNull List<Integer> slotList,
                                     @Nullable ItemStack fillerItem);

    /**
     * See {@link ViewRegistry#scrollable(ItemLoader, ItemStack, List)} for more information.
     */
    default @NotNull ViewPagination scrollable(@NotNull ItemLoader itemLoader,
                                               @NotNull ViewPattern viewPattern,
                                               @NotNull Character... slotCharacters) {
        return scrollable(itemLoader, null, viewPattern.getListOfSlots(slotCharacters));
    }

    /**
     * See {@link ViewRegistry#scrollable(ItemLoader, ItemStack, List)} for more information.
     */
    default @NotNull ViewPagination scrollable(@NotNull ItemLoader itemLoader,
                                               @NotNull List<List<Integer>> listOfSlotLists) {
        return scrollable(itemLoader, null, listOfSlotLists);
    }

    /**
     * Creates a new instance of {@link ViewPagination} using scroll-like pages.
     * The returned instance needs to be added as child to any {@link ViewComponent}.
     * - {@link ViewComponent#addChild(ViewComponent)}.
     * <p>
     * @param itemLoader The {@link ItemLoader}, which fills the pagination with items.
     * @param fillerItem The filler item, if no item is present for the specific page. Defaults to Air.
     * @param listOfSlotLists The list of all slot-lists, which are part of the pagination.
     * @return A new instance of {@link ViewPagination}.
     */
    @NotNull ViewPagination scrollable(@NotNull ItemLoader itemLoader,
                                       @Nullable ItemStack fillerItem,
                                       @NotNull List<List<Integer>> listOfSlotLists);

    /**
     * See {@link ViewRegistry#pattern(Collection)}.
     */
    default ViewPattern pattern(@NotNull String... pattern) {
        return pattern(List.of(pattern));
    }

    /**
     * Creates a new {@link ViewPattern} instance.
     * See {@link ViewPattern} for more information.
     * @param pattern The pattern of the inventory.
     * @return A new instance of {@link ViewPattern}
     */
    ViewPattern pattern(@NotNull Collection<String> pattern);

    /**
     * Executes the given Function recursively on all components,
     * starting with the given component and traversing down the component tree.
     * @param component The starting point of execution.
     * @param function The execution function.
     */
    void executeComponents(ViewComponent component, Consumer<ViewComponent> function);
}
