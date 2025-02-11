package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.component.ViewProvider;
import eu.koboo.minestom.invue.api.pagination.ViewPagination;
import eu.koboo.minestom.invue.core.CorePlayerView;
import eu.koboo.minestom.invue.core.CoreViewRegistry;
import lombok.experimental.UtilityClass;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class provides several default {@link Interaction}s.
 * For now, you need to check out their implementation, if you want to know what they do.
 * Documentation will follow.
 */
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

    public Interaction close(@Nullable Consumer<Player> afterClosing) {
        return action -> {
            action.getPlayer().closeInventory();
            action.getEvent().setCancelled(true);
            if (afterClosing == null) {
                return;
            }
            afterClosing.accept(action.getPlayer());
        };
    }

    public Interaction open(@NotNull ViewProvider newProviderToOpen) {
        return open(newProviderToOpen.getBuilder(), null);
    }

    public Interaction open(@NotNull ViewBuilder newBuilderToOpen) {
        return open(newBuilderToOpen, null);
    }

    public Interaction open(@NotNull ViewBuilder newBuilderToOpen,
                            @Nullable Consumer<Player> afterOpening) {
        return action -> {
            Player player = action.getPlayer();
            action.getRegistry().open(player, newBuilderToOpen);
            if (afterOpening != null) {
                afterOpening.accept(player);
            }
        };
    }

    public Interaction forwardToNextView() {
        return action -> {
            Player player = action.getPlayer();
            CoreViewRegistry registry = (CoreViewRegistry) action.getRegistry();
            CorePlayerView nextView = (CorePlayerView) registry.getNextView(player);
            if (nextView == null) {
                return;
            }
            registry.openViewInternal(nextView, false, true);
        };
    }

    public Interaction backToLastView() {
        return action -> {
            Player player = action.getPlayer();
            CoreViewRegistry registry = (CoreViewRegistry) action.getRegistry();
            CorePlayerView lastView = (CorePlayerView) registry.getLastView(player);
            if (lastView == null) {
                return;
            }
            registry.openViewInternal(lastView, false, true);
        };
    }

    public Interaction reloadPagination(@NotNull ViewPagination pagination) {
        return reloadPagination(pagination, null);
    }

    public Interaction reloadPagination(@NotNull ViewPagination pagination,
                                        @Nullable Consumer<Player> afterRefreshing) {
        return action -> {
            action.getEvent().setCancelled(true);
            pagination.reloadItems(action.getView());
            if (afterRefreshing == null) {
                return;
            }
            afterRefreshing.accept(action.getPlayer());
        };
    }

    public Interaction toPage(@NotNull ViewPagination pagination,
                              @NotNull Integer newPage) {
        return toPage(pagination, newPage, null);
    }

    public Interaction toPage(@NotNull ViewPagination pagination,
                              @NotNull Integer newPage,
                              @Nullable Consumer<Player> afterNavigating) {
        return action -> {
            action.getEvent().setCancelled(true);
            pagination.toPage(action.getView(), newPage);
            if (afterNavigating == null) {
                return;
            }
            afterNavigating.accept(action.getPlayer());
        };
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination) {
        return toNextPage(pagination, null, null);
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination,
                                  @Nullable Consumer<Player> noNavigation) {
        return toNextPage(pagination, null, noNavigation);
    }

    public Interaction toNextPage(@NotNull ViewPagination pagination,
                                  @Nullable Consumer<Player> afterNavigating,
                                  @Nullable Consumer<Player> noNavigation) {
        return action -> {
            action.getEvent().setCancelled(true);
            if (!pagination.hasNextPage()) {
                if (noNavigation == null) {
                    return;
                }
                noNavigation.accept(action.getPlayer());
                return;
            }
            pagination.toNextPage(action.getView());
            if (afterNavigating == null) {
                return;
            }
            afterNavigating.accept(action.getPlayer());
        };
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination) {
        return toPreviousPage(pagination, null, null);
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination,
                                      @Nullable Consumer<Player> noNavigation) {
        return toPreviousPage(pagination, null, noNavigation);
    }

    public Interaction toPreviousPage(@NotNull ViewPagination pagination,
                                      @Nullable Consumer<Player> afterNavigating,
                                      @Nullable Consumer<Player> noNavigation) {
        return action -> {
            action.getEvent().setCancelled(true);
            if (!pagination.hasPreviousPage()) {
                if (noNavigation == null) {
                    return;
                }
                noNavigation.accept(action.getPlayer());
                return;
            }
            pagination.toPreviousPage(action.getView());
            if (afterNavigating == null) {
                return;
            }
            afterNavigating.accept(action.getPlayer());
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
