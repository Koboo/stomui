package eu.koboo.minestom.examples.views;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.ViewBuilder;
import eu.koboo.minestom.api.ViewRegistry;
import eu.koboo.minestom.api.ViewType;
import eu.koboo.minestom.api.component.RootViewComponent;
import eu.koboo.minestom.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.api.item.ViewItem;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

@Getter
public class AnvilInputExampleProvider extends RootViewComponent implements AnvilInputInteraction {

    public AnvilInputExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.ANVIL)
            .title("Simple anvil input example"));
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        ViewItem.bySlot(view, 0)
            .material(Material.REDSTONE)
            .name("Close")
            .closeInventoryInteraction();
    }

    @Override
    public void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String input) {
        player.sendMessage("Received input: " + input);

    }
}
