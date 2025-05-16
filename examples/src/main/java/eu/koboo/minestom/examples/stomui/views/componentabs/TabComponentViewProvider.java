package eu.koboo.minestom.examples.stomui.views.componentabs;

import eu.koboo.minestom.examples.stomui.utils.SettingsTab;
import eu.koboo.minestom.examples.stomui.views.componentabs.tabs.ClanTabComponent;
import eu.koboo.minestom.examples.stomui.views.componentabs.tabs.FriendsTabComponent;
import eu.koboo.minestom.examples.stomui.views.componentabs.tabs.ProfileTabComponent;
import eu.koboo.minestom.stomui.api.PlayerView;
import eu.koboo.minestom.stomui.api.ViewRegistry;
import eu.koboo.minestom.stomui.api.ViewType;
import eu.koboo.minestom.stomui.api.component.ViewComponent;
import eu.koboo.minestom.stomui.api.component.ViewProvider;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TabComponentViewProvider extends ViewProvider {

    private SettingsTab settingsTab;
    private ViewComponent tabComponent;

    // NOT FULLY IMPLEMENTED
    public TabComponentViewProvider(ViewRegistry viewRegistry) {
        super(viewRegistry, ViewType.SIZE_6_X_9);
        settingsTab = SettingsTab.CLAN;
    }

    @Override
    public void onRebuild(@NotNull PlayerView view, @NotNull Player player) {
        // tabComponent is null on first rebuild triggered by opening the inventory.
        if (tabComponent != null) {
            removeChild(tabComponent);
        }
        tabComponent = createComponentByTab();
        addChild(tabComponent);
    }

    private ViewComponent createComponentByTab() {
        return switch (settingsTab) {
            case PROFILE -> new ProfileTabComponent();
            case CLAN -> new ClanTabComponent();
            case FRIEND -> new FriendsTabComponent();
        };
    }
}
