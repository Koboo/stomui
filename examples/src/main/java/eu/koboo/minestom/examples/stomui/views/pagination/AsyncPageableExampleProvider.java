package eu.koboo.minestom.examples.stomui.views.pagination;

import eu.koboo.minestom.examples.stomui.views.pagination.components.PaginationActionButtons;
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

    ViewPagination<Material> pagination;

    public AsyncPageableExampleProvider(ViewRegistry registry) {
        super(registry, ViewType.SIZE_6_X_9);
        addChild(new PaginationBorder());
        ViewPattern pattern = registry.pattern(
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
        addChild(pagination);
        addChild(new PaginationActionButtons(pagination, pattern));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title("<red>Async pageable");
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Now we can execute the async loading method
        // and await the completion of the future.
        // In the completion we can add the items to the pagination and
        // update the pagination on the given PlayerView.
        player.sendMessage("Loading async items..");
        CompletableFuture<Collection<Material>> future = loadSomethingAsync();
        future.whenComplete((list, exception) -> {
            player.sendMessage("Finished future!");

            // Exception caught. Something went wrong. Notify your players about that.
            if (exception != null) {
                player.sendMessage("An error occurred while loading async pagination: " + exception.getMessage());
                return;
            }

            // We got our needed results. Now update the pagination, please!
            if (list != null) {
                player.sendMessage("Found items!");
                pagination.addItems(list);
                pagination.refreshPage(view);
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
}
