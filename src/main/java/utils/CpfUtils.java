package main.java.utils;

public class CpfUtils {
    private CpfUtils() {}

    public static Boolean isValid(String cpf) {
        String cleaned = clean(cpf);

        if (cleaned.length() != 11 || cleaned.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (cleaned.charAt(i) - '0') * (10 - i);
            }
            int firstDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cleaned.charAt(i) - '0') * (11 - i);
            }
            int secondDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

            return (cleaned.charAt(9) - '0' == firstDigit) &&
                    (cleaned.charAt(10) - '0' == secondDigit);
        } catch (Exception e) {
            return false;
        }
    }

    public static String format(String cpf) {
        if (!isValid(cpf)) return "";

        String cleaned = clean(cpf);
        return String.format("%s.%s.%s-%s",
            cleaned.substring(0, 3),
            cleaned.substring(3, 6),
            cleaned.substring(6, 9),
            cleaned.substring(9));
    }

    public static String clean(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("[^0-9]", "");
    }
}
