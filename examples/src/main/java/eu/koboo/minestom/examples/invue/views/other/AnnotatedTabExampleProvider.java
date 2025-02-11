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
import eu.koboo.minestom.invue.api.slots.SlotIterator;
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
    public void onStateUpdate(@NotNull PlayerView view, @NotNull Player player) {
        // Getting all ViewItems by the slots of the tab content.
        for (ViewItem viewItem : ViewItem.bySlotIterator(view, tabContentSlots)) {

            // Changing the material and name, just to visual changes in the example
            viewItem.material(currentTab.getMaterial())
                .displayName(currentTab.getName() + " Settings Name");
        }
    }

    @Slot(0)
    public PrebuiltItem closeItem() {
        // Closes the inventory
        return PrebuiltItem.empty()
            .material(Material.REDSTONE)
            .displayName("Close")
            .closeInventoryInteraction();
    }

    @Slot(2)
    @Stateful
    public PrebuiltItem profileItem() {
        // Changes the current tab to PROFILE
        return createSettingsItem(SettingsTab.PROFILE);
    }

    @Slot(4)
    @Stateful
    public PrebuiltItem clanItem() {
        // Changes the current tab to CLAN
        return createSettingsItem(SettingsTab.CLAN);
    }

    @Slot(6)
    @Stateful
    public PrebuiltItem friendsItem() {
        // Changes the current tab to FRIEND
        return createSettingsItem(SettingsTab.FRIEND);
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

    private PrebuiltItem createSettingsItem(SettingsTab settingsTab) {
        // Method to create a tab item, based on the given settingsTab.
        return PrebuiltItem.empty()
            .material(settingsTab.getMaterial())
            .displayName(settingsTab.getName())
            .glint(currentTab == settingsTab)
            .interaction(interaction -> {
                // If the user clicks this item,
                // we update the current tab field in this component instance,
                // and we call updateState(), to ensure the tab item,
                // which was selected starts glinting (enchantment glow)
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
