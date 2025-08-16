import java.text.DecimalFormat;
import java.util.Locale;

public class CurrencyConverter {

    public static void main(String[] args) {
        DecimalFormat f = new DecimalFormat("##.##");

        // Basic input validation
        if (args == null || args.length < 2) {
            System.out.println("Usage: java CurrencyConverter <amount> <currency>");
            return;
        }

        String amountStr = args[0];
        String currencyStr = args[1];

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount: please provide a numeric value. Usage: java CurrencyConverter <amount> <currency>");
            return;
        }

        String currency = currencyStr.trim().toLowerCase(Locale.ROOT);

        // Exchange rates (as per coursework)
        final double DOLLAR_TO_POUND = 0.74;
        final double DOLLAR_TO_EURO  = 0.88;
        final double POUND_TO_DOLLAR = 1.36;
        final double POUND_TO_EURO   = 1.19;
        final double EURO_TO_DOLLAR  = 1.13;
        final double EURO_TO_POUND   = 0.84;

        double dollar, pound, euro;

        switch (currency) {
            case "dollars":
            case "dollar":
                pound = amount * DOLLAR_TO_POUND;
                System.out.println(amount + " Dollars = " + f.format(pound) + " Pounds");
                euro = amount * DOLLAR_TO_EURO;
                System.out.println(amount + " Dollars = " + f.format(euro) + " Euros");
                break;

            case "pounds":
            case "pound":
                dollar = amount * POUND_TO_DOLLAR;
                System.out.println(amount + " Pounds = " + f.format(dollar) + " Dollars");
                euro = amount * POUND_TO_EURO;
                System.out.println(amount + " Pounds = " + f.format(euro) + " Euros");
                break;

            case "euros":
            case "euro":
                dollar = amount * EURO_TO_DOLLAR;
                System.out.println(amount + " Euros = " + f.format(dollar) + " Dollars");
                pound = amount * EURO_TO_POUND;
                System.out.println(amount + " Euros = " + f.format(pound) + " Pounds");
                break;

            default:
                System.out.println("Invalid currency. Supported: dollars, pounds, euros. Usage: java CurrencyConverter <amount> <currency>");
                return;
        }

        System.out.println("Thank you for using the converter.");
    }
}
