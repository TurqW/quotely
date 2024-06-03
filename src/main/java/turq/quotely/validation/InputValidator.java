package turq.quotely.validation;

public class InputValidator {
    public static String validateInput(final String[] args) {
        if (args.length == 0) {
            // No parameters, easy peasy
            return "en";
        }
        if (args.length > 1) {
            System.out.println("Too many arguments.");
            usage();
        }
        String sanitized = args[0].toLowerCase();
        if (sanitized.equals("english") || sanitized.equals("russian")) {
            // Future upgrade: accept the russian name for russian?
            // Would need to consult with user about whether that's desirable, whether to accept latinized or cyrillic or both, etc
            return sanitized.substring(0, 2);
        }
        System.out.println("Language code not recognized.");
        usage();
        return ""; // Unreachable, "usage" always throws an exception
    }

    private static void usage() {
        System.out.println("Usage: The only allowed argument is an optional language name.");
        System.out.println("Valid languages in this version: \"english\", \"russian\".");
        System.out.println("Language may be omitted to default to English.");
        throw new InvalidInputException();
    }
}
