
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CurrencyConverterTest {

    // Helper: run main() and capture stdout/stderr as a single String
    private String runMainCapturingOutput(String... args) {
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
        try {
            // If your program throws, test will fail (good for current broken behavior)
            assertDoesNotThrow(() -> CurrencyConverter.main(args),
                "Program should not crash/throw for this input");
        } finally {
            System.setOut(origOut);
            System.setErr(origErr);
        }
        return out.toString() + err.toString();
    }

    // Helper: find a numeric value before a target currency in output (case-insensitive)
    private Double findValue(String output, String targetCurrency) {
        Pattern p = Pattern.compile("=\\s*([0-9]+(?:\\.[0-9]+)?)\\s+" + targetCurrency,
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(output);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return null;
    }

    // ---------- (a) INPUT VALIDATION TESTS ----------

    @Test
    @DisplayName("a1) No arguments -> should show usage/help and exit gracefully")
    void noArgsShowsUsage() {
        String out = runMainCapturingOutput();
        String lower = out.toLowerCase();
        // Define the contract you want after fix: must show 'usage' or 'invalid'
        assertTrue(lower.contains("usage") || lower.contains("invalid") || lower.contains("provide"),
                "Should show a helpful usage/invalid input message");
    }

    @Test
    @DisplayName("a2) Wrong order (currency then amount) -> helpful message, no crash")
    void wrongOrderShowsHelpfulMessage() {
        String out = runMainCapturingOutput("dollars", "50");
        String lower = out.toLowerCase();
        assertTrue(lower.contains("usage") || lower.contains("order") || lower.contains("provide"),
                "Should inform correct format, e.g., <amount> <currency>");
    }

    @Test
    @DisplayName("a3) Non-numeric amount -> helpful message, no crash")
    void nonNumericAmountHandled() {
        String out = runMainCapturingOutput("abc", "dollars");
        String lower = out.toLowerCase();
        assertTrue(lower.contains("invalid") || lower.contains("number") || lower.contains("usage"),
                "Should inform amount must be a valid number");
    }

    // ---------- (b) CASE-INSENSITIVITY TESTS ----------

    @Test
    @DisplayName("b1) Lowercase 'euros' should work")
    void lowercaseEurosWorks() {
        String out = runMainCapturingOutput("1", "euros");
        // Expect some conversion lines present
        assertTrue(out.toLowerCase().contains("dollars") || out.toLowerCase().contains("pounds"),
                "Should print conversions from Euros to Dollars/Pounds");
    }

    @Test
    @DisplayName("b2) UPPERCASE 'POUNDS' should work (case-insensitive)")
    void uppercasePoundsWorks() {
        String out = runMainCapturingOutput("1", "POUNDS");
        assertTrue(out.toLowerCase().contains("dollars") || out.toLowerCase().contains("euros"),
                "Should accept uppercase currency and show conversions");
    }

    @Test
    @DisplayName("b3) Capitalized 'Dollars' should work (case-insensitive)")
    void capitalizedDollarsWorks() {
        String out = runMainCapturingOutput("1", "Dollars");
        assertTrue(out.toLowerCase().contains("pounds") || out.toLowerCase().contains("euros"),
                "Should accept capitalized currency and show conversions");
    }

    // ---------- (c) CONVERSION RATE ACCURACY TESTS ----------
    // Given:
    // 1 Dollar = 0.74 Pounds, 0.88 Euros
    // 1 Pound  = 1.36 Dollars, 1.19 Euros
    // 1 Euro   = 1.13 Dollars, 0.84 Pounds

    @Test
    @DisplayName("c1) 1 Dollar -> 0.74 Pounds & 0.88 Euros")
    void dollarToPoundsEuros() {
        String out = runMainCapturingOutput("1", "dollars");
        Double pounds = findValue(out, "Pounds");
        Double euros  = findValue(out, "Euros");
        assertNotNull(pounds, "Should print conversion to Pounds");
        assertNotNull(euros,  "Should print conversion to Euros");
        assertEquals(0.74, pounds, 0.01, "Dollar->Pounds should be 0.74");
        assertEquals(0.88, euros,  0.01, "Dollar->Euros should be 0.88");
    }

    @Test
    @DisplayName("c2) 1 Pound -> 1.36 Dollars & 1.19 Euros")
    void poundToDollarsEuros() {
        String out = runMainCapturingOutput("1", "pounds");
        Double dollars = findValue(out, "Dollars");
        Double euros   = findValue(out, "Euros");
        assertNotNull(dollars, "Should print conversion to Dollars");
        assertNotNull(euros,   "Should print conversion to Euros");
        assertEquals(1.36, dollars, 0.01, "Pound->Dollars should be 1.36");
        assertEquals(1.19, euros,   0.01, "Pound->Euros should be 1.19");
    }

    @Test
    @DisplayName("c3) 1 Euro -> 1.13 Dollars & 0.84 Pounds")
    void euroToDollarsPounds() {
        String out = runMainCapturingOutput("1", "euros");
        Double dollars = findValue(out, "Dollars");
        Double pounds  = findValue(out, "Pounds");
        assertNotNull(dollars, "Should print conversion to Dollars");
        assertNotNull(pounds,  "Should print conversion to Pounds");
        assertEquals(1.13, dollars, 0.01, "Euro->Dollars should be 1.13");
        assertEquals(0.84, pounds,  0.01, "Euro->Pounds should be 0.84");
    }
}
