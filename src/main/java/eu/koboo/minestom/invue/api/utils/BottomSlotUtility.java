package eu.koboo.minestom.invue.api.utils;

import lombok.experimental.UtilityClass;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

/**
 * This utility class is used to convert the bottom slots, provided by {@link InventoryPreClickEvent}
 * to raw slots, which define the slot size using both inventory slots.
 */
@UtilityClass
public class BottomSlotUtility {

    /**
     * Denormalize bottom slot int.
     * <p>
     * Expected conversionSlot input:
     * 1 row  = 0  -  8 -> +  9 = 17
     * 2 row  = 9  - 17 -> +  9 = 26
     * 3 row  = 18 - 26 -> +  9 = 35
     * hotbar = 27 - 35 -> - 27 = 8
     * <p>
     * @param conversionSlot the conversion slot
     * @return the int
     */
    public int denormalizeBottomSlot(int conversionSlot) {
        if (conversionSlot >= 27) {
            return conversionSlot - 27;
        }
        return conversionSlot + 9;
    }

    /**
     * Normalize bottom slot int.
     * <p>
     * Expected conversionSlot input:
     * 1 row  = 9  - 17 -> -  9 = 8
     * 2 row  = 18 - 26 -> -  9 = 17
     * 3 row  = 27 - 35 -> -  9 = 26
     * hotbar = 0  -  8 -> + 27 = 35
     * <p>
     * @param conversionSlot the conversion slot
     * @return the int
     */
    public int normalizeBottomSlot(int conversionSlot) {
        if (conversionSlot >= 9) {
            return conversionSlot - 9;
        }
        return conversionSlot + 27;
    }
}
