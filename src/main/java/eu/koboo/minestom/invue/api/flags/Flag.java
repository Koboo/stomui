package eu.koboo.minestom.invue.api.flags;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.ViewBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * This class represents an instance of a specific {@link Flag}.
 * These flags can be passed into a {@link ViewBuilder}, using the method
 * {@link ViewBuilder#withFlags(Flag...)}.
 * The added {@link Flag} get copied to the {@link PlayerView} of the
 * {@link ViewBuilder} and can still be modified using these methods:
 * - {@link PlayerView#addFlags(Flag...)}
 * - {@link PlayerView#hasFlags(Flag...)}
 * - {@link PlayerView#removeFlags(Flag...)}
 *
 * @param name
 */
public record Flag(String name) {

    /**
     * Defines the regex pattern for {@link Flag} names.
     */
    private static final Pattern FLAG_REGEX_PATTERN = Pattern.compile("^([a-z_-]){3,}$");

    /**
     * Creates a new {@link Flag} instance using the given name.
     * The name needs to
     * - be 3 or more characters long
     * - only contain lowercase letters, "_" or "-" symbols
     *
     * @param name The name of the created {@link Flag}
     * @return A new instance of {@link Flag}
     */
    public static @NotNull Flag of(@NotNull String name) {
        if (!FLAG_REGEX_PATTERN.matcher(name).matches()) {
            throw new NullPointerException("Flag name doesnt match expected pattern!");
        }
        return new Flag(name.toLowerCase(Locale.ROOT));
    }
}
