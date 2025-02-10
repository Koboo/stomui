package eu.koboo.minestom.invue.api;

import eu.koboo.minestom.invue.api.component.RootViewComponent;
import eu.koboo.minestom.invue.api.flags.Flag;
import eu.koboo.minestom.invue.api.flags.Flags;
import eu.koboo.minestom.invue.api.interaction.Interaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This builder is used to define several properties and behaviours of the {@link PlayerView},
 * which gets created, by passing this {@link ViewBuilder} into
 * {@link ViewRegistry#open(Player, ViewBuilder)}.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ViewBuilder {

    final ViewType type;
    final Set<Flag> flags;
    final Set<ClickType> disabledClickTypes;
    RootViewComponent rootComponent;
    Component title;
    int clickCooldownInMillis;
    int slotClickCooldownInMillis;

    /**
     * Required private constructor
     * @param type The {@link ViewType}, which has to be any value of the enum.
     */
    private ViewBuilder(@NotNull ViewType type) {
        this.type = type;
        this.flags = new HashSet<>();
        this.disabledClickTypes = new HashSet<>();
        this.title = Component.text("A custom view");
        this.rootComponent = null;
        this.clickCooldownInMillis = 10;
        this.slotClickCooldownInMillis = 10;
    }

    /**
     * Sets the title as {@link Component} of the opened {@link Inventory}.
     * @param title The title of the {@link Inventory}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the title as {@link String} to be deserialized by {@link MiniMessage}
     * of the opened {@link Inventory}.
     * @param miniMessageText The title of the {@link Inventory}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder title(@NotNull String miniMessageText) {
        this.title = MiniMessage.miniMessage().deserialize(miniMessageText);
        return this;
    }

    /**
     * Sets the {@link RootViewComponent} to be used as start point of the component tree.
     * @param component The root component.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder component(@NotNull RootViewComponent component) {
        if (this.rootComponent != null) {
            throw new IllegalStateException("Root component has already been set");
        }
        this.rootComponent = component;
        return this;
    }

    /**
     * Sets the cooldown for every slot of the opened inventory.
     * @param millis The cooldown in milliseconds. Needs to be greater or equal to 0.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder clickCooldown(int millis) {
        this.clickCooldownInMillis = millis;
        return this;
    }

    /**
     * Sets the cooldown for a specific slot of the opened inventory.
     * @param millis The cooldown in milliseconds. Needs to be greater or equal to 0.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder slotClickCooldown(int millis) {
        this.slotClickCooldownInMillis = millis;
        return this;
    }

    /**
     * Adds the given {@link Flag} into the new instance of {@link PlayerView}.
     * @param flags The {@link Flag}s, which will be added.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder withFlags(@NotNull Flag... flags) {
        this.flags.addAll(List.of(flags));
        return this;
    }

    /**
     * Adds the given {@link ClickType}s as disabled/cancelled, which results in
     * ignoring any {@link Interaction} if the specific {@link ClickType} is used
     * and just cancelling the {@link InventoryPreClickEvent}.
     * @param clickTypes The {@link ClickType}s, which will be cancelled.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder disableClickTypes(@NotNull ClickType... clickTypes) {
        this.disabledClickTypes.addAll(List.of(clickTypes));
        return this;
    }

    /**
     * Adds the {@link Flag} {@link Flags#ALLOW_BOTTOM_INTERACTION}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder allowBottomInteractions() {
        return withFlags(Flags.ALLOW_BOTTOM_INTERACTION);
    }

    /**
     * Adds the {@link Flag} {@link Flags#CLOSE_ON_BOTTOM_INTERACTION}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder closeOnBottomInteractions() {
        return withFlags(Flags.CLOSE_ON_BOTTOM_INTERACTION);
    }

    /**
     * Adds the {@link Flag} {@link Flags#ALLOW_OUTSIDE_INTERACTION}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder allowOutsideInteractions() {
        return withFlags(Flags.ALLOW_OUTSIDE_INTERACTION);
    }

    /**
     * Adds the {@link Flag} {@link Flags#ALLOW_OUTSIDE_INTERACTION}.
     * @return This {@link ViewBuilder} instance.
     */
    public @NotNull ViewBuilder closeOutsideOnInteractions() {
        return withFlags(Flags.ALLOW_OUTSIDE_INTERACTION);
    }

    /**
     * Called by {@link ViewRegistry#open(PlayerView)}, to validate the properties of
     * this current instance of {@link ViewBuilder}.
     */
    public void validate() {
        if (type == null) {
            throw new NullPointerException("ViewType is null");
        }
        if (rootComponent == null) {
            throw new NullPointerException("RootViewComponent is null");
        }
        if (clickCooldownInMillis < 0) {
            throw new IllegalArgumentException("clickCooldownInMillis must be positive or 0");
        }
        if (slotClickCooldownInMillis < 0) {
            throw new IllegalArgumentException("slotClickCooldownInMillis must be positive or 0");
        }
    }

    /**
     * Creates a new {@link ViewBuilder} with the given {@link ViewType}.
     * @param type The given {@link ViewType}, to create a new {@link ViewBuilder}.
     * @return A new instance of {@link ViewBuilder}
     */
    public static @NotNull ViewBuilder of(@NotNull ViewType type) {
        return new ViewBuilder(type);
    }

    /**
     * Create a new copied instance of{@link ViewBuilder} by the given {@link ViewBuilder}.
     * @param builder The instance of {@link ViewBuilder} to copy.
     * @return A new instance of {@link ViewBuilder} with all properties copied.
     */
    public static ViewBuilder copyOf(ViewBuilder builder) {
        ViewBuilder copy = new ViewBuilder(builder.type);
        copy.title(builder.title);
        copy.component(builder.rootComponent);
        copy.clickCooldown(builder.clickCooldownInMillis);
        copy.slotClickCooldown(builder.slotClickCooldownInMillis);
        copy.flags.addAll(builder.flags);
        copy.disabledClickTypes.addAll(builder.disabledClickTypes);
        return copy;
    }
}
