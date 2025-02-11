package eu.koboo.minestom.invue.api.slots;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.RootViewComponent;
import eu.koboo.minestom.invue.api.item.ViewItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * This class can be used, to create an instance of {@link List<Integer>},
 * which holds filtered / specific slots.
 * It works with rawSlots, instead of inventory-specific slots.
 * Because this class also implements {@link Iterable<Integer>} it
 * can also be used in a for-loop like this:
 * <pre>
 * {@code
 *      SlotIterator slotIterator = SlotIterator.of(..);
 *      for (Integer slot : slotIterator) {
 *          ViewItem viewItem = ViewItem.bySlot(view, slot);
 *      }
 * }
 * </pre>
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SlotIterator implements Iterable<Integer> {

    final ViewType viewType;
    @Getter
    int startSlot;
    @Getter
    int endSlot;
    final List<Integer> includedSlotList;
    final List<Integer> blacklistedSlotList;

    private SlotIterator(@NotNull ViewType viewType) {
        this.viewType = viewType;
        this.startSlot = -1;
        this.endSlot = -1;
        this.includedSlotList = new ArrayList<>();
        this.blacklistedSlotList = new ArrayList<>();
    }

    @ApiStatus.Internal
    private SlotIterator executeOnMaterial(List<Integer> slots,
                                           @NotNull PlayerView playerView, @NotNull Material material,
                                           @NotNull Consumer<Integer> slotConsumer) {
        for (Integer rawSlot : slots) {
            ItemStack itemStack = playerView.getItemStack(rawSlot);
            if (itemStack.material() != material) {
                continue;
            }
            slotConsumer.accept(rawSlot);
        }
        return this;
    }

    /**
     * @param startSlot The start slot, needs to be greater or equal to 0.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator startSlot(int startSlot) {
        this.startSlot = startSlot;
        return this;
    }

    /**
     * @param endSlot The end slot, needs to be greater or equals to 0.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator endSlot(int endSlot) {
        this.endSlot = endSlot;
        return this;
    }

    /**
     * See more information:
     * - {@link SlotIterator#startSlot(int)}
     * - {@link SlotIterator#endSlot(int)}
     *
     * @param startSlot The start slot
     * @param endSlot   The end slot
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator slotRange(int startSlot, int endSlot) {
        endSlot(endSlot);
        startSlot(startSlot);
        return this;
    }

    /**
     * Defines the builder's start slot, by using row and column.
     *
     * @param startRow    The startRow, starts at 0.
     * @param startColumn The startRow, starts at 0.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator startPosition(int startRow, int startColumn) {
        return startSlot(viewType.toSlot(startRow, startColumn));
    }

    /**
     * Defines the builder's end slot, by using row and column.
     *
     * @param endRow    The endRow, starts at 0.
     * @param endColumn The endColumn, starts at 0.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator endPosition(int endRow, int endColumn) {
        return endSlot(viewType.toSlot(endRow, endColumn));
    }

    /**
     * Blacklists all slots, which are inside the given Collection.
     *
     * @param slots The Collection of slots to blacklist.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistSlots(@NotNull Collection<Integer> slots) {
        blacklistedSlotList.addAll(slots);
        return this;
    }

    /**
     * Blacklists all slots, which are inside the given array.
     *
     * @param slots The array of slots to blacklist.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistSlots(@NotNull Integer... slots) {
        return blacklistSlots(List.of(slots));
    }

    /**
     * Blacklists a single slot, based on the row-column pair
     *
     * @param row    The row of the inventory, starts at 0.
     * @param column The row of the inventory, starts at 0.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistSlot(int row, int column) {
        return blacklistSlots(viewType.toSlot(row, column));
    }

    /**
     * Find more information:
     * - {@link SlotIterator#blacklistSlots(Collection)}
     *
     * @param viewItems The array of viewItems to blacklist.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistSlots(@NotNull ViewItem... viewItems) {
        return blacklistSlots(Stream.of(viewItems).map(ViewItem::getRawSlot).toList());
    }

    /**
     * Blacklists all border slots of the top inventory.
     *
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistTopBorder() {
        return blacklistSlots(viewType.getTopBorderSlots());
    }

    /**
     * Blacklists all border slots of the bottom inventory.
     *
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistBottomBorder() {
        return blacklistSlots(viewType.getBottomBorderSlots());
    }

    /**
     * Blacklists all raw slots of a specific {@link Material} within the top-inventory,
     * using the given {@link PlayerView}.
     *
     * @param playerView The given {@link PlayerView}.
     * @param material   The blacklisted {@link Material}.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistTopMaterial(@NotNull PlayerView playerView, @NotNull Material material) {
        return executeOnMaterial(
            viewType.getTopSlots(),
            playerView, material,
            this::blacklistSlots
        );
    }

    /**
     * Blacklists all raw slots of a specific {@link Material} within the bottom-inventory,
     * using the given {@link PlayerView}.
     *
     * @param playerView The given {@link PlayerView}.
     * @param material   The blacklisted {@link Material}.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator blacklistBottomMaterial(@NotNull PlayerView playerView, @NotNull Material material) {
        return executeOnMaterial(
            viewType.getBottomSlots(),
            playerView,
            material,
            this::blacklistSlots
        );
    }

    /**
     * Adds slots as "hardset", so the given slots, doesn't need to
     * be in range of start and end slot.
     *
     * @param slots The collection of slots.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator includeSlots(@NotNull Collection<Integer> slots) {
        includedSlotList.addAll(slots);
        return this;
    }

    /**
     * Find more information:
     * - {@link SlotIterator#includeSlots(Collection)}
     *
     * @param slots The array of slots.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator includeSlots(@NotNull Integer... slots) {
        return includeSlots(List.of(slots));
    }

    /**
     * Find more information:
     * - {@link SlotIterator#includeSlots(Collection)}
     *
     * @param viewItems The array of viewItems to add.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator includeSlots(@NotNull ViewItem... viewItems) {
        return includeSlots(Stream.of(viewItems).map(ViewItem::getRawSlot).toList());
    }

    /**
     * Includes all raw slots of a specific {@link Material} within the top-inventory,
     * using the given {@link PlayerView}.
     *
     * @param playerView The given {@link PlayerView}.
     * @param material   The included {@link Material}.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator includeTopMaterial(@NotNull PlayerView playerView, @NotNull Material material) {
        return executeOnMaterial(
            viewType.getTopSlots(),
            playerView,
            material,
            this::includeSlots
        );
    }


    /**
     * Includes all raw slots of a specific {@link Material} within the bottom-inventory,
     * using the given {@link PlayerView}.
     *
     * @param playerView The given {@link PlayerView}.
     * @param material   The included {@link Material}.
     * @return This {@link SlotIterator} instance.
     */
    public SlotIterator includeBottomMaterial(@NotNull PlayerView playerView, @NotNull Material material) {
        return executeOnMaterial(
            viewType.getBottomSlots(),
            playerView,
            material,
            this::includeSlots
        );
    }

    /**
     * Creates a new List of Integers.
     * <p>
     * Adds the following sources to the slot list:
     * <p>
     * 1. All slots between start- and endSlot (if present)
     * 2. All slots of the "hardset" slots (if any present)
     * 2. Removes all slots of the "hardset" blacklisted slots
     * <p>
     *
     * @return The new unmodifiable instance of List with Integers,
     * which represents the generated rawSlots.
     */
    public List<Integer> toList() {
        List<Integer> baseSlots = new ArrayList<>();
        if (includedSlotList.isEmpty()) {
            if (startSlot == -1) {
                throw new IllegalStateException("startSlot must be set, but was -1");
            }
            if (endSlot == -1) {
                throw new IllegalStateException("endSlot must be set, but was -1");
            }
            if (startSlot >= endSlot) {
                throw new IllegalStateException("startSlot must be less than endSlot (" + startSlot + " >= " + endSlot + ")");
            }
            // Generate the slots between the start and end slots.
            baseSlots.addAll(SlotUtility.getSlotsBetween(startSlot, endSlot));
        }

        // Add all slots from the hardset list
        if (!includedSlotList.isEmpty()) {
            baseSlots.addAll(includedSlotList);
        }

        // Remove all slots from the hardset blacklist
        if (!blacklistedSlotList.isEmpty()) {
            baseSlots.removeAll(blacklistedSlotList);
        }

        // Cleaned slot list ready for return
        return baseSlots;
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @return An empty instance of {@link SlotIterator}.
     */
    public static SlotIterator of(@NotNull ViewType viewType) {
        return new SlotIterator(viewType);
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @return An empty instance of {@link SlotIterator}.
     */
    public static SlotIterator of(@NotNull ViewBuilder viewBuilder) {
        return of(viewBuilder.getType());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @return An empty instance of {@link SlotIterator}.
     */
    public static SlotIterator of(@NotNull RootViewComponent rootViewComponent) {
        return of(rootViewComponent.getBuilder());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @return An empty instance of {@link SlotIterator}.
     */
    public static SlotIterator of(@NotNull PlayerView playerView) {
        return of(playerView.getType());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param startSlot The slot, where the list should start.
     * @param endSlot   The slot, where the list should end.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator bySlotRange(@NotNull ViewType viewType, int startSlot, int endSlot) {
        return of(viewType).slotRange(startSlot, endSlot);
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param slotList A list, which is hard-defined
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator withSlots(@NotNull ViewType viewType, @NotNull List<Integer> slotList) {
        return of(viewType).includeSlots(slotList);
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param viewType The {@link ViewType}, which provides all top slots of an inventory.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allTopSlots(@NotNull ViewType viewType) {
        return of(viewType).slotRange(0, viewType.getLastTopSlot());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param viewType The {@link ViewType}, which provides all bottom slots of an inventory.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allBottomSlots(@NotNull ViewType viewType) {
        return of(viewType).slotRange(viewType.getFirstBottomSlot(), viewType.getLastBottomSlot());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param viewType The {@link ViewType}, which provides all slots (top and bottom) of an inventory.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allInventorySlots(@NotNull ViewType viewType) {
        return of(viewType).slotRange(viewType.getFirstTopSlot(), viewType.getLastBottomSlot());
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param playerView The {@link PlayerView} to check the items in.
     * @param material The included {@link Material}.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allTopMaterialSlots(@NotNull PlayerView playerView, @NotNull Material material) {
        return of(playerView.getType()).includeTopMaterial(playerView, material);
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param playerView The {@link PlayerView} to check the items in.
     * @param material The included {@link Material}.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allBottomMaterialSlots(@NotNull PlayerView playerView, @NotNull Material material) {
        return of(playerView.getType()).includeBottomMaterial(playerView, material);
    }

    /**
     * Creates a new instance of {@link SlotIterator}.
     * @param playerView The {@link PlayerView} to check the items in.
     * @param material The included {@link Material}.
     * @return A new instance of {@link SlotIterator}.
     */
    public static SlotIterator allMaterialSlots(@NotNull PlayerView playerView, @NotNull Material material) {
        return of(playerView.getType())
            .includeTopMaterial(playerView, material)
            .includeBottomMaterial(playerView, material);
    }

    @Override
    public @NotNull Iterator<Integer> iterator() {
        return toList().iterator();
    }
}
