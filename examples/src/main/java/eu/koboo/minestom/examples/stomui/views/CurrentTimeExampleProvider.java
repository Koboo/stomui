package eu.koboo.minestom.examples.stomui.views;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CurrentTimeExampleProvider extends ViewProvider {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalTime currentTime;

    public CurrentTimeExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_5_X_1));
        currentTime = LocalTime.now();
    }

    @Override
    public void modifyBuilder(@NotNull ViewBuilder viewBuilder, @NotNull Player player) {
        viewBuilder.title(getCurrentTime());
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        List<Integer> allSlotsOfTopInventory = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, allSlotsOfTopInventory)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .cancelClicking();
        }
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                currentTime = LocalTime.now();
                view.setTitle(getCurrentTime());
            },
            TaskSchedule.immediate(),
            TaskSchedule.seconds(1)
        );
    }

    private String getCurrentTime() {
        return "<yellow>" + FORMATTER.format(currentTime) + "</yellow>";
    }
}
