package eu.koboo.minestom.invue.api.slots;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SlotUtility {

    public static final Integer BOTTOM_INVENTORY_SIZE = 36;

    public Position toPosition(int slot, int slotsPerColumn) {
        int row = slot / slotsPerColumn;
        int column = slot % slotsPerColumn;
        return Position.of(row, column);
    }

    public int toSlot(Position position, int slotsPerColumn) {
        return toSlot(position.getRow(), position.getColumn(), slotsPerColumn);
    }

    public int toSlot(int row, int column) {
        return toSlot(row, column, 9);
    }

    public int toSlot(int row, int column, int slotsPerColumn) {
        return row * slotsPerColumn + column;
    }

    public List<Integer> getSlotsBetween(Integer startSlot, Integer endSlot) {
        if (startSlot == null) {
            throw new NullPointerException("startSlot is null");
        }
        if (endSlot == null) {
            throw new NullPointerException("endSlot is null");
        }
        if (startSlot < 0) {
            throw new IllegalArgumentException("startSlot (" + startSlot + ") is negative");
        }
        if (endSlot < 0) {
            throw new IllegalArgumentException("endSlot (" + endSlot + ") is negative");
        }
        if (startSlot > endSlot) {
            throw new IllegalArgumentException("startSlot must be less than endSlot (start=" + startSlot +
                " > end=" + endSlot + ")");
        }
        List<Integer> slotList = new ArrayList<>();
        for (int i = startSlot; i <= endSlot; i++) {
            slotList.add(i);
        }
        return slotList;
    }

    public @NotNull List<Integer> mergeSlotLists(List<List<Integer>> slotLists) {
        List<Integer> mergedList = new ArrayList<>();
        for (int i = 0; i < slotLists.size(); i++) {
            mergedList.addAll(slotLists.get(i));
        }
        return mergedList;
    }

    public @NotNull List<List<Integer>> getSlotLists(@NotNull SlotDirection slotDirection,
                                                     int rowIndex, int columnIndex) {
        List<List<Integer>> listOfSlotLists = new ArrayList<>();
        int firstLength;
        int secondLength;
        if (slotDirection == SlotDirection.HORIZONTAL) {
            firstLength = rowIndex;
            secondLength = columnIndex;
        } else {
            firstLength = columnIndex;
            secondLength = rowIndex;
        }
        int slotsPerColumn = (columnIndex + 1);
        for (int firstIndex = 0; firstIndex <= firstLength; firstIndex++) {
            List<Integer> slotList = new ArrayList<>();
            for (int secondIndex = 0; secondIndex <= secondLength; secondIndex++) {
                int currentRow;
                int currentColumn;
                if (slotDirection == SlotDirection.HORIZONTAL) {
                    currentRow = firstIndex;
                    currentColumn = secondIndex;
                } else {
                    currentRow = secondIndex;
                    currentColumn = firstIndex;
                }
                slotList.add(currentRow * slotsPerColumn + currentColumn);
            }
            listOfSlotLists.add(slotList);
        }
        return listOfSlotLists;
    }

    public List<List<Integer>> getSlotListsByColumn(List<Integer> slotList, int columnIndex) {
        int rowSize = calculatePairIndex(slotList.size(), columnIndex);
        return getSlotLists(SlotDirection.HORIZONTAL, rowSize, columnIndex);
    }

    public List<List<Integer>> getSlotListsByRow(List<Integer> slotList, int rowIndex) {
        int columnSize = calculatePairIndex(slotList.size(), rowIndex);
        return getSlotLists(SlotDirection.VERTICAL, rowIndex, columnSize);
    }

    public @NotNull List<Integer> getAllSlots(int rowIndex, int columnIndex) {
        List<List<Integer>> listOfSlotLists = getSlotLists(SlotDirection.VERTICAL, rowIndex, columnIndex);
        return mergeSlotLists(listOfSlotLists);
    }

    public @NotNull List<Integer> getBorderSlots(@NotNull List<List<Integer>> horizontalSlotLists) {
        List<Integer> borderSlots = new ArrayList<>(horizontalSlotLists.removeFirst());
        if (!horizontalSlotLists.isEmpty()) {
            borderSlots.addAll(horizontalSlotLists.removeLast());
        }
        for (int i = 0; i < horizontalSlotLists.size(); i++) {
            List<Integer> allSlots = horizontalSlotLists.get(i);
            if (allSlots.isEmpty()) {
                continue;
            }
            borderSlots.add(allSlots.getFirst());
            borderSlots.add(allSlots.getLast());
        }
        return borderSlots;
    }

    public @NotNull List<Integer> getContentSlots(int rowIndex, int columnIndex) {
        List<List<Integer>> horizontalSlotList = getSlotLists(SlotDirection.HORIZONTAL, rowIndex, columnIndex);
        List<Integer> contentSlots = new ArrayList<>();
        for (int i = 0; i < horizontalSlotList.size(); i++) {
            List<Integer> allSlots = horizontalSlotList.get(i);
            allSlots.removeFirst();
            allSlots.removeLast();
            contentSlots.addAll(allSlots);
        }
        return contentSlots;
    }

    /**
     * This method gives back a List of all slots, which follow a snake pattern.
     * Example outputs:
     * input:
     * direction = SlotDirection.HORIZONTAL
     * startFirst = false
     * startSingle = false
     * output:
     * X X X X X X X X X
     * 0 0 0 0 0 0 0 0 X
     * X X X X X X X X X
     * X 0 0 0 0 0 0 0 0
     * X X X X X X X X X
     * 0 0 0 0 0 0 0 0 X
     * <p>
     * input:
     * direction = SlotDirection.VERTICAL
     * startFirst = false
     * startSingle = false
     * output:
     * X 0 X X X 0 X X X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X X X 0 X X X 0 X
     * <p>
     * input:
     * direction = SlotDirection.VERTICAL
     * startFirst = true
     * startSingle = false
     * output:
     * X X X 0 X X X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X 0 X 0 X 0 X
     * X 0 X X X 0 X X X
     * <p>
     * input:
     * direction = SlotDirection.VERTICAL
     * startFirst = true
     * startSingle = true
     * output:
     * X X 0 X X X 0 X X
     * 0 X 0 X 0 X 0 X 0
     * 0 X 0 X 0 X 0 X 0
     * 0 X 0 X 0 X 0 X 0
     * 0 X 0 X 0 X 0 X 0
     * 0 X X X 0 X X X 0
     *
     * @param slotDirection The direction the snake should travel along
     * @param rowIndex      The size of one row
     * @param columnIndex   The size of one column
     * @param startFirst    if the snake should start on first the slot or the last slot
     * @param startSingle   if the snake should start with a single slot or with a whole row/column
     * @return The list with all slots of the snake pattern
     */
    public @NotNull List<Integer> getSnakeSlots(SlotDirection slotDirection,
                                                int rowIndex, int columnIndex,
                                                boolean startFirst, boolean startSingle) {
        List<List<Integer>> allSlots = getSlotLists(slotDirection, rowIndex, columnIndex);
        List<Integer> snakeSlots = new ArrayList<>();
        boolean addSingle = startSingle;
        boolean addFirst = startFirst;
        for (List<Integer> allSlot : allSlots) {
            if (addSingle) {
                addSingle = false;
                if (addFirst) {
                    addFirst = false;
                    snakeSlots.add(allSlot.getFirst());
                } else {
                    addFirst = true;
                    snakeSlots.add(allSlot.getLast());
                }
            } else {
                addSingle = true;
                snakeSlots.addAll(allSlot);
            }
        }
        return snakeSlots;
    }

    private int calculatePairIndex(int listSize, int pairIndex) {
        if (pairIndex <= 0) {
            throw new IllegalArgumentException("Element size (" + pairIndex + ") must be greater than 0.");
        }
        if (listSize <= 0) {
            throw new IllegalArgumentException("List size (" + listSize + ") must be greater than 0.");
        }
        if (listSize % pairIndex != 0) {
            throw new IllegalArgumentException("List size (" + listSize + ") must be divisible by element size " +
                "(" + pairIndex + ").");
        }
        return listSize / pairIndex;
    }

    public int getHighestSlot(@NotNull List<Integer> slotList) {
        int highestSlot = 0;
        for (int i = 0; i < slotList.size(); i++) {
            int slot = slotList.get(i);
            if (slot <= highestSlot) {
                continue;
            }
            highestSlot = slot;
        }
        return highestSlot;
    }

    public int getLowestSlot(@NotNull List<Integer> slotList) {
        int lowestSlot = Integer.MAX_VALUE;
        for (int i = 0; i < slotList.size(); i++) {
            int slot = slotList.get(i);
            if (slot >= lowestSlot) {
                continue;
            }
            lowestSlot = slot;
        }
        return lowestSlot;
    }
}
