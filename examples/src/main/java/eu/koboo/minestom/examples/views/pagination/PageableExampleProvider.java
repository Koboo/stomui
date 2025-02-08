package eu.koboo.minestom.examples.views.pagination;

import eu.koboo.minestom.api.ViewBuilder;
import eu.koboo.minestom.api.ViewRegistry;
import eu.koboo.minestom.api.ViewType;
import eu.koboo.minestom.api.component.RootViewComponent;
import eu.koboo.minestom.api.pagination.ViewPagination;
import eu.koboo.minestom.api.slots.ViewPattern;
import eu.koboo.minestom.examples.views.pagination.components.PaginationBorder;
import eu.koboo.minestom.examples.views.pagination.loader.ExampleItemLoader;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageableExampleProvider extends RootViewComponent {

    // Render order of components:
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
        ViewPagination pagination = registry.pageable(
            new ExampleItemLoader(),
            pattern.getMergedSlots('1')
        );
        addChild(new PaginationBorder(pagination, pattern));
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title("<red>Pageable example");
    }
}
