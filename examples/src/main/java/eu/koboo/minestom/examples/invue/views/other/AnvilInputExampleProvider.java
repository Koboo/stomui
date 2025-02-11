package eu.koboo.minestom.examples.invue.views.other;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.component.ViewProvider;
import eu.koboo.minestom.invue.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.invue.api.item.ViewItem;
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
        // Anvil only allows slots from 0-2
        // But with every input the client predicts changes and the item in slot "2" starts to flicker.
        // We somewhat fixed that, but it's still flickering.
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .name("Close")
            .closeInventoryInteraction();
    }

    @Override
    public void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input) {
        // The player enter or removed a character from the anvil input field.
        // This method is implemented by the AnvilInputInteraction interface
        player.sendMessage("Received input: " + input);
    }
}
