package pt.lisomatrix.Sockets.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/***
 * This class takes care of generating random codes
 *
 * TODO PROBABLY NOT GONNA BE USED
 */
@Component
public class RandomCodeGenerator {

    static final private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    final private Random rng = new SecureRandom();

    private char randomChar() {
        return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
    }

    public String randomUUID(int length, int spacing, char spacerChar) {
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while (length > 0) {
            if (spacer == spacing) {
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }
}