package eu.koboo.minestom.stomui.core;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.flags.Flag;
import eu.koboo.minestom.stomui.api.flags.Flags;
import eu.koboo.minestom.stomui.api.interaction.Interaction;
import eu.koboo.minestom.stomui.api.interaction.Interactions;
import eu.koboo.minestom.stomui.api.utils.BottomSlotUtility;
import eu.koboo.minestom.stomui.api.utils.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
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

    final ViewProvider provider;
    final Map<Integer, Interaction> interactions;
    final Set<Flag> addedFlags;
    final Set<Class<? extends Click>> disabledClickTypes;

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

        this.provider = builder.getProvider();

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
                provider,
                component -> component.onOpen(this, player)
            );
        }
        player.openInventory(topInventory);
        executeRebuild();
    }

    @ApiStatus.Internal
    public void closeView() {
        registry.executeComponents(
            provider,
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
        addedFlags.addAll(List.of(flags));
    }

    @Override
    public boolean hasFlags(@NotNull Flag... flags) {
        return addedFlags.containsAll(List.of(flags));
    }

    @Override
    public void removeFlags(@NotNull Flag... flags) {
        List.of(flags).forEach(addedFlags::remove);
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
    public void executeRebuild() {
        registry.executeComponents(
            provider,
            component -> component.onRebuild(this, player)
        );
    }

    @Override
    public void setTitle(@NotNull Component component) {
        topInventory.setTitle(component);
    }

    @Override
    public void setTitle(@NotNull String miniMessage) {
        setTitle(MiniMessage.miniMessage().deserialize(miniMessage));
    }

    @Override
    public String toString() {
        return "PlayerView{" +
            "id=" + id + ", " +
            "component=" + provider.toString() + ", " +
            "player=" + player.getUsername() +
            "}";
    }
}
