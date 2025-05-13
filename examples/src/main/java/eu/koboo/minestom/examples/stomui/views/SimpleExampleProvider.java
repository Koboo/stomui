package eu.koboo.minestom.examples.stomui.views;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.Priority;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleExampleProvider extends ViewProvider {

    public SimpleExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9));

        // Open by calling:
        // SimpleExampleProvider exampleProvider = new SimpleExampleProvider(registry);
        // exampleProvider.open(player);
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        // Modify the ViewBuilder, based on the player who will open the inventory.
        // The instance of the ViewBuilder parameter is a copy of the ViewBuilder from the constructor,
        // because every place gets his own instance of the ViewBuilder.
        viewBuilder.title("Welcome, " + player.getUsername());
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Gets executed before the player opens the inventory.

        // Getting all slot numbers from the top inventory of the provided ViewType.
        List<Integer> allSlotsOfTopInventory = view.getType().getTopSlots();

        // Getting all view items for all slots of the top inventory.
        // (Makes it easier to change material and names, using the ViewItem api)
        List<ViewItem> allItemsOfTopInventory = ViewItem.bySlotList(view, allSlotsOfTopInventory);

        // Setting all ItemStacks to material gray stained-glass panes with empty names
        // and disable any clicking on them.
        for (ViewItem viewItem : allItemsOfTopInventory) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .cancelClicking();
        }

        // Creating a simple close item in the first slot of the top inventory
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();
    }

    @Override
    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        // Gets executed if the player closes or swaps the inventory.
        // Other events that could cause this method to be executed are:
        // - PlayerDisconnectEvent
        // - InventoryCloseEvent
        // - Player gets opened a new PlayerView
        player.sendMessage("You've closed the view");
    }

    @Override
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        // Gets called everytime view.updateState() is called.
    }

    @Override
    public @NotNull Priority getPriority() {
        // Can be used to define the rendering order of children within a parent component
        return Priority.LOW;
    }
}
