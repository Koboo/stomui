package eu.koboo.minestom.api.interaction;

import eu.koboo.minestom.api.ViewBuilder;
import eu.koboo.minestom.api.component.RootViewComponent;
import eu.koboo.minestom.api.pagination.ViewPagination;
import eu.koboo.minestom.core.SimplePlayerView;
import eu.koboo.minestom.core.SimpleViewRegistry;
import lombok.experimental.UtilityClass;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class Interactions {

    public Interaction cancel() {
        return action -> action.getEvent().setCancelled(true);
    }

    public Interaction allow() {
        return action -> action.getEvent().setCancelled(false);
    }

    public Interaction close() {
        return close(null);
    }

    public Interaction close(@Nullable Consumer<Player> afterClose) {
        return action -> {
            action.getPlayer().closeInventory();
            action.getEvent().setCancelled(true);
            if (afterClose == null) {
                return;
            }
            afterClose.accept(action.getPlayer());
        };
    }

    public Interaction open(@NotNull RootViewComponent newOpener) {
        return open(newOpener.getBuilder(), null);
    }

    public Interaction open(@NotNull ViewBuilder newBuilderToOpen) {
        return open(newBuilderToOpen, null);
    }

    public Interaction open(@NotNull ViewBuilder newBuilderToOpen,
                            @Nullable Consumer<Player> afterOpen) {
        return action -> {
            Player player = action.getPlayer();
            action.getRegistry().open(player, newBuilderToOpen);
            if (afterOpen != null) {
                afterOpen.accept(player);
            }
        };
    }

    public Interaction forwardToNextView() {
        return action -> {
            Player player = action.getPlayer();
            SimpleViewRegistry registry = (SimpleViewRegistry) action.getRegistry();
            SimplePlayerView nextView = (SimplePlayerView) registry.getNextView(player);
            if (nextView == null) {
                return;
            }
            registry.openViewInternal(player, nextView, false, true);
        };
    }

    public Interaction backToLastView() {
        return action -> {
            Player player = action.getPlayer();
            SimpleViewRegistry registry = (SimpleViewRegistry) action.getRegistry();
            SimplePlayerView lastView = (SimplePlayerView) registry.getLastView(player);
            if (lastView == null) {
                return;
            }
            registry.openViewInternal(player, lastView, false, true);
        };
    }

    public Interaction reloadPagination(@NotNull ViewPagination pagination) {
        return reloadPagination(pagination, null);
    }

    public Interaction reloadPagination(@NotNull ViewPagination pagination,
                                        @Nullable Consumer<Player> afterRefresh) {
        return action -> {
            action.getEvent().setCancelled(true);
            pagination.reloadItems(action.getView());
            if (afterRefresh == null) {
                return;
            }
            afterRefresh.accept(action.getPlayer());
        };
    }

    public Interaction toPage(@NotNull ViewPagination pagination,
                              @NotNull Integer newPage) {
        return toPage(pagination, newPage, null);
    }

    public Interaction toPage(@NotNull ViewPagination pagination,
                              @NotNull Integer newPage,
                              @Nullable Consumer<Player> afterRender) {
        return action -> {
            action.getEvent().setCancelled(true);
            pagination.toPage(action.getView(), newPage);
            if (afterRender == null) {
                return;
            }
            afterRender.accept(action.getPlayer());
        };
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination) {
        return toNextPage(pagination, null, null);
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination,
                                  @Nullable Consumer<Player> noNextPage) {
        return toNextPage(pagination, null, noNextPage);
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination,
                                  @Nullable Consumer<Player> afterRenderNextPage,
                                  @Nullable Consumer<Player> noNextPage) {
        return action -> {
            action.getEvent().setCancelled(true);
            if (!pagination.hasNextPage()) {
                if (noNextPage == null) {
                    return;
                }
                noNextPage.accept(action.getPlayer());
                return;
            }
            pagination.toNextPage(action.getView());
            if (afterRenderNextPage == null) {
                return;
            }
            afterRenderNextPage.accept(action.getPlayer());
        };
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination) {
        return toPreviousPage(pagination, null, null);
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination,
                                      @Nullable Consumer<Player> noPreviousPage) {
        return toPreviousPage(pagination, null, noPreviousPage);
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination,
                                      @Nullable Consumer<Player> afterRenderPreviousPage,
                                      @Nullable Consumer<Player> noPreviousPage) {
        return action -> {
            action.getEvent().setCancelled(true);
            if (!pagination.hasPreviousPage()) {
                if (noPreviousPage == null) {
                    return;
                }
                noPreviousPage.accept(action.getPlayer());
                return;
            }
            pagination.toPreviousPage(action.getView());
            if (afterRenderPreviousPage == null) {
                return;
            }
            afterRenderPreviousPage.accept(action.getPlayer());
        };
    }

    public Interaction merge(@NotNull Interaction... interactions) {
        return merge(List.of(interactions));
    }

    public Interaction merge(@NotNull Collection<Interaction> interactionCollection) {
        return action -> {
            for (Interaction interaction : interactionCollection) {
                interaction.interact(action);
            }
        };
    }
}
