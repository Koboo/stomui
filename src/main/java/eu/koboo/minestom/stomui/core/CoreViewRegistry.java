package eu.koboo.minestom.stomui.core;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.component.ViewComponent;
import eu.koboo.minestom.stomui.api.pagination.ItemRenderer;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import eu.koboo.minestom.stomui.core.listener.ViewInventoryCloseListener;
import eu.koboo.minestom.stomui.core.listener.ViewInventoryPreClickListener;
import eu.koboo.minestom.stomui.core.listener.ViewPlayerDisconnectListener;
import eu.koboo.minestom.stomui.core.listener.ViewPlayerPacketNameItemListener;
import eu.koboo.minestom.stomui.core.pagination.PageComponent;
import eu.koboo.minestom.stomui.core.pagination.ScrollComponent;
import eu.koboo.minestom.stomui.core.slots.CoreViewPattern;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CoreViewRegistry implements ViewRegistry {

    List<EventListener<?>> eventListeners;
    Map<Player, CorePlayerView> playerViewRegistry;
    Map<UUID, List<CorePlayerView>> playerViewHistoryRegistry;

    CoreViewRegistry() {
        this.eventListeners = new ArrayList<>();
        this.playerViewRegistry = new ConcurrentHashMap<>();
        this.playerViewHistoryRegistry = new ConcurrentHashMap<>();
    }

    @Override
    public void enable() {
        registerListener(PlayerPacketEvent.class, new ViewPlayerPacketNameItemListener(this));
        registerListener(InventoryPreClickEvent.class, new ViewInventoryPreClickListener(this));
        registerListener(InventoryCloseEvent.class, new ViewInventoryCloseListener(this));
        registerListener(PlayerDisconnectEvent.class, new ViewPlayerDisconnectListener(this));
    }

    @Override
    public void disable() {
        for (Player player : playerViewRegistry.keySet()) {
            player.closeInventory();
        }
        playerViewRegistry.clear();
        playerViewHistoryRegistry.clear();
        for (EventListener<?> eventListener : eventListeners) {
            MinecraftServer.getGlobalEventHandler().removeListener(eventListener);
        }
        eventListeners.clear();
    }

    private <T extends Event> void registerListener(Class<T> eventClass, Consumer<T> consumer) {
        EventListener<T> eventListener = EventListener.of(eventClass, consumer);
        eventListeners.add(eventListener);
        MinecraftServer.getGlobalEventHandler().addListener(eventListener);
    }

    // Only called by PlayerView#closeView()
    @ApiStatus.Internal
    public void unregisterPlayerView(@NotNull Player player) {
        PlayerView playerView = playerViewRegistry.get(player);
        if (playerView == null) {
            return;
        }
        playerViewRegistry.remove(player);
    }

    @Override
    public @NotNull PlayerView open(@NotNull Player player, @NotNull ViewBuilder viewBuilder) {
        ViewBuilder newViewBuilder = ViewBuilder.copyOf(viewBuilder);
        newViewBuilder.validate();
        executeComponents(
            newViewBuilder.getProvider(),
            component -> component.modifyBuilder(newViewBuilder, player)
        );

        CorePlayerView playerView = new CorePlayerView(this, player, newViewBuilder);
        openViewInternal(playerView, true, false);
        return playerView;
    }

    @Override
    public @NotNull PlayerView open(@NotNull PlayerView playerView) {
        openViewInternal(
            (CorePlayerView) playerView,
            false,
            false
        );
        return playerView;
    }

    public void openViewInternal(@NotNull CorePlayerView playerView,
                                 boolean callComponentOpen, boolean excludeHistory) {
        Player player = playerView.getPlayer();
        CorePlayerView previousView = getCurrentView(player);
        if (previousView != null) {
            // Don't reopen the same view again
            if (playerView.getId().equals(previousView.getId())) {
                throw new IllegalStateException("Player has already opened this view.");
            }
            previousView.closeView();
        }
        if (!excludeHistory) {
            List<CorePlayerView> viewHistory = getHistory(player);
            viewHistory.add(playerView);
        }

        playerViewRegistry.put(player, playerView);
        playerView.openView(callComponentOpen);
    }

    @Override
    public @Nullable CorePlayerView getCurrentView(@NotNull Player player) {
        if (!player.isOnline()) {
            return null;
        }
        return playerViewRegistry.get(player);
    }

    @Override
    public @NotNull Collection<PlayerView> getAllViews() {
        return List.copyOf(playerViewRegistry.values());
    }

    @Override
    public @Nullable PlayerView getLastView(@NotNull Player player) {
        if (!player.isOnline()) {
            throw new IllegalArgumentException("Player is not online");
        }
        List<CorePlayerView> history = playerViewHistoryRegistry.get(player.getUuid());
        if (history == null || history.isEmpty()) {
            return null;
        }
        CorePlayerView playerView = getCurrentView(player);
        if (playerView == null) {
            return null;
        }
        int currentIndex = history.indexOf(playerView);
        if (currentIndex == -1) {
            return null;
        }
        int index = currentIndex - 1;
        if (index < 0) {
            return null;
        }
        return history.get(index);
    }

    @Override
    public @Nullable PlayerView getNextView(@NotNull Player player) {
        if (!player.isOnline()) {
            throw new IllegalArgumentException("Player is not online");
        }
        List<CorePlayerView> history = playerViewHistoryRegistry.get(player.getUuid());
        if (history == null || history.isEmpty()) {
            return null;
        }
        CorePlayerView playerView = getCurrentView(player);
        if (playerView == null) {
            return null;
        }
        int currentIndex = history.indexOf(playerView);
        if (currentIndex == -1) {
            return null;
        }
        int index = currentIndex + 1;
        if (index >= history.size()) {
            return null;
        }
        return history.get(index);
    }

    @ApiStatus.Internal
    public void resetPlayer(Player player) {
        playerViewRegistry.remove(player);
        List<CorePlayerView> history = playerViewHistoryRegistry.remove(player.getUuid());
        if (history != null) {
            history.clear();
        }
    }

    @ApiStatus.Internal
    public @NotNull List<CorePlayerView> getHistory(@NotNull Player player) {
        return playerViewHistoryRegistry.computeIfAbsent(player.getUuid(), k -> new ArrayList<>());
    }

    @Override
    public @NotNull <T> ViewPagination<T> pageable(@NotNull ItemRenderer<T> itemRenderer,
                                                   @Nullable Comparator<T> itemSorter,
                                                   @Nullable ItemStack fillerItem,
                                                   @NotNull List<Integer> slotList) {
        return new PageComponent<>(itemRenderer, itemSorter, fillerItem, slotList);
    }

    @Override
    public @NotNull <T> ViewPagination<T> scrollable(@NotNull ItemRenderer<T> itemRenderer,
                                                     @Nullable Comparator<T> itemSorter,
                                                     @Nullable ItemStack fillerItem,
                                                     @NotNull List<List<Integer>> listOfSlotLists) {
        return new ScrollComponent<>(itemRenderer, itemSorter, fillerItem, listOfSlotLists);
    }

    @Override
    public ViewPattern pattern(@NotNull Collection<String> pattern) {
        return new CoreViewPattern(pattern);
    }

    @Override
    public void executeComponents(ViewComponent component, Consumer<ViewComponent> function) {
        function.accept(component);
        if (component.getChildren().isEmpty()) {
            return;
        }
        for (int i = 0; i < component.getChildren().size(); i++) {
            ViewComponent child = component.getChildren().get(i);
            executeComponents(child, function);
        }
    }
}
