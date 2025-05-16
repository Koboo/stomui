package eu.koboo.minestom.stomui.api.slots;

import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.core.slots.CoreViewPattern;
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
 * Implementation: {@link CoreViewPattern}
 */
public interface ViewPattern {

    /**
     * @return The provided pattern-strings in a {@link Collection}.
     * This {@link Collection} is unmodifiable.
     */
    @NotNull Collection<String> getPattern();

    /**
     * @return The mapping of slots to character.
     * This {@link Map} is unmodifiable.
     */
    @NotNull Map<Integer, Character> getSlotsByCharacters();

    /**
     * Gets all slots of this pattern
     *
     * @return A {@link List} with all slots.
     */
    @NotNull List<Integer> getAllSlots();

    /**
     * Gets a {@link List} of slots by the given slot character.
     *
     * @param slotCharacter the slot character
     * @return A {@link List} of all mapped slots by the character
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
     * Gets a {@link List} of slot-lists by calling
     * - {@link ViewPattern#getSlots(Character)}
     * and cumulating all returned {@link List}s with slots
     * into a big {@link List} and returns it.
     *
     * @param slotCharacters the slot characters
     * @return A {@link List} with all slot-lists
     */
    @NotNull List<List<Integer>> getListOfSlots(@NotNull Character... slotCharacters);

    /**
     * Gets all {@link List}s of slots by calling
     * - {@link ViewPattern#getListOfSlots(Character...)}
     * and merges them into one on distinct {@link List}.
     *
     * @param slotCharacters The included characters of the slot in the pattern
     * @return The {@link List} with all slots for the included characters.
     */
    @NotNull List<Integer> getMergedSlots(@NotNull Character... slotCharacters);

    /**
     * Offset all pattern slots by the given slotOffset.
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
