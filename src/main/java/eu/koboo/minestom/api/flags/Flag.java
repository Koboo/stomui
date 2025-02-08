package eu.koboo.minestom.api.flags;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record Flag(String name) {

    public static @NotNull Flag of(@NotNull String name) {
        if (name.isEmpty()) {
            throw new NullPointerException("Name must not be empty");
        }
        return new Flag(name.toLowerCase(Locale.ROOT));
    }
}
