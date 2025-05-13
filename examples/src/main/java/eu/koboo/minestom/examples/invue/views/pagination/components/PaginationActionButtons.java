package eu.koboo.minestom.examples.invue.views.pagination.components;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.component.ViewComponent;
import eu.koboo.minestom.invue.api.interaction.Interactions;
import eu.koboo.minestom.invue.api.item.ViewItem;
import eu.koboo.minestom.invue.api.pagination.ViewPagination;
import eu.koboo.minestom.invue.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaginationActionButtons extends ViewComponent {

    ViewPagination pagination;
    ViewPattern pattern;

    public PaginationActionButtons(ViewPagination pagination, ViewPattern pattern) {
        this.pagination = pagination;
        this.pattern = pattern;
        addChild(pagination);
    }

    @Override
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        // Sets the "previous" and "next" page buttons, based on the current page.
        // That's why these items are set in "onStateUpdate" instead of "onOpen".

        String nextName = "<green>Next (" + pagination.getCurrentPage() + ")";
        if (!pagination.hasNextPage()) {
            nextName = "<red> No next page";
        }
        ViewItem.bySlot(view, pattern.getSlot('>'))
            .material(Material.ARROW)
            .name(nextName)
            .interaction(Interactions.toNextPage(pagination));

        String previousName = "<green>Previous (" + pagination.getCurrentPage() + ")";
        if (!pagination.hasPreviousPage()) {
            previousName = "<red>No previous page";
        }
        ViewItem.bySlot(view, pattern.getSlot('<'))
            .material(Material.ARROW)
            .name(previousName)
            .interaction(Interactions.toPreviousPage(pagination));
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Add close and refresh buttons to the top inventory.
        // Since these items don't update on state changes (e.g. "next page navigation"),
        // we can add them in "onOpen" instead of "onStateUpdate".

        ViewItem.bySlot(view, pattern.getSlot('K'))
            .material(Material.REDSTONE)
            .name("Close")
            .closeInventoryInteraction();

        ViewItem.bySlot(view, pattern.getSlot('Z'))
            .material(Material.NETHER_STAR)
            .name("Refresh")
            .interaction(Interactions.reloadPagination(pagination));
    }
}
