package eu.koboo.minestom.invue.api.utils;

import eu.koboo.minestom.invue.api.PlayerView;
import eu.koboo.minestom.invue.api.component.ViewComponent;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

/**
 * This class is used to generate string ids, with a specific length.
 * It's used to specifically generate ids for:
 * - {@link ViewComponent}
 * - {@link PlayerView}
 */
@UtilityClass
public class IdGenerator {

    /**
     * The possible characters, which are used to generate a new id.
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * The {@link SecureRandom} instance to genrate new string ids.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates random string id, with a given length.
     *
     * @param idLength the id length
     * @return the string id
     */
    public String generateRandomString(int idLength) {
        StringBuilder idBuilder = new StringBuilder(idLength);
        for (int i = 0; i < idLength; i++) {
            int characterIndex = SECURE_RANDOM.nextInt(CHARACTERS.length());
            idBuilder.append(CHARACTERS.charAt(characterIndex));
        }
        return idBuilder.toString();
    }
}
