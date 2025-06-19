package eu.koboo.minestom.stomui.api.interaction;

import eu.koboo.minestom.stomui.api.PlayerView;

/**
 * This interface is used to define a {@link Interaction} on a single slot or multiple slots
 * within the top- or bottom-inventory. It's called on a single slot
 * but can be assigned to multiple slots, so several slots could share the same {@link Interaction}.
 * You can get the respective clicked slot through the given {@link ViewAction} and its methods.
 * <p>
 * There are several default {@link Interaction}s defined in {@link Interactions}.
 */
@FunctionalInterface
public interface Interaction {

    /**
     * Gets called everytime the player interacts with
     * a slot within the currently opened {@link PlayerView}.
     *
     * @param action The wrapper of the interaction relevant properties.
     *               See {@link ViewAction} for more and detailed information.
     */
    void interact(ViewAction action);

    default Interaction with(Interaction otherInteraction) {
        InteractionChain chain = new InteractionChain();
        return chain.with(otherInteraction);
    }
}
