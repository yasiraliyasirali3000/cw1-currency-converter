public class CurrencyConverter {
    public static void main(String[] args) {
        // Handle missing arguments
        if (args.length < 2) {
            System.out.println("Usage: java CurrencyConverter <amount> <currency>");
            return;
        }

        double amount = 0.0;
       try {
    amount = Double.parseDouble(args[0]);
    currency = args[1];
} catch (NumberFormatException e) {
    try {
        amount = Double.parseDouble(args[1]);
        currency = args[0];
    } catch (Exception ex) {
        System.out.println("Error: Amount must be a number.");
        return;
    }
}


        // Normalize currency input (case-insensitive)
        String currency = args[1].toLowerCase();

        switch (currency) {
            case "dollars":
                System.out.println(amount + " Dollars = " + (amount * 0.74) + " Pounds");
                System.out.println(amount + " Dollars = " + (amount * 0.88) + " Euros");
                break;

            case "pounds":
                System.out.println(amount + " Pounds = " + (amount * 1.36) + " Dollars");
                System.out.println(amount + " Pounds = " + (amount * 1.19) + " Euros");
                break;

            case "euros":
                System.out.println(amount + " Euros = " + (amount * 1.13) + " Dollars");
                System.out.println(amount + " Euros = " + (amount * 0.84) + " Pounds");
                break;

            default:
                System.out.println("Unknown currency. Use dollars, pounds, or euros.");
        }

        System.out.println("Thank you for using the converter.");
    }
}


