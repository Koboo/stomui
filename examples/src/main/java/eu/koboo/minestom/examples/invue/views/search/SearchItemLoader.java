package eu.koboo.minestom.examples.invue.views.search;

import eu.koboo.minestom.invue.api.item.PrebuiltItem;
import eu.koboo.minestom.invue.api.pagination.ItemLoader;
import eu.koboo.minestom.invue.api.pagination.Pagifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchItemLoader implements ItemLoader {

    String searchInput;

    @Override
    public void load(Pagifier<PrebuiltItem> pagifier) {
        List<Material> values = new ArrayList<>(Material.values());
        values.sort(Comparator.comparing(Material::id));
        for (Material value : values) {
            // Only blocks please.
            if (value == Material.AIR || !value.isBlock()) {
                continue;
            }
            // If we got an input, filter all materials out,
            // if they don't match the input.
            if (searchInput != null && !searchInput.isEmpty()) {
                if (!value.name().toLowerCase().contains(searchInput.toLowerCase())) {
                    continue;
                }
            }
            pagifier.addItem(PrebuiltItem.of(ItemStack.of(value)));
        }
    }
}
