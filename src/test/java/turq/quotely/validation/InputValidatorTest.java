package turq.quotely.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputValidatorTest {
    @Test
    public void noArgs() {
        String[] args = new String[0];
        assertEquals("en", InputValidator.validateInput(args));
    }

    @Test
    public void validLanguage() {
        String[] args = new String[]{"Russian"};
        assertEquals("ru", InputValidator.validateInput(args));
    }

    @Test
    public void decapitalizes() {
        String[] args = new String[]{"eNgLiSh"};
        assertEquals("en", InputValidator.validateInput(args));
    }

    @Test
    public void invalidLanguage() {
        String[] args = new String[]{"nederlands"};
        assertThrows(InvalidInputException.class, () -> InputValidator.validateInput(args));
    }

    @Test
    public void tooManyArgs() {
        String[] args = new String[]{"english", "russian"};
        assertThrows(InvalidInputException.class, () -> InputValidator.validateInput(args));
    }
}