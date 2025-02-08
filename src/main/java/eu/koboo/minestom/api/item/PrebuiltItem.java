package eu.koboo.minestom.api.item;

import eu.koboo.minestom.api.interaction.Interaction;
import eu.koboo.minestom.api.interaction.Interactions;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PrebuiltItem extends ModifiableItem {

    public static PrebuiltItem empty() {
        return new PrebuiltItem();
    }

    public static PrebuiltItem of(ItemStack itemStack) {
        return of(itemStack, Interactions.cancel());
    }

    public static PrebuiltItem of(Interaction interaction) {
        return of(ItemStack.of(Material.AIR), interaction);
    }

    public static PrebuiltItem of(@NotNull ItemStack itemStack,
                                  @NotNull Interaction interaction) {
        return new PrebuiltItem()
            .item(itemStack)
            .interaction(interaction);
    }

    ItemStack itemStack;
    Interaction interaction;

    private PrebuiltItem() {
        this.itemStack = ItemStack.of(Material.AIR);
        this.interaction = Interactions.cancel();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return itemStack;
    }

    @Override
    public @NotNull Interaction getInteraction() {
        return interaction;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ModifiableItem> T item(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ModifiableItem> T interaction(@NotNull Interaction interaction) {
        this.interaction = interaction;
        return (T) this;
    }
}
