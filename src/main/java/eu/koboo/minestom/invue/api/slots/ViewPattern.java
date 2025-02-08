package eu.koboo.minestom.invue.api.slots;

import eu.koboo.minestom.invue.api.ViewType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ViewPattern {

    @NotNull Collection<String> getPattern();

    @NotNull Map<Integer, Character> getSlotsByCharacters();

    @NotNull List<Integer> getAllSlots();

    @NotNull List<Integer> getSlots(@NotNull Character slotCharacter);

    int getSlot(@NotNull Character slotCharacter);

    @NotNull List<List<Integer>> getListOfSlots(@NotNull Character... slotCharacters);

    @NotNull List<Integer> getMergedSlots(@NotNull Character... slotCharacter);

    @NotNull SlotBuilder toSlots(@NotNull Character... slotCharacter);

    void offsetTop(@NotNull ViewType viewType);
}
