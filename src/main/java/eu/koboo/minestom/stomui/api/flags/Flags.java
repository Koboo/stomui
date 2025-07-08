package eu.koboo.minestom.stomui.api.flags;

import eu.koboo.minestom.stomui.core.listener.ViewInventoryPreClickListener;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class holds the static instances of several default {@link Flag}s.
 */
public final class Flags {

    /**
     * Allows the interaction on the bottom slots of the inventory.
     * If that flag is not set, the clicking on bottom slots is cancelled by default.
     * Can't be used with {@link Flags#CLOSE_ON_BOTTOM_INTERACTION}.
     */
    public static final Flag ALLOW_BOTTOM_INTERACTION = new Flag("allow_bottom_interaction");

    /**
     * Closes the view on the interaction with any bottom slots of the inventory.
     * If that flag is not set, the clicking on bottom slots is cancelled by default.
     * Can't be used with {@link Flags#ALLOW_BOTTOM_INTERACTION}.
     */
    public static final Flag CLOSE_ON_BOTTOM_INTERACTION = new Flag("close_on_bottom_interaction");

    /**
     * Allows the usage of item-dragging within an inventory.
     * It only takes effect if the click allowed. Otherwise, the event ist just cancelled.
     */
    @ApiStatus.Experimental
    public static final Flag ALLOW_ITEM_DRAGGING = new Flag("allow_item_dragging");
    /**
     * Converts the special cursor click (slot = -1) to an outside click (slot = -999).
     * See {@link ViewInventoryPreClickListener} for more information on the implementation.
     * Most of the time, you don't need to set this flag.
     */
    public static final Flag CONVERT_CURSOR_TO_MAGIC_SLOT = new Flag("convert_cursor_to_outside_interaction");
}
