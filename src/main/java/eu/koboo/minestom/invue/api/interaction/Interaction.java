package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.PlayerView;

/**
 * This interface is used to define any {@link Interaction} on a single or multiple slots
 * within the top- or bottom-inventory.
 * <p>
 * There are several default {@link Interaction}s defined in {@link Interactions}.
 */
@FunctionalInterface
public interface Interaction {

    /**
     * Gets called everytime the player interacts with the {@link PlayerView}.
     *
     * @param action The wrapper of the interaction relevant properties. See more all: {@link ViewAction}
     */
    void interact(ViewAction action);
}
