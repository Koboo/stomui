package eu.koboo.minestom.examples.stomui.views.switching;

import eu.koboo.minestom.stomui.api.ViewRegistry;

public class SwitchTwoExampleProvider extends SwitchParentProvider {

    public SwitchTwoExampleProvider(ViewRegistry registry) {
        super(registry, "Second View");
    }

    @Override
    public SwitchParentProvider oppositeViewProvider() {
        return new SwitchOneExampleProvider(registry);
    }
}
