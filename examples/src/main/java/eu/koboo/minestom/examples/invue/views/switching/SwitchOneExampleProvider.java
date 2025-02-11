package eu.koboo.minestom.examples.invue.views.switching;

import eu.koboo.minestom.invue.api.ViewRegistry;

public class SwitchOneExampleProvider extends SwitchParentProvider {

    public SwitchOneExampleProvider(ViewRegistry registry) {
        super(registry, "First View");
    }

    @Override
    public SwitchParentProvider oppositeViewProvider() {
        return new SwitchTwoExampleProvider(registry);
    }
}
