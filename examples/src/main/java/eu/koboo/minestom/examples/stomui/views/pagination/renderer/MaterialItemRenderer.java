package eu.koboo.minestom.examples.stomui.views.pagination.renderer;

import eu.koboo.minestom.stomui.api.item.PrebuiltItem;
import eu.koboo.minestom.stomui.api.pagination.ItemRenderer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MaterialItemRenderer implements ItemRenderer<Material> {

    @Override
    public PrebuiltItem renderItem(Material material) {
        return PrebuiltItem.of(ItemStack.of(material));
    }
}
