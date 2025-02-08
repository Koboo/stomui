package eu.koboo.minestom.api.item;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.interaction.Interaction;
import eu.koboo.minestom.api.slots.Position;
import eu.koboo.minestom.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ViewItem extends ModifiableItem {

    public static @NotNull List<ViewItem> bySlotList(PlayerView playerView, List<Integer> slotList) {
        List<ViewItem> viewItems = new ArrayList<>();
        for (Integer slot : slotList) {
            viewItems.add(new ViewItem(playerView, slot));
        }
        return List.copyOf(viewItems);
    }

    public static @NotNull ViewItem byPattern(PlayerView playerView, ViewPattern pattern, Character slotCharacter) {
        return bySlot(playerView, pattern.getSlot(slotCharacter));
    }

    public static @NotNull ViewItem byPosition(PlayerView playerView, Position position) {
        int slot = playerView.getType().toSlot(position.getRow(), position.getColumn());
        return bySlot(playerView, slot);
    }

    public static @NotNull ViewItem byRowColumn(PlayerView playerView, int row, int column) {
        int slot = playerView.getType().toSlot(row, column);
        return bySlot(playerView, slot);
    }

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

    public void applyPrebuilt(PrebuiltItem prebuiltItem) {
        item(prebuiltItem.getItem());
        interaction(prebuiltItem.getInteraction());
    }
}
