package eu.koboo.minestom.api;

import eu.koboo.minestom.api.component.RootViewComponent;
import eu.koboo.minestom.api.flags.Flag;
import eu.koboo.minestom.api.flags.Flags;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.inventory.click.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private ViewBuilder(@NotNull ViewType type) {
        this.type = type;
        this.flags = new HashSet<>();
        this.disabledClickTypes = new HashSet<>();
        this.title = Component.text("A custom view");
        this.rootComponent = null;
        this.clickCooldownInMillis = 10;
        this.slotClickCooldownInMillis = 10;
    }

    public @NotNull ViewBuilder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    public @NotNull ViewBuilder title(@NotNull String miniMessageText) {
        this.title = MiniMessage.miniMessage().deserialize(miniMessageText);
        return this;
    }

    public @NotNull ViewBuilder component(@NotNull RootViewComponent component) {
        if (this.rootComponent != null) {
            throw new IllegalStateException("Root component has already been set");
        }
        this.rootComponent = component;
        return this;
    }

    public @NotNull ViewBuilder clickCooldown(int millis) {
        this.clickCooldownInMillis = millis;
        return this;
    }

    public @NotNull ViewBuilder slotClickCooldown(int millis) {
        this.slotClickCooldownInMillis = millis;
        return this;
    }

    public @NotNull ViewBuilder withFlags(@NotNull Flag... flags) {
        this.flags.addAll(List.of(flags));
        return this;
    }

    public @NotNull ViewBuilder disableClickTypes(@NotNull ClickType... clickTypes) {
        this.disabledClickTypes.addAll(List.of(clickTypes));
        return this;
    }

    public @NotNull ViewBuilder allowBottomInteractions() {
        return withFlags(Flags.ALLOW_BOTTOM_INTERACTION);
    }

    public @NotNull ViewBuilder closeOnBottomInteractions() {
        return withFlags(Flags.CLOSE_ON_BOTTOM_INTERACTION);
    }

    public @NotNull ViewBuilder allowOutsideInteractions() {
        return withFlags(Flags.ALLOW_OUTSIDE_INTERACTION);
    }

    public @NotNull ViewBuilder closeOutsideOnInteractions() {
        return withFlags(Flags.ALLOW_OUTSIDE_INTERACTION);
    }

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

    public static @NotNull ViewBuilder of(@NotNull ViewType type) {
        return new ViewBuilder(type);
    }

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
