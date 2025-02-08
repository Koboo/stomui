package eu.koboo.minestom.core;

import eu.koboo.minestom.api.PlayerView;
import eu.koboo.minestom.api.ViewBuilder;
import eu.koboo.minestom.api.ViewType;
import eu.koboo.minestom.api.component.RootViewComponent;
import eu.koboo.minestom.api.flags.Flag;
import eu.koboo.minestom.api.flags.Flags;
import eu.koboo.minestom.api.interaction.Interaction;
import eu.koboo.minestom.api.interaction.Interactions;
import eu.koboo.minestom.api.slots.BottomSlotUtility;
import eu.koboo.minestom.api.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiStatus.Internal
public final class CorePlayerView implements PlayerView {

    final CoreViewRegistry registry;
    final String id;
    final ViewType type;

    final Player player;
    final Inventory topInventory;
    final PlayerInventory bottomInventory;

    final RootViewComponent rootComponent;
    final Map<Integer, Interaction> interactions;
    final Set<Flag> addedFlags;
    final Set<ClickType> disabledClickTypes;

    final long clickCooldown;
    final long slotClickCooldown;

    final Map<Integer, Long> slotClickCooldownMap;
    long clickCooldownUntil;

    public CorePlayerView(@NotNull CoreViewRegistry registry,
                          @NotNull Player player,
                          @NotNull ViewBuilder builder) {
        this.registry = registry;
        this.id = IdGenerator.generateRandomString(10);
        this.type = builder.getType();

        this.player = player;
        this.topInventory = new Inventory(builder.getType().getInventoryType(), builder.getTitle());
        this.bottomInventory = player.getInventory();

        this.rootComponent = builder.getRootComponent();

        this.interactions = new HashMap<>();

        this.addedFlags = new HashSet<>();
        this.addedFlags.addAll(builder.getFlags());

        this.disabledClickTypes = new HashSet<>();
        this.disabledClickTypes.addAll(builder.getDisabledClickTypes());

        this.clickCooldown = builder.getClickCooldownInMillis();
        this.slotClickCooldown = builder.getSlotClickCooldownInMillis();

        this.slotClickCooldownMap = new HashMap<>();
        this.clickCooldownUntil = Instant.now().toEpochMilli();

        addFlags(Flags.CONVERT_CURSOR_TO_OUTSIDE_INTERACTION);
    }

    @ApiStatus.Internal
    public void openView(boolean callComponentOpen) {
        if (callComponentOpen) {
            registry.executeComponents(
                rootComponent,
                component -> component.onOpen(this, player)
            );
        }
        player.openInventory(topInventory);
        updateState();
    }

    @ApiStatus.Internal
    public void closeView() {
        registry.executeComponents(
            rootComponent,
            component -> component.onClose(this, player)
        );
        registry.unregisterPlayerView(player);
    }

    @ApiStatus.Internal
    public boolean hasCooldown(int slot) {
        Instant now = Instant.now();
        Instant clickInstant = Instant.ofEpochMilli(clickCooldownUntil);
        if (clickInstant.isAfter(now)) {
            return true;
        }
        clickCooldownUntil = now.toEpochMilli() + clickCooldown;

        long slotClick = slotClickCooldownMap.computeIfAbsent(slot, key -> 0L);
        Instant slotClickInstant = Instant.ofEpochMilli(slotClick);
        if (slotClickInstant.isAfter(now)) {
            return true;
        }
        slotClick = now.toEpochMilli() + slotClickCooldown;
        slotClickCooldownMap.put(slot, slotClick);
        return false;
    }

    @Override
    public void addFlags(@NotNull Flag... flags) {
        addedFlags.addAll(Arrays.asList(flags));
    }

    @Override
    public boolean hasFlags(@NotNull Flag... flags) {
        return addedFlags.containsAll(Arrays.asList(flags));
    }

    @Override
    public void removeFlags(@NotNull Flag... flags) {
        Arrays.asList(flags).forEach(addedFlags::remove);
    }

    @Override
    public void setInteraction(int rawSlot, @NotNull Interaction interaction) {
        interactions.put(rawSlot, interaction);
    }

    @Override
    public void setItemStack(int rawSlot, @NotNull ItemStack itemStack) {
        if (type.isTopSlot(rawSlot)) {
            topInventory.setItemStack(rawSlot, itemStack);
            return;
        }
        int firstBottomSlot = type.getFirstBottomSlot();
        int conversionSlot = rawSlot - firstBottomSlot;
        int expectedSlot = BottomSlotUtility.denormalizeBottomSlot(conversionSlot);
        bottomInventory.setItemStack(expectedSlot, itemStack);
    }

    @Override
    public @NotNull ItemStack getItemStack(int rawSlot) {
        if (type.isTopSlot(rawSlot)) {
            return topInventory.getItemStack(rawSlot);
        }
        int firstBottomSlot = type.getFirstBottomSlot();
        int conversionSlot = rawSlot - firstBottomSlot;
        int expectedSlot = BottomSlotUtility.denormalizeBottomSlot(conversionSlot);
        return bottomInventory.getItemStack(expectedSlot);
    }

    @Override
    public @NotNull Interaction getInteraction(int rawSlot) {
        Interaction interaction = interactions.get(rawSlot);
        if (interaction == null) {
            return Interactions.cancel();
        }
        return interaction;
    }

    @Override
    public void updateState() {
        registry.executeComponents(
            rootComponent,
            component -> component.onStateUpdate(this, player)
        );
    }

    @Override
    public String toString() {
        return "PlayerView{" +
            "id=" + id + ", " +
            "component=" + rootComponent.toString() + ", " +
            "player=" + player.getUsername() +
            "}";
    }
}
