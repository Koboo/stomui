package eu.koboo.minestom.examples.stomui.views.search;

import eu.koboo.minestom.examples.stomui.views.pagination.renderer.MaterialItemRenderer;
import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.interaction.AnvilInputInteraction;
import eu.koboo.minestom.stomui.api.interaction.Interactions;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;

@Getter
public class SearchExampleProvider extends ViewProvider implements AnvilInputInteraction {

    private static final Collection<Material> ALL_MATERIALS = Material.values();
    private static final ViewType VIEW_TYPE = ViewType.ANVIL;

    ViewPattern pattern;
    SearchItemFilter filter;
    ViewPagination<Material> pagination;

    public SearchExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(VIEW_TYPE)
            .title("Search:"));
        pattern = registry.pattern(
            "#########",
            "#########",
            "#########",
            "A <   >  "
        );
        // This method allows offsetting the slots of the top inventory,
        // so the pattern applies to the bottom inventory.
        // You could also offset the pattern by a specific slot amount,
        // but this is more convenient and fail-safe.
        pattern.offsetTopInventory(VIEW_TYPE);

        pagination = registry.pageable(
            new MaterialItemRenderer(),
            ItemStack.AIR,
            pattern.getSlots('#')
        );
        pagination.setItemSorter(Comparator.comparing(Material::id));
        filter = new SearchItemFilter();
        pagination.setItemFilter(filter);
        pagination.addItems(ALL_MATERIALS);
        addChild(pagination);
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        view.getBottomInventory().clear();

        ViewItem.bySlot(view, 0)
            .material(Material.PAPER)
            .name("")
            .cancelClicking();

        for (Integer slot : pattern.getSlots(' ')) {
            ViewItem.bySlot(view, slot)
                .material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .cancelClicking();
        }
    }

    @Override
    public void onClose(@NotNull PlayerView view, @NotNull Player player) {
        view.getBottomInventory().clear();
    }

    @Override
    public void onRebuild(@NotNull PlayerView view, @NotNull Player player) {
        String nextName = "<green>Next (" + pagination.getNextPage() + ")";
        if (!pagination.hasNextPage()) {
            nextName = "<red> No next page";
        }
        ViewItem.bySlot(view, pattern.getSlot('>'))
            .material(Material.ARROW)
            .name(nextName)
            .glint(pagination.hasNextPage())
            .interaction(Interactions.toNextPage(pagination));

        String previousName = "<green>Previous (" + pagination.getPreviousPage() + ")";
        if (!pagination.hasPreviousPage()) {
            previousName = "<red>No previous page";
        }
        ViewItem.bySlot(view, pattern.getSlot('<'))
            .material(Material.ARROW)
            .name(previousName)
            .glint(pagination.hasPreviousPage())
            .interaction(Interactions.toPreviousPage(pagination));

        ViewItem.bySlot(view, pattern.getSlot('A'))
            .material(Material.GOLD_NUGGET)
            .name("<gray>Amount: <gold>" + pagination.getTotalItems())
            .cancelClicking();
    }

    @Override
    public void onAnvilInput(@NotNull PlayerView playerView, @NotNull Player player, @NotNull String searchInput) {
        player.sendMessage("Received input: " + searchInput);
        filter.setTextInput(searchInput);
        pagination.refreshPage(playerView);
    }
}
