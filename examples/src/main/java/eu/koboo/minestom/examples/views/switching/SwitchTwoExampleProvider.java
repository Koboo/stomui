package eu.koboo.minestom.examples.views.switching;

import eu.koboo.minestom.api.ViewRegistry;

public class SwitchTwoExampleProvider extends SwitchParentComponent {

    public SwitchTwoExampleProvider(ViewRegistry registry) {
        super(registry, "Second View");
    }

    @Override
    public SwitchParentComponent oppositeViewProvider() {
        return new SwitchOneExampleProvider(registry);
    }
}
