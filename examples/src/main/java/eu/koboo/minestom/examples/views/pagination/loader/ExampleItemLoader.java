package eu.koboo.minestom.examples.views.pagination.loader;

import eu.koboo.minestom.api.item.PrebuiltItem;
import eu.koboo.minestom.api.pagination.ItemLoader;
import eu.koboo.minestom.api.pagination.Pagifier;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExampleItemLoader implements ItemLoader {

    @Override
    public void load(Pagifier<PrebuiltItem> pagifier) {
        List<Material> values = new ArrayList<>(Material.values());
        values.sort(Comparator.comparing(Material::id));
        for (Material value : values) {
            if (value == Material.AIR || !value.isBlock()) {
                continue;
            }
            pagifier.addItem(PrebuiltItem.of(ItemStack.of(value)));
        }
    }
}
