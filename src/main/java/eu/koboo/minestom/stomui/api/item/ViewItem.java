package eu.koboo.minestom.stomui.api.item;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.interaction.Interaction;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.Position;
import eu.koboo.minestom.stomui.api.slots.SlotIterator;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the actual {@link ItemStack} and {@link Interaction} of
 * a specific slot within the opened {@link PlayerView}.
 * It also provides some static methods to create new instances of {@link ViewItem}
 * for easier usage.
 * There are also several methods for easier modifications on the {@link ItemStack},
 * implemented in {@link ModifiableItem}.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ViewItem extends ModifiableItem {

    /**
     * Returns a List will all items within the given List of slots, for the given view.
     *
     * @param playerView The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param slotList   The List of slots passed into the {@link ViewItem}.
     * @return An unmodifiable List of {@link ViewItem}s.
     */
    public static @NotNull List<ViewItem> bySlotList(PlayerView playerView, List<Integer> slotList) {
        List<ViewItem> viewItems = new ArrayList<>();
        for (Integer slot : slotList) {
            viewItems.add(new ViewItem(playerView, slot));
        }
        return List.copyOf(viewItems);
    }

    /**
     * Find more information: {@link ViewItem#bySlotList(PlayerView, List)}.
     *
     * @param playerView   The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param slotIterator The List of slots passed into the {@link ViewItem}.
     * @return An unmodifiable List of {@link ViewItem}s.
     */
    public static @NotNull List<ViewItem> bySlotIterator(PlayerView playerView, SlotIterator slotIterator) {
        return bySlotList(playerView, slotIterator.toList());
    }

    /**
     * Returns a new instance of the item, with the given slot, for the given view.
     *
     * @param playerView    The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param pattern       The instance of the mapping {@link ViewPagination}.
     * @param slotCharacter The character to the wanted raw slot within the {@link ViewPagination}.
     * @return A new instance of {@link ViewItem}.
     */
    public static @NotNull ViewItem byPattern(PlayerView playerView, ViewPattern pattern, Character slotCharacter) {
        return bySlot(playerView, pattern.getSlot(slotCharacter));
    }

    /**
     * Returns a new instance of the item, with the given slot, for the given view.
     *
     * @param playerView The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param position   The position within the inventory.
     * @return A new instance of {@link ViewItem}.
     */
    public static @NotNull ViewItem byPosition(PlayerView playerView, Position position) {
        int slot = playerView.getType().toSlot(position.getRow(), position.getColumn());
        return bySlot(playerView, slot);
    }

    /**
     * Returns a new instance of the item, with the given slot, for the given view.
     *
     * @param playerView The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param row        The row within the inventory, starts at 0.
     * @param column     The column within the inventory, starts at 0.
     * @return A new instance of {@link ViewItem}.
     */
    public static @NotNull ViewItem byRowColumn(PlayerView playerView, int row, int column) {
        int slot = playerView.getType().toSlot(row, column);
        return bySlot(playerView, slot);
    }

    /**
     * Returns a new instance of the item, with the given slot, for the given view.
     *
     * @param playerView The {@link PlayerView} to pass into the {@link ViewItem}s.
     * @param rawSlot    The raw slot within the inventory.
     * @return A new instance of {@link ViewItem}.
     */
    public static @NotNull ViewItem bySlot(PlayerView playerView, int rawSlot) {
        return new ViewItem(playerView, rawSlot);
    }

    final PlayerView view;
    final int rawSlot;

    private ViewItem(PlayerView view, int rawSlot) {
        this.view = view;
        this.rawSlot = rawSlot;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return view.getItemStack(rawSlot);
    }

    @Override
    public @NotNull Interaction getInteraction() {
        return view.getInteraction(rawSlot);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ModifiableItem> T item(@NotNull ItemStack itemStack) {
        view.setItemStack(rawSlot, itemStack);
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ModifiableItem> T interaction(@NotNull Interaction interaction) {
        view.setInteraction(rawSlot, interaction);
        return (T) this;
    }

    /**
     * Applies the {@link ItemStack} and the {@link Interaction} of the {@link PrebuiltItem}
     * onto the instance of this {@link ViewItem}.
     *
     * @param prebuiltItem The {@link PrebuiltItem} to apply onto this {@link ViewItem}.
     */
    public void applyPrebuilt(PrebuiltItem prebuiltItem) {
        item(prebuiltItem.getItem());
        interaction(prebuiltItem.getInteraction());
    }
}
