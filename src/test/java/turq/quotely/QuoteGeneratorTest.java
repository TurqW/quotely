package turq.quotely;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class QuoteGeneratorTest {

    private final static String VALID_QUOTE = "This is a valid quote! (Turq Whiteside)";
    HttpClient baseMockClient = mock(HttpClient.class);
    HttpResponse mockGoodResult = mock(HttpResponse.class);

    @BeforeEach
    public void setup() {
        when(baseMockClient.sendAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(mockGoodResult));
        when(mockGoodResult.statusCode()).thenReturn(200);
        when(mockGoodResult.body()).thenReturn(VALID_QUOTE);
    }

    @Test
    public void getQuoteEnglishHappy() {
        QuoteGenerator gen = new QuoteGenerator(baseMockClient);
        assertEquals(VALID_QUOTE, gen.getQuote("en"));
    }

    @Test
    public void getQuoteRussianHappy() {
        QuoteGenerator gen = new QuoteGenerator(baseMockClient);
        assertEquals(VALID_QUOTE, gen.getQuote("ru"));
    }

    @Test
    public void getQuoteNoSpanishAvailable() {
        HttpResponse badResult = mock(HttpResponse.class);
        HttpClient brokenClient = mock(HttpClient.class);
        when(brokenClient.sendAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(badResult));
        when(badResult.body()).thenReturn("Error: wrong language param");
        QuoteGenerator gen = new QuoteGenerator(brokenClient);
        assertThrows(Exception.class, () -> gen.getQuote("es"));
    }

    @Test
    public void retries() {
        HttpResponse badResult = mock(HttpResponse.class);
        HttpClient partlyBrokenClient = mock(HttpClient.class);
        when(partlyBrokenClient.sendAsync(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(badResult))
                .thenReturn(CompletableFuture.completedFuture(mockGoodResult));
        when(badResult.statusCode()).thenReturn(408);
        QuoteGenerator gen = new QuoteGenerator(partlyBrokenClient);
        assertEquals(VALID_QUOTE, gen.getQuote("en"));
    }
}