package eu.koboo.minestom.invue.api.interaction;

import eu.koboo.minestom.invue.api.PlayerView;

/**
 * This interface is used to define any {@link Interaction} on a single or multiple slots
 * within the top- or bottom-inventory.
 * There are several default {@link Interaction}.
 * See more at {@link Interactions}
 */
@FunctionalInterface
public interface Interaction {

    /**
     * Gets called everytime the player interacts with the {@link PlayerView}.
     *
     * @param interaction The wrapper of interaction relevant properties. See more at {@link ViewAction}.
     */
    void interact(ViewAction interaction);
}
