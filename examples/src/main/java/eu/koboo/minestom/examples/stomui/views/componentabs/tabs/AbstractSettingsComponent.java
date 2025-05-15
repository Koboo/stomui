package eu.koboo.minestom.examples.stomui.views.componentabs.tabs;

import eu.koboo.minestom.examples.stomui.utils.SettingsTab;
import eu.koboo.minestom.stomui.api.component.ViewComponent;

public abstract class AbstractSettingsComponent extends ViewComponent {

    private final SettingsTab settingsTab;

    public AbstractSettingsComponent(SettingsTab settingsTab) {
        this.settingsTab = settingsTab;
    }
}
