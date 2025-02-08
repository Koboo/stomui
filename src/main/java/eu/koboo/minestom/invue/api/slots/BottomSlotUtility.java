package eu.koboo.minestom.invue.api.slots;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BottomSlotUtility {

    // Expected conversionSlot input:
    // 1 row  = 0  -  8 -> +  9 = 17
    // 2 row  = 9  - 17 -> +  9 = 26
    // 3 row  = 18 - 26 -> +  9 = 35
    // hotbar = 27 - 35 -> - 27 = 8
    public int denormalizeBottomSlot(int conversionSlot) {
        if (conversionSlot >= 27) {
            return conversionSlot - 27;
        }
        return conversionSlot + 9;
    }

    // Expected conversionSlot input:
    // 1 row  = 9  - 17 -> -  9 = 8
    // 2 row  = 18 - 26 -> -  9 = 17
    // 3 row  = 27 - 35 -> -  9 = 26
    // hotbar = 0  -  8 -> + 27 = 35
    public int normalizeBottomSlot(int conversionSlot) {
        if (conversionSlot >= 9) {
            return conversionSlot - 9;
        }
        return conversionSlot + 27;
    }
}
