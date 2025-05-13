package eu.koboo.minestom.stomui.api;

import eu.koboo.minestom.stomui.api.slots.SlotDirection;
import eu.koboo.minestom.stomui.api.slots.SlotUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.inventory.InventoryType;

import java.util.List;

/**
 * Represents the size and type of the inventory.
 * It's also used to do a lot of slot utility calculations,
 * like the following:
 * - top-slot to bottom-slot
 * - all top-/bottom-slots
 * - all top-/bottom-border-slots
 * - toSlot(Position)
 * - toPosition(Slot)
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ViewType {

    SIZE_1_X_9(
        InventoryType.CHEST_1_ROW,
        1,
        9
    ),
    SIZE_2_X_9(
        InventoryType.CHEST_2_ROW,
        2,
        9
    ),
    SIZE_3_X_9(
        InventoryType.CHEST_3_ROW,
        3,
        9
    ),
    SIZE_4_X_9(
        InventoryType.CHEST_4_ROW,
        4,
        9
    ),
    SIZE_5_X_9(
        InventoryType.CHEST_5_ROW,
        5,
        9
    ),
    SIZE_6_X_9(
        InventoryType.CHEST_6_ROW,
        6,
        9
    ),
    SIZE_3_X_3(
        InventoryType.CRAFTER_3X3,
        3,
        3
    ),
    SIZE_5_X_1(
        InventoryType.HOPPER,
        1,
        5
    ),
    ANVIL(
        InventoryType.ANVIL,
        1,
        3
    ),
    ;

    public static final int BOTTOM_SLOTS_PER_ROW = 4;
    public static final int BOTTOM_ROW_INDEX = BOTTOM_SLOTS_PER_ROW - 1;
    public static final int BOTTOM_SLOTS_PER_COLUMN = 9;
    public static final int BOTTOM_COLUMN_INDEX = BOTTOM_SLOTS_PER_COLUMN - 1;

    int size;
    InventoryType inventoryType;
    int topSlotsPerRow;
    int topRowIndex;
    int topSlotsPerColumn;
    int topColumnIndex;
    int firstTopSlot;
    int lastTopSlot;
    int firstBottomSlot;
    int lastBottomSlot;
    List<Integer> topSlots;
    List<List<Integer>> topHorizontalSlots;
    List<List<Integer>> topVerticalSlots;
    List<Integer> bottomSlots;
    List<List<Integer>> bottomHorizontalSlots;
    List<List<Integer>> bottomVerticalSlots;

    ViewType(InventoryType inventoryType, int topSlotsPerRow, int topSlotsPerColumn) {
        this.size = topSlotsPerRow * topSlotsPerColumn;
        this.inventoryType = inventoryType;
        this.topSlotsPerRow = topSlotsPerRow;
        this.topRowIndex = topSlotsPerRow - 1;
        this.topSlotsPerColumn = topSlotsPerColumn;
        this.topColumnIndex = topSlotsPerColumn - 1;
        this.firstTopSlot = SlotUtility.toSlot(0, 0, topSlotsPerColumn);
        this.lastTopSlot = SlotUtility.toSlot(topRowIndex, topColumnIndex, topSlotsPerColumn);
        this.firstBottomSlot = lastTopSlot + 1;
        this.lastBottomSlot = lastTopSlot + SlotUtility.BOTTOM_INVENTORY_SIZE;

        this.topSlots = SlotUtility.getAllSlots(topRowIndex, topColumnIndex);
        this.topHorizontalSlots = SlotUtility.getSlotLists(SlotDirection.HORIZONTAL, topRowIndex, topColumnIndex);
        this.topVerticalSlots = SlotUtility.getSlotLists(SlotDirection.VERTICAL, topRowIndex, topColumnIndex);

        this.bottomSlots = SlotUtility.getAllSlots(BOTTOM_ROW_INDEX, BOTTOM_COLUMN_INDEX);
        this.bottomHorizontalSlots = SlotUtility.getSlotLists(SlotDirection.HORIZONTAL, BOTTOM_ROW_INDEX, BOTTOM_COLUMN_INDEX);
        this.bottomVerticalSlots = SlotUtility.getSlotLists(SlotDirection.VERTICAL, BOTTOM_ROW_INDEX, BOTTOM_COLUMN_INDEX);
    }

    public boolean isTopSlot(int slot) {
        return slot <= getLastTopSlot();
    }

    public boolean isBottomSlot(int slot) {
        return slot > getLastTopSlot();
    }

    public int offsetFromTopToBottom(int slot) {
        return slot + getLastTopSlot() + 1;
    }

    public List<List<Integer>> offsetFromTopToBottom(List<List<Integer>> slots) {
        return slots.stream()
            .map(slotList -> slotList.stream().map(this::offsetFromTopToBottom).toList())
            .toList();
    }

    public List<Integer> getTopBorderSlots() {
        List<List<Integer>> allSlots = SlotUtility.getSlotLists(
            SlotDirection.HORIZONTAL,
            topRowIndex,
            topColumnIndex
        );
        return SlotUtility.getBorderSlots(allSlots);
    }

    public List<Integer> getBottomBorderSlots() {
        List<List<Integer>> allSlots = SlotUtility.getSlotLists(
            SlotDirection.HORIZONTAL,
            BOTTOM_ROW_INDEX,
            BOTTOM_COLUMN_INDEX
        );
        return SlotUtility.getBorderSlots(offsetFromTopToBottom(allSlots));
    }

    public int toSlot(int rowIndex, int columnIndex) {
        int slotsPerColumn = topSlotsPerColumn;
        if (rowIndex > topRowIndex || columnIndex > topColumnIndex) {
            slotsPerColumn = ViewType.BOTTOM_SLOTS_PER_COLUMN;
        }
        return SlotUtility.toSlot(rowIndex, columnIndex, slotsPerColumn);
    }
}
