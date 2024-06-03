package turq.quotely;

import turq.quotely.validation.InputValidator;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        String language = InputValidator.validateInput(args);
        QuoteGenerator gen = new QuoteGenerator(HttpClient.newHttpClient());
        System.out.println(gen.getQuote(language));
    }
}