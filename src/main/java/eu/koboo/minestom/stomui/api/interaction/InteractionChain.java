package eu.koboo.minestom.stomui.api.interaction;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InteractionChain implements Interaction {

    Set<Interaction> interactionChainSet;

    public InteractionChain() {
        this.interactionChainSet = new LinkedHashSet<>();
    }

    @Override
    public void interact(ViewAction action) {
        for (Interaction interaction : interactionChainSet) {
            interaction.interact(action);
        }
    }

    @Override
    public Interaction with(Interaction otherInteraction) {
        interactionChainSet.add(otherInteraction);
        return this;
    }
}
