package eu.koboo.minestom.stomui.api.slots;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents the row and column pair of a specific slot within an inventory.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Position {

    int row;
    int column;

    /**
     * Creates a new instance of {@link Position}.
     *
     * @param row    The row within the inventory, starts at 0.
     * @param column The column within the inventory, starts at 0.
     * @return A new instance of {@link Position}.
     */
    public static Position of(int row, int column) {
        return new Position(row, column);
    }
}
