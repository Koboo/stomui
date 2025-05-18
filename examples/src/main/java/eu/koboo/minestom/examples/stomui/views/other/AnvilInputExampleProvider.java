package eu.koboo.minestom.examples.stomui.views.other;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

@Getter
public class AnvilInputExampleProvider extends ViewProvider implements AnvilInputInteraction {

    public AnvilInputExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.ANVIL)
            .title("Simple anvil input example"));
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // An inventory of type ANVIL only allows slots to range from 0 to 2.
        // With every text input, the client predicts the result
        // and that results in some items flickering.
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .name("Close")
            .closeInventoryInteraction();
    }

    @Override
    public void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input) {
        // The player entered or removed a character from the anvil text input field.
        // This method is provided by the AnvilInputInteraction interface
        player.sendMessage("Received input: " + input);
    }
}
