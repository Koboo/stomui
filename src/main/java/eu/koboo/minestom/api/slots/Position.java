package eu.koboo.minestom.api.slots;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Position {

    int row;
    int column;

    public static Position of(int row, int column) {
        return new Position(row, column);
    }
}
