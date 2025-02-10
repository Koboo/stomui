package eu.koboo.minestom.invue.api.slots;

import eu.koboo.minestom.invue.api.ViewType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class can be used, to create an instance of {@link List<Integer>},
 * which holds filtered / specific slots.
 * It works with rawSlots, instead of inventory-specific slots.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SlotBuilder {

    @Getter
    int startSlot;
    @Getter
    int endSlot;
    final List<Integer> slotList;
    final List<Integer> blacklist;

    public SlotBuilder() {
        this.startSlot = -1;
        this.endSlot = -1;
        this.slotList = new ArrayList<>();
        this.blacklist = new ArrayList<>();
    }

    public SlotBuilder startSlot(int startSlot) {
        this.startSlot = startSlot;
        return this;
    }

    public SlotBuilder endSlot(int endSlot) {
        this.endSlot = endSlot;
        return this;
    }

    public SlotBuilder slotRange(int startSlot, int endSlot) {
        endSlot(endSlot);
        startSlot(startSlot);
        return this;
    }

    public SlotBuilder startPosition(int startRow, int startColumn) {
        return startSlot(SlotUtility.toSlot(startRow, startColumn));
    }

    public SlotBuilder startPosition(int startRow, int startColumn, int columnSize) {
        return startSlot(SlotUtility.toSlot(startRow, startColumn, columnSize));
    }

    public SlotBuilder endPosition(int endRow, int endColumn) {
        return endSlot(SlotUtility.toSlot(endRow, endColumn));
    }

    public SlotBuilder endPosition(int endRow, int endColumn, int columnSize) {
        return endSlot(SlotUtility.toSlot(endRow, endColumn, columnSize));
    }

    public SlotBuilder blacklist(@NotNull Collection<Integer> slots) {
        blacklist.addAll(slots);
        return this;
    }

    public SlotBuilder blacklist(@NotNull Integer... slots) {
        return blacklist(List.of(slots));
    }

    public SlotBuilder blacklist(int row, int column) {
        return blacklist(SlotUtility.toSlot(row, column, 9));
    }

    public SlotBuilder blacklist(int row, int column, int columnSize) {
        return blacklist(SlotUtility.toSlot(row, column, columnSize));
    }

    public SlotBuilder blacklistTopBorder(@NotNull ViewType type) {
        return blacklist(type.getTopBorderSlots());
    }

    public SlotBuilder blacklistBottomBorder(@NotNull ViewType type) {
        return blacklist(type.getBottomBorderSlots());
    }

    public SlotBuilder slots(@NotNull Collection<Integer> slots) {
        slotList.addAll(slots);
        return this;
    }

    public SlotBuilder slots(@NotNull Integer... slots) {
        return slots(List.of(slots));
    }

    public SlotBuilder listOfSlots(@NotNull List<List<Integer>> listOfSlots) {
        return slots(SlotUtility.mergeSlotLists(listOfSlots));
    }

    public List<Integer> getDefinedSlotList() {
        return Collections.unmodifiableList(slotList);
    }

    public List<Integer> getDefinedBlackList() {
        return Collections.unmodifiableList(blacklist);
    }

    /**
     * Applies all filters, the blacklist and iterations to build the wanted List<Integer>,
     * containing rawSlots.
     *
     * @return The list with wanted rawSlots.
     */
    public List<Integer> toList() {
        List<Integer> baseSlots = new ArrayList<>();
        if (slotList.isEmpty()) {
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

        // Adding all slots from the hard defined list
        if (!slotList.isEmpty()) {
            // Adding all slots form the defined slot list
            baseSlots.addAll(slotList);
        }

        // Remove all slots from the hard defined blacklist
        if (!blacklist.isEmpty()) {
            baseSlots.removeAll(blacklist);
        }

        // Cleaned slot list ready for return
        return baseSlots;
    }

    /**
     * @return An empty instance of {@link SlotBuilder}.
     */
    public static SlotBuilder builder() {
        return new SlotBuilder();
    }

    /**
     * @param startSlot The slot, where the list should start.
     * @param endSlot   The slot, where the list should end.
     * @return A new instance of {@link SlotBuilder}.
     */
    public static SlotBuilder builder(int startSlot, int endSlot) {
        return builder().startSlot(startSlot).endSlot(endSlot);
    }

    /**
     * @param slotList A list, which is hard-defined
     * @return A new instance of {@link SlotBuilder}.
     */
    public static SlotBuilder builder(List<Integer> slotList) {
        return builder().slots(slotList);
    }

    /**
     * @param type The {@link ViewType}, which provides all top slots of an inventory.
     * @return A new instance of {@link SlotBuilder}.
     */
    public static SlotBuilder allTopSlots(ViewType type) {
        return builder(0, type.getLastTopSlot());
    }

    /**
     * @param type The {@link ViewType}, which provides all bottom slots of an inventory.
     * @return A new instance of {@link SlotBuilder}.
     */
    public static SlotBuilder allBottomSlots(ViewType type) {
        return builder().startSlot(type.getFirstBottomSlot()).endSlot(type.getLastBottomSlot());
    }

}
