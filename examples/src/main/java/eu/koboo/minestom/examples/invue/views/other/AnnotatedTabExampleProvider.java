package eu.koboo.minestom.examples.invue.views.other;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import eu.koboo.minestom.invue.api.ViewRegistry;
import eu.koboo.minestom.invue.api.ViewType;
import eu.koboo.minestom.invue.api.annotations.Slot;
import eu.koboo.minestom.invue.api.annotations.Stateful;
import eu.koboo.minestom.invue.api.annotations.components.AnnotationRenderComponent;
import eu.koboo.minestom.invue.api.component.RootViewComponent;
import eu.koboo.minestom.invue.api.item.PrebuiltItem;
import eu.koboo.minestom.invue.api.item.ViewItem;
import eu.koboo.minestom.invue.api.slots.SlotBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class AnnotatedTabExampleProvider extends RootViewComponent {

    SettingsTab currentTab;
    List<Integer> currentTabItemSlots;

    public AnnotatedTabExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("Annotated tab example"));
        this.currentTab = SettingsTab.PROFILE;
        this.currentTabItemSlots = new SlotBuilder()
            .startPosition(1, 1)
            .endPosition(5, 8)
            .blacklistTopBorder(getBuilder().getType())
            .toList();

        addChild(new AnnotationRenderComponent());
    }

    @Override
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        for (ViewItem viewItem : ViewItem.bySlotList(view, currentTabItemSlots)) {
            viewItem.material(currentTab.getMaterial())
                .displayName(currentTab.getName() + " Settings Name");
        }
    }

    @Slot(0)
    public PrebuiltItem closeItem() {
        return PrebuiltItem.empty()
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();
    }

    @Slot(2)
    @Stateful
    public PrebuiltItem profileItem() {
        return createSettingsItem(SettingsTab.PROFILE);
    }

    @Slot(4)
    @Stateful
    public PrebuiltItem clanItem() {
        return createSettingsItem(SettingsTab.CLAN);
    }

    @Slot(6)
    @Stateful
    public PrebuiltItem friendsItem() {
        return createSettingsItem(SettingsTab.FRIEND);
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        List<Integer> topSlots = view.getType().getTopSlots();
        for (ViewItem viewItem : ViewItem.bySlotList(view, topSlots)) {
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ");
        }
    }

    private PrebuiltItem createSettingsItem(SettingsTab settingsTab) {
        return PrebuiltItem.empty()
            .material(settingsTab.getMaterial())
            .displayName(settingsTab.getName())
            .glint(currentTab == settingsTab)
            .interaction(interaction -> {
                this.currentTab = settingsTab;
                interaction.getView().updateState();
            });
    }

    @Getter
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private enum SettingsTab {

        PROFILE(Material.OAK_BOAT, "Profile"),
        CLAN(Material.ACACIA_BOAT, "Clan"),
        FRIEND(Material.GOLDEN_APPLE, "Friends");

        Material material;
        String name;
    }
}
