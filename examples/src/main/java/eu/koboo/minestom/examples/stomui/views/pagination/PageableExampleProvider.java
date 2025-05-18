package eu.koboo.minestom.examples.stomui.views.pagination;

import eu.koboo.minestom.examples.stomui.views.pagination.components.PaginationBorder;
import eu.koboo.minestom.examples.stomui.views.pagination.renderer.MaterialItemRenderer;
import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.pagination.ViewPagination;
import eu.koboo.minestom.stomui.api.slots.ViewPattern;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageableExampleProvider extends ViewProvider {

    ViewPagination<Material> pagination;

    // Hierarchy of components:
    // - this
    //   - PaginationBorder
    //     - PaginationActionButtons
    //       - ViewPagination
    public PageableExampleProvider(ViewRegistry registry) {
        super(registry, ViewType.SIZE_6_X_9);
        ViewPattern pattern = registry.pattern(
            "K#######Z",
            "#1111111#",
            "#1111111#",
            "#1111111#",
            "#1111111#",
            "##<###>##"
        );
        pagination = registry.pageable(
            new MaterialItemRenderer(),
            ItemStack.AIR,
            pattern.getMergedSlots('1')
        );
        pagination.setItemSorter(Comparator.comparing(Material::id));
        pagination.addItems(Material.values());
        addChild(new PaginationBorder(pagination, pattern));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title("<red>Pageable example");
    }
}
