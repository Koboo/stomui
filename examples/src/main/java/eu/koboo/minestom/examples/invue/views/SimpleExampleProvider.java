package eu.koboo.minestom.examples.invue.views;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.Priority;
import eu.koboo.minestom.invue.api.component.ViewProvider;
import eu.koboo.minestom.invue.api.item.ViewItem;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleExampleProvider extends ViewProvider {

    public SimpleExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        // Modify the ViewBuilder, based on the player who will open the modified
        // ViewBuilder. This ViewBuilder is copy of the ViewBuilder from the constructor.
        viewBuilder.title("Welcome, " + player.getUsername());
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Gets executed if the player opens the inventory.

        // Getting all slots of top inventory of the provided ViewType.
        List<Integer> allSlotsOfTopInventory = view.getType().getTopSlots();

        // Getting all view items for all slots of the top inventory.
        List<ViewItem> allItemsOfTopInventory = ViewItem.bySlotList(view, allSlotsOfTopInventory);

        // Setting all items to gray stained-glass panes and empty their names.
        for (ViewItem viewItem : allItemsOfTopInventory) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .cancelClicking();
        }

        // Creating a simple close item at the first slot of the top inventory
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();
    }

    @Override
    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        // Gets executed if the player closes or swaps the inventory.
        player.sendMessage("You've closed the view");
    }

    @Override
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        // Gets called everytime view.updateState() is called.
    }

    @Override
    public @NotNull Priority getPriority() {
        // Can be used to define rendering order of children within a parent component
        return Priority.LOW;
    }
}
