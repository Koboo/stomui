package eu.koboo.minestom.examples.stomui.views.other;

import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewBuilder;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.annotations.Slot;
import eu.koboo.minestom.stomui.api.annotations.Rebuildable;
import eu.koboo.minestom.stomui.api.annotations.components.AnnotationRenderComponent;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import eu.koboo.minestom.stomui.api.item.PrebuiltItem;
import eu.koboo.minestom.stomui.api.item.ViewItem;
import eu.koboo.minestom.stomui.api.slots.SlotIterator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class AnnotatedTabExampleProvider extends ViewProvider {

    SettingsTab currentTab;
    SlotIterator tabContentSlots;

    public AnnotatedTabExampleProvider(ViewRegistry registry) {
        super(registry, ViewBuilder.of(ViewType.SIZE_6_X_9)
            .title("Annotated tab example"));

        // Set the default visible tab
        this.currentTab = SettingsTab.PROFILE;

        // Creating a SlotIterator with all slots of the content.
        tabContentSlots = SlotIterator.of(this)
            .startPosition(1, 1)
            .endPosition(5, 8)
            .blacklistTopBorder();

        addChild(new AnnotationRenderComponent());
    }

    @Override
    public void onRebuild(@NotNull PlayerView view, @NotNull Player player) {
        // Getting all ViewItems by the slots of the tab content.
        for (ViewItem viewItem : ViewItem.bySlotIterator(view, tabContentSlots)) {

            // Changing the material and name, just to visual changes in the example
            viewItem.material(currentTab.getMaterial())
                .displayName(currentTab.getName() + " Settings Name");
        }
    }

    @Slot(0)
    // No @Stateful means, only build on view opening.
    public PrebuiltItem closeItem() {
        // Closes the inventory
        return PrebuiltItem.empty()
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();
    }

    @Slot(2)
    @Rebuildable // Rebuilds with "onStateUpdate"
    public PrebuiltItem profileItem() {
        // Changes the current tab to PROFILE
        return createSettingsItem(SettingsTab.PROFILE);
    }

    @Slot(4)
    @Rebuildable // Rebuilds with "onStateUpdate"
    public PrebuiltItem clanItem() {
        // Changes the current tab to CLAN
        return createSettingsItem(SettingsTab.CLAN);
    }

    @Slot(6)
    @Rebuildable // Rebuilds with "onStateUpdate"
    public PrebuiltItem friendsItem() {
        // Changes the current tab to FRIEND
        return createSettingsItem(SettingsTab.FRIEND);
    }

    private PrebuiltItem createSettingsItem(SettingsTab ownTab) {
        // Method to create a tab item, based on the given ownTab.
        return PrebuiltItem.empty()
            .material(ownTab.getMaterial())
            .displayName(ownTab.getName())
            // Because the item gets rebuild "onStateUpdate" we can just check
            // the currentTab here.
            .glint(currentTab == ownTab)
            .interaction(action -> {
                // If the user clicks this item,
                // we update the current tab field in this component instance,
                // and we call updateState(), to ensure the tab item,
                // which was selected starts glinting (enchantment glow)
                this.currentTab = ownTab;
                action.getView().executeRebuild();
            });
    }

    @Override
    public void onOpen(@NotNull PlayerView view, @NotNull Player player) {
        // Getting all topSlots by the ViewType of the view.
        List<Integer> topSlots = view.getType().getTopSlots();

        // Getting all ViewItems by the slot list of the ViewType.
        for (ViewItem viewItem : ViewItem.bySlotList(view, topSlots)) {

            // Setting placeholder items
            viewItem.material(Material.GRAY_STAINED_GLASS_PANE)
                .displayName(" ")
                .cancelClicking();
        }
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
