package eu.koboo.minestom.invue.api;

import eu.koboo.minestom.invue.api.component.RootViewComponent;
import eu.koboo.minestom.invue.api.flags.Flag;
import eu.koboo.minestom.invue.api.interaction.Interaction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface PlayerView {

    @NotNull String getId();

    @NotNull ViewRegistry getRegistry();

    @NotNull Player getPlayer();

    @NotNull ViewType getType();

    @NotNull RootViewComponent getRootComponent();

    @NotNull Inventory getTopInventory();

    @NotNull PlayerInventory getBottomInventory();

    void addFlags(@NotNull Flag... flags);

    boolean hasFlags(@NotNull Flag... flags);

    void removeFlags(@NotNull Flag... flags);

    void setInteraction(int rawSlot, @NotNull Interaction interaction);

    void setItemStack(int rawSlot, @NotNull ItemStack itemStack);

    @NotNull ItemStack getItemStack(int rawSlot);

    @NotNull Interaction getInteraction(int rawSlot);

    void updateState();
}
