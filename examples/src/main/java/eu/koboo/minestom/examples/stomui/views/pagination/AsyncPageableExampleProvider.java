package eu.koboo.minestom.examples.stomui.views.pagination;

import eu.koboo.minestom.examples.stomui.views.pagination.components.PaginationBorder;
import eu.koboo.minestom.examples.stomui.views.pagination.renderer.MaterialItemRenderer;
import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.interaction.Interactions;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AsyncPageableExampleProvider extends ViewProvider {

    ViewPattern pattern;
    ViewPagination<Material> pagination;

    public AsyncPageableExampleProvider(ViewRegistry registry) {
        super(registry, ViewType.SIZE_6_X_9);
        pattern = registry.pattern(
            "K#######Z",
            "#1111111#",
            "#1111111#",
            "#1111111#",
            "#1111111#",
            "##<###>##"
        );
        pagination = registry.pageable(
            new MaterialItemRenderer(),
            ItemStack.AIR,
            pattern.getMergedSlots('1')
        );
        pagination.setItemSorter(Comparator.comparing(Material::id));
        addChild(new PaginationBorder(pagination, pattern));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title("<red>Async pageable");
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {

        List<Integer> topSlots = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, topSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ");
        }

        // Add close and refresh buttons to the top inventory.
        // Since these items don't update on rebuilds (e.g. "next page navigation"),
        // we can add them in "onOpen" instead of "onRebuild".
        ViewItem.bySlot(view, pattern.getSlot('K'))
            .material(Material.REDSTONE)
            .name("Close")
            .closeInventoryInteraction();

        ViewItem.bySlot(view, pattern.getSlot('Z'))
            .material(Material.NETHER_STAR)
            .name("Refresh")
            .interaction(Interactions.updatePagination(pagination));

        // Now we can execute the async loading method
        // and await the completion of the future.
        // In the completion we can add the items to the pagination and
        // update the pagination on the given PlayerView.
        CompletableFuture<Collection<Material>> future = loadSomethingAsync();
        future.whenComplete((list, exception) -> {
            // Exception caught. Something went wrong. Notify your players about that.
            if (exception != null) {
                player.sendMessage("An error occurred while loading async pagination: " + exception.getMessage());
                return;
            }
            // We got our needed results. Now update the pagination, please!
            if (list != null) {
                pagination.addItems(list);
                view.executeRebuild();
            }
        });
    }

    public CompletableFuture<Collection<Material>> loadSomethingAsync() {
        // Create something running async...
        CompletableFuture<Collection<Material>> future = new CompletableFuture<>();
        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            future.complete(Material.values());
        });
        return future;
    }

    @Override
    public void onRebuild(@NotNull PlayerView view, @NotNull Player player) {
        // Sets the "previous" and "next" page buttons, based on the current page.
        // That's why these items are set in "onStateUpdate" instead of "onOpen".

        String nextName = "<green>Next (" + pagination.getNextPage() + ")";
        if (!pagination.hasNextPage()) {
            nextName = "<red> No next page";
        }
        ViewItem.bySlot(view, pattern.getSlot('>'))
            .material(Material.ARROW)
            .name(nextName)
            .interaction(Interactions.toNextPage(pagination));

        String previousName = "<green>Previous (" + pagination.getPreviousPage() + ")";
        if (!pagination.hasPreviousPage()) {
            previousName = "<red>No previous page";
        }
        ViewItem.bySlot(view, pattern.getSlot('<'))
            .material(Material.ARROW)
            .name(previousName)
            .interaction(Interactions.toPreviousPage(pagination));
    }
}
