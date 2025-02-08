package eu.koboo.minestom.invue.api.slots;

import eu.koboo.minestom.invue.api.ViewType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The interface View pattern.
 * TODO: Update generated javadocs
 */
public interface ViewPattern {

    /**
     * Gets pattern.
     *
     * @return the pattern
     */
    @NotNull Collection<String> getPattern();

    /**
     * Gets slots by characters.
     *
     * @return the slots by characters
     */
    @NotNull Map<Integer, Character> getSlotsByCharacters();

    /**
     * Gets all slots.
     *
     * @return the all slots
     */
    @NotNull List<Integer> getAllSlots();

    /**
     * Gets slots.
     *
     * @param slotCharacter the slot character
     * @return the slots
     */
    @NotNull List<Integer> getSlots(@NotNull Character slotCharacter);

    /**
     * Gets slot.
     *
     * @param slotCharacter the slot character
     * @return the slot
     */
    int getSlot(@NotNull Character slotCharacter);

    /**
     * Gets list of slots.
     *
     * @param slotCharacters the slot characters
     * @return the list of slots
     */
    @NotNull List<List<Integer>> getListOfSlots(@NotNull Character... slotCharacters);

    /**
     * Gets merged slots.
     *
     * @param slotCharacter the slot character
     * @return the merged slots
     */
    @NotNull List<Integer> getMergedSlots(@NotNull Character... slotCharacter);

    /**
     * To slots slot builder.
     *
     * @param slotCharacter the slot character
     * @return the slot builder
     */
    @NotNull SlotBuilder toSlots(@NotNull Character... slotCharacter);

    /**
     * Offset top.
     *
     * @param viewType the view type
     */
    void offsetTop(@NotNull ViewType viewType);
}
