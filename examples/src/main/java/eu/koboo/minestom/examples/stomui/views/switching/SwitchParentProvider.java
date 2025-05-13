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
public abstract class SwitchParentProvider extends ViewProvider {

    String title;

    public SwitchParentProvider(ViewRegistry registry, String title) {
        super(registry, ViewType.SIZE_5_X_9);
        this.title = title;
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
        SwitchParentProvider oppositeViewProvider = oppositeViewProvider();
        ViewItem.byRowColumn(view, 1, 4)
            .material(Material.BONE)
            .displayName("Back to \"" + oppositeViewProvider.getTitle() + "\" inventory")
            .interaction(Interactions.backToLastView());
        ViewItem.byRowColumn(view, 3, 4)
            .material(Material.ARROW)
            .displayName("Open new \"" + oppositeViewProvider.getTitle() + "\" inventory")
            .interaction(Interactions.open(oppositeViewProvider));
    }

    @Override
    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        player.sendMessage(">> \"" + title + "\" closed " + view.getId());
    }

    public abstract SwitchParentProvider oppositeViewProvider();
}
