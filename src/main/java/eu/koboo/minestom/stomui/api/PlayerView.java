package eu.koboo.minestom.stomui.api;

import eu.koboo.minestom.stomui.api.component.ViewComponent;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.flags.Flag;
import eu.koboo.minestom.stomui.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.stomui.api.interaction.Interaction;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.core.CorePlayerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the combination of a {@link Player}, with its open
 * top-inventory {@link Inventory} and bottom-inventory {@link PlayerInventory}.
 * It also holds several view-relevant data and provides several getters.
 * Implementation: {@link CorePlayerView}
 */
public interface PlayerView {

    /**
     * @return The generated id of this instance.
     */
    @NotNull String getId();

    /**
     * @return The provided {@link ViewRegistry} of the {@link ViewProvider}.
     */
    @NotNull ViewRegistry getRegistry();

    /**
     * @return The {@link Player} of this {@link PlayerView}.
     */
    @NotNull Player getPlayer();

    /**
     * @return The configured {@link ViewType}, provided by the {@link ViewBuilder}.
     */
    @NotNull ViewType getType();

    /**
     * @return The configured {@link ViewProvider}, provided by the {@link ViewBuilder}.
     */
    @NotNull ViewProvider getProvider();

    /**
     * @return The instance of the {@link Inventory}, which is the open top-inventory.
     * Created by the configured {@link PlayerView#getType()}
     */
    @NotNull Inventory getTopInventory();

    /**
     * @return The instance of the {@link PlayerInventory}, which is the open bottom-inventory.
     * Delegates to {@link Player#getInventory()}.
     */
    @NotNull PlayerInventory getBottomInventory();

    /**
     * Adds all provided {@link Flag}s to this view.
     *
     * @param flags All {@link Flag}s, which should be added.
     */
    void addFlags(@NotNull Flag... flags);

    /**
     * Checks if the view has all provided {@link Flag}s.
     *
     * @param flags All {@link Flag}s, which should be checked.
     * @return true, if the view contains all {@link Flag}s.
     */
    boolean hasFlags(@NotNull Flag... flags);

    /**
     * Removes all provided {@link Flag}s from this view.
     *
     * @param flags All {@link Flag}s, which should be removed.
     */
    void removeFlags(@NotNull Flag... flags);

    /**
     * Sets the interaction on a specific raw slot.
     * The raw slot supports slots of the top- and the bottom-inventory.
     *
     * @param rawSlot     The slot you want to set.
     * @param interaction The interaction, which gets executed by clicking the given rawSlot.
     */
    void setInteraction(int rawSlot, @NotNull Interaction interaction);

    /**
     * Sets the {@link ItemStack} on a specific raw slot.
     * The raw slot supports slots of the top- and the bottom-inventory.
     *
     * @param rawSlot   The slot you want to set.
     * @param itemStack The {@link ItemStack}, which gets set in the given slot.
     */
    void setItemStack(int rawSlot, @NotNull ItemStack itemStack);

    /**
     * Returns the {@link ItemStack}, which is set in the given raw slot.
     * The raw slot supports slots of the top- and the bottom-inventory.
     *
     * @param rawSlot The slot you want to get.
     * @return {@link ItemStack}, in the provided raw slot.
     */
    @NotNull ItemStack getItemStack(int rawSlot);

    /**
     * Returns the {@link Interaction}, which is set in the given raw slot.
     * The raw slot supports slots of the top- and the bottom-inventory.
     *
     * @param rawSlot The slot you want to get.
     * @return {@link Interaction}, in the provided raw slot.
     */
    @NotNull Interaction getInteraction(int rawSlot);

    /**
     * Executes the abstract method of every {@link ViewComponent#onRebuild(PlayerView, Player)}
     * in the component tree / hierarchy.
     * <p>
     * This method gets also called by the following actions and conditions:
     * Everytime after...
     * - ...the {@link PlayerView} was opened.
     * - ...an {@link AnvilInputInteraction} gets called.
     * - ...{@link ViewPagination#update(PlayerView)} gets called.
     * - ...{@link ViewPagination#toPage(PlayerView, int)} gets called.
     * - ...the {@link Player} types anything in the anvil text input field.
     */
    void executeRebuild();

    /**
     * This method allows to change title of the currently
     * open top-inventory by a given {@link Component}.
     * @param component The new title as {@link Component}.
     */
    void setTitle(@NotNull Component component);

    /**
     * This method allows to change title of the currently
     * open top-inventory by a given {@link String} as {@link MiniMessage} text.
     * @param miniMessage The new title as {@link MiniMessage} text.
     */
    void setTitle(@NotNull String miniMessage);
}
