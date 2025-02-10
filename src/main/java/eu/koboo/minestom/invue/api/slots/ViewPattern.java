package eu.koboo.minestom.invue.api.slots;

import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This interface represents the mapping between slots and characters.
 * The methods to create a new pattern are:
 * - {@link ViewRegistry#pattern(Collection)}
 * - {@link ViewRegistry#pattern(String...)}
 * <p>
 * It's an alternative to row-column-positioning and makes it easier to visualize
 * the actual structure of an inventory. This class is just a utility and is not
 * deeply integrated into the framework itself. So users still can choose,
 * how they want to resolve their wanted slots.
 */
public interface ViewPattern {

    /**
     * @return The provided pattern. This collection is unmodifiable.
     */
    @NotNull Collection<String> getPattern();

    /**
     * @return The mapping of slots by character. This Map if not modifiable.
     */
    @NotNull Map<Integer, Character> getSlotsByCharacters();

    /**
     * Gets all slots of this pattern
     *
     * @return A List with all slots.
     */
    @NotNull List<Integer> getAllSlots();

    /**
     * Gets a List of slots by the given slot character.
     *
     * @param slotCharacter the slot character
     * @return A list of all mapped slots by the character
     */
    @NotNull List<Integer> getSlots(@NotNull Character slotCharacter);

    /**
     * Gets a single slot by the given slot character.
     *
     * @param slotCharacter the slot character
     * @return the slot mapped by the character
     * @throws IllegalArgumentException if there's no mapping for this character
     */
    int getSlot(@NotNull Character slotCharacter);

    /**
     * Gets a List of slot-lists by getting {@link ViewPattern#getSlots(Character)} and
     * adding them into one list.
     *
     * @param slotCharacters the slot characters
     * @return A list with all slot-lists
     */
    @NotNull List<List<Integer>> getListOfSlots(@NotNull Character... slotCharacters);

    /**
     * Gets all Lists of slots by {@link ViewPattern#getListOfSlots(Character...)} and
     * merges them into one List.
     *
     * @param slotCharacters The included characters of the slot in the pattern
     * @return The List with all slots for the included characters.
     */
    @NotNull List<Integer> getMergedSlots(@NotNull Character... slotCharacters);

    /**
     * Offset all pattern slots by the given slot.
     * So basically it does:
     * patternSlot + slotOffset = newPatternSlot.
     *
     * @param slotOffset The offset to add to all pattern slots.
     */
    void offsetSlots(int slotOffset);

    /**
     * Offset all pattern slots, so that the returned slots are translated to
     * bottom inventory slots.
     *
     * @param viewType The {@link ViewType} to calculate top offset.
     */
    void offsetTopInventory(@NotNull ViewType viewType);
}
