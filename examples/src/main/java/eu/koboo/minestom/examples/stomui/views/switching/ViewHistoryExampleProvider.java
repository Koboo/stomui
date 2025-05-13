package eu.koboo.minestom.examples.stomui.views.switching;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.interaction.Interactions;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ViewHistoryExampleProvider extends ViewProvider {

    int layerDepth;
    String title;

    public ViewHistoryExampleProvider(ViewRegistry registry, int layerDepth) {
        super(registry, ViewBuilder.of(ViewType.SIZE_5_X_9));
        this.layerDepth = layerDepth;
        this.title = "History: " + layerDepth;
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title(title);
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        player.sendMessage(">> \"" + title + "\" opened " + view.getId());
        for (ViewItem viewItem : ViewItem.bySlotList(view, view.getType().getTopSlots())) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ");
        }
        int nextLayerDepth = layerDepth + 1;
        int previousLayerDepth = layerDepth - 1;

        ViewItem.byRowColumn(view, 1, 3)
            .material(Material.BONE)
            .displayName("Back depth " + previousLayerDepth)
            .interaction(Interactions.backToLastView());
        ViewItem.byRowColumn(view, 1, 5)
            .material(Material.BONE)
            .displayName("Forward depth " + nextLayerDepth)
            .interaction(Interactions.forwardToNextView());
        ViewItem.byRowColumn(view, 3, 4)
            .material(Material.ARROW)
            .displayName("New depth " + nextLayerDepth)
            .interaction(Interactions.open(new ViewHistoryExampleProvider(registry, nextLayerDepth)));
    }

    @Override
    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        player.sendMessage(">> \"" + title + "\" closed " + view.getId());
    }

    @Override
    public String toString() {
        return "MultiLayer{depth=" + layerDepth + "}";
    }
}
