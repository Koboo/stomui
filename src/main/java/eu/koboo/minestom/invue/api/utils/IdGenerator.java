package eu.koboo.minestom.invue.api.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class IdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Integer DEFAULT_ID_LENGTH = 10;

    public String generateRandomString() {
        return generateRandomString(DEFAULT_ID_LENGTH);
    }

    public String generateRandomString(int idLength) {
        StringBuilder idBuilder = new StringBuilder(idLength);
        for (int i = 0; i < idLength; i++) {
            int characterIndex = SECURE_RANDOM.nextInt(CHARACTERS.length());
            idBuilder.append(CHARACTERS.charAt(characterIndex));
        }
        return idBuilder.toString();
    }
}
