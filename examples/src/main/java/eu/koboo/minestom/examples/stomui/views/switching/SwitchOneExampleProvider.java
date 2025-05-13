package eu.koboo.minestom.examples.stomui.views.switching;

import eu.koboo.minestom.stomui.api.ViewRegistry;

public class SwitchOneExampleProvider extends SwitchParentProvider {

    public SwitchOneExampleProvider(ViewRegistry registry) {
        super(registry, "First View");
    }

    @Override
    public SwitchParentProvider oppositeViewProvider() {
        return new SwitchTwoExampleProvider(registry);
    }
}
