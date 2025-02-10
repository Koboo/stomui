package eu.koboo.minestom.invue.core.slots;

import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.slots.SlotBuilder;
import eu.koboo.minestom.invue.api.slots.SlotUtility;
import eu.koboo.minestom.invue.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ApiStatus.Internal
public final class CoreViewPattern implements ViewPattern {

    Collection<String> pattern;
    Map<Integer, Character> slotByCharacterMapping;

    @ApiStatus.Internal
    public CoreViewPattern(@NotNull Collection<String> pattern) {
        this.pattern = pattern;
        this.slotByCharacterMapping = convertToMapping(pattern);
    }

    @Override
    public @NotNull Collection<String> getPattern() {
        return List.copyOf(pattern);
    }

    @Override
    public @NotNull Map<Integer, Character> getSlotsByCharacters() {
        return Collections.unmodifiableMap(slotByCharacterMapping);
    }

    @Override
    public @NotNull List<Integer> getAllSlots() {
        return List.copyOf(slotByCharacterMapping.keySet());
    }

    @Override
    public @NotNull List<Integer> getSlots(@NotNull Character slotCharacter) {
        if (slotByCharacterMapping.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> slotList = new ArrayList<>();
        for (Integer slot : slotByCharacterMapping.keySet()) {
            Character mappedChar = slotByCharacterMapping.get(slot);
            if (mappedChar == null) {
                continue;
            }
            if (!mappedChar.equals(slotCharacter)) {
                continue;
            }
            slotList.add(slot);
        }
        return slotList;
    }

    @Override
    public int getSlot(@NotNull Character slotCharacter) {
        if (slotByCharacterMapping.isEmpty()) {
            throw new IllegalArgumentException("Slot map is empty");
        }
        for (Integer slot : slotByCharacterMapping.keySet()) {
            Character mappedCharacter = slotByCharacterMapping.get(slot);
            if (mappedCharacter == null) {
                continue;
            }
            if (!mappedCharacter.equals(slotCharacter)) {
                continue;
            }
            return slot;
        }
        throw new IllegalArgumentException("No slot found for character " + slotCharacter);
    }

    @Override
    public @NotNull List<List<Integer>> getListOfSlots(@NotNull Character... slotCharacters) {
        List<List<Integer>> listOfSlotList = new ArrayList<>();
        for (Character slotCharacter : slotCharacters) {
            List<Integer> slotList = getSlots(slotCharacter);
            listOfSlotList.add(slotList);
        }
        return List.copyOf(listOfSlotList);
    }

    @Override
    public @NotNull List<Integer> getMergedSlots(@NotNull Character... slotCharacters) {
        return SlotUtility.mergeSlotLists(getListOfSlots(slotCharacters));
    }

    @Override
    public void offsetTop(@NotNull ViewType viewType) {
        int firstBottom = viewType.getFirstBottomSlot();
        List<Integer> slotList = getAllSlots();
        Map<Integer, Character> newSlotByCharacterMapping = new HashMap<>();
        for (Integer slot : slotList) {
            Character character = slotByCharacterMapping.remove(slot);
            if (character == null) {
                continue;
            }
            int offsetSlot = slot + firstBottom;
            newSlotByCharacterMapping.put(offsetSlot, character);
        }
        slotByCharacterMapping.clear();
        slotByCharacterMapping.putAll(newSlotByCharacterMapping);
    }

    @ApiStatus.Internal
    private static @NotNull Map<Integer, Character> convertToMapping(@NotNull Collection<String> patternCollection) {
        if (patternCollection.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Integer, Character> mappingMap = new LinkedHashMap<>();
        int totalRows = patternCollection.size();
        List<String> patternList = List.copyOf(patternCollection);
        for (int rowIndex = 0; rowIndex < totalRows; rowIndex++) {
            String patternLine = patternList.get(rowIndex);
            int slotsPerColumn = patternLine.length();
            for (int slotInColumn = 0; slotInColumn < slotsPerColumn; slotInColumn++) {
                int slot = rowIndex * slotsPerColumn + slotInColumn;
                char charAtSlot = patternLine.charAt(slotInColumn);
                mappingMap.put(slot, charAtSlot);
            }
        }
        return mappingMap;
    }
}
