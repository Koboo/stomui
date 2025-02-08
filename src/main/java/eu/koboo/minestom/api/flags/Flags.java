package eu.koboo.minestom.api.flags;

public final class Flags {

    /**
     * Allows the interaction on the bottom slots of the inventory.
     */
    public static final Flag ALLOW_BOTTOM_INTERACTION = new Flag("allow_bottom_interaction");

    /**
     * Closes the view on the interaction with any bottom slots of the inventory.
     */
    public static final Flag CLOSE_ON_BOTTOM_INTERACTION = new Flag("close_on_bottom_interaction");

    /**
     * Allows the interaction on the outside of the inventory.
     */
    public static final Flag ALLOW_OUTSIDE_INTERACTION = new Flag("allow_outside_interaction");

    /**
     * Closes the view on the interaction with the outside of the inventory.
     */
    public static final Flag CLOSE_ON_OUTSIDE_INTERACTION = new Flag("close_on_outside_interaction");

    /**
     * Converts the cursor click (slot = -1) to an outside interaction.
     */
    public static final Flag CONVERT_CURSOR_TO_OUTSIDE_INTERACTION = new Flag("convert_cursor_to_outside_interaction");
}
