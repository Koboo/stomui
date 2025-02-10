package eu.koboo.minestom.invue.api.item;

import eu.koboo.minestom.invue.api.interaction.Interaction;
import eu.koboo.minestom.invue.api.interaction.Interactions;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract sealed class ModifiableItem permits PrebuiltItem, ViewItem {

    public abstract @NotNull ItemStack getItem();

    public abstract @NotNull Interaction getInteraction();

    public abstract <T extends ModifiableItem> T item(@NotNull ItemStack itemStack);

    public abstract <T extends ModifiableItem> T interaction(@NotNull Interaction interaction);

    public <T extends ModifiableItem> T cancelClicking() {
        return interaction(Interactions.cancel());
    }

    public <T extends ModifiableItem> T allowClicking() {
        return interaction(Interactions.allow());
    }

    public <T extends ModifiableItem> T closeInventoryInteraction() {
        return interaction(Interactions.close());
    }

    public <T extends ModifiableItem> T material(@NotNull Material newMaterial) {
        return item(getItem().withMaterial(newMaterial));
    }

    public boolean hasMaterial(@NotNull Material material) {
        return getItem().material() == material;
    }

    public <T extends ModifiableItem> T displayName(@NotNull Component component) {
        return name(component);
    }

    public <T extends ModifiableItem> T name(@NotNull Component component) {
        return item(getItem().withCustomName(component));
    }

    public <T extends ModifiableItem> T displayName(@NotNull String text) {
        return name(text);
    }

    public <T extends ModifiableItem> T name(@NotNull String text) {
        return name(MiniMessage.miniMessage().deserialize(text));
    }

    public <T extends ModifiableItem> T loreComponents(@NotNull List<Component> lines) {
        return item(getItem().withLore(lines));
    }

    public <T extends ModifiableItem> T lore(@NotNull List<String> lines) {
        List<Component> componentList = new ArrayList<>();
        for (String line : lines) {
            componentList.add(MiniMessage.miniMessage().deserialize(line));
        }
        return loreComponents(componentList);
    }

    public <T extends ModifiableItem> T lore(@NotNull String... lines) {
        return lore(List.of(lines));
    }

    public <T extends ModifiableItem> T loreLine(int lineIndex, @Nullable Component component) {
        ItemStack itemStack = getItem();
        List<Component> newComponentList = new ArrayList<>();
        List<Component> loreComponentList = itemStack.get(ItemComponent.LORE);
        if (loreComponentList != null) {
            newComponentList.addAll(loreComponentList);
        }
        if (component == null) {
            component = Component.empty();
        }
        newComponentList.set(lineIndex, component);
        return item(itemStack.withLore(newComponentList));
    }

    public <T extends ModifiableItem> T loreLine(int lineIndex, @Nullable String text) {
        Component component;
        if (text == null) {
            component = null;
        } else {
            component = MiniMessage.miniMessage().deserialize(text);
        }
        return loreLine(lineIndex, component);
    }

    public <T extends ModifiableItem> T amount(int amount) {
        return item(getItem().withAmount(amount));
    }

    public <D, T extends ModifiableItem> T addComponent(DataComponent<D> dataComponent, D value) {
        return item(getItem().with(dataComponent, value));
    }

    public <D, T extends ModifiableItem> T removeComponent(DataComponent<D> dataComponent) {
        return item(getItem().without(dataComponent));
    }

    public <D, T extends ModifiableItem> D getComponentValue(DataComponent<D> dataComponent) {
        return getItem().get(dataComponent);
    }

    public <D> boolean hasComponent(DataComponent<D> dataComponent) {
        return getItem().has(dataComponent);
    }

    public <D> boolean hasComponentValue(DataComponent<D> dataComponent, D value) {
        D componentValue = getComponentValue(dataComponent);
        if (componentValue == null) {
            return false;
        }
        return componentValue.equals(value);
    }

    public <T extends ModifiableItem> T glint(boolean isGlint) {
        return addComponent(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, isGlint);
    }

    public <T extends ModifiableItem> T glint() {
        return glint(true);
    }

    public <T extends ModifiableItem> T unglint() {
        return glint(false);
    }

    public boolean hasGlint() {
        return hasComponentValue(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    public <T extends ModifiableItem> T hideTooltip(boolean hide) {
        T ret;
        if (hide) {
            ret = addComponent(ItemComponent.HIDE_TOOLTIP, Unit.INSTANCE);
        } else {
            ret = removeComponent(ItemComponent.HIDE_TOOLTIP);
        }
        return ret;
    }
}
