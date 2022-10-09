package com.company;

public class BinaryMath {
    public static String ConvertToBackCode(String number) {
        char sign = number.charAt(0);
        number = number.substring(1);
        if (sign == '1') {
            number = number.replaceAll("0", "2");
            number = number.replaceAll("1", "0");
            number = number.replaceAll("2", "1");
        }
        return sign + number;
    }

    public static String ConvertToAdditionalCode(String number) {
        if (!number.startsWith("0")) number = binaryAddition(ConvertToBackCode(number), "1");
        return number;
    }

    public static String binaryAddition(String n1, String n2) {
        if (n1 == null || n2 == null) return "";
        int first = n1.length() - 1;
        int second = n2.length() - 1;
        StringBuilder result = new StringBuilder();
        int carry = 0;
        while (first >= 0 || second >= 0) {
            int sum = carry;
            if (first >= 0) {
                sum += n1.charAt(first) - '0';
                first--;
            }
            if (second >= 0) {
                sum += n2.charAt(second) - '0';
                second--;
            }
            carry = sum >> 1;
            sum = sum & 1;
            result.append(sum == 0 ? '0' : '1');
        }
        if (carry > 0) result.append('1');

        result.reverse();
        return String.valueOf(result);
    }

    public static String ConvertToDirectCode(String code, int maxLength) {
        int newLength = maxLength + 4 - maxLength % 4;

        if (newLength < 8) newLength = 8;

        StringBuilder result = new StringBuilder(
                code.charAt(0) == '-' ? code.substring(1) : code
        );
        for (int i = length(code); i < newLength - 1; i++) result.insert(0, "0");
        return (code.charAt(0) == '-' ? "1" : "0") + result;
    }

    public static String ConvertToDirectCode(String code) {
        int newLength = code.length() + 4 - code.length() % 4;

        if (newLength < 8) newLength = 8;

        StringBuilder result = new StringBuilder(code);
        for (int i = length(code); i < newLength - 1; i++) result.insert(0, "0");
        return (code.charAt(0) == '-' ? "1" : "0") + result;
    }

    public static String Addition(String n1, String n2) {
        int maxLength = Math.max(length(n1), length(n2));
        n1 = ConvertToAdditionalCode(
                ConvertToDirectCode(n1, maxLength)
        );
        n2 = ConvertToAdditionalCode(
                ConvertToDirectCode(n2, maxLength)
        );

        String additionResult = binaryAddition(n1, n2);
        additionResult = additionResult.length() > n1.length()
                ? additionResult.substring(1)
                : additionResult;
        return additionResult.charAt(0) == '1'
                ? binaryAddition(ConvertToBackCode(additionResult), "1")
                : additionResult;
    }

    public static String Subtraction(String n1, String n2) {
        return Addition(n1, "-"+n2);
    }

    public static String Multiplication(String n1, String n2) {
        String result = "";
        int length = n1.length();
        for (int i = n2.length() - 1; i >= 0; i--) {
            if (n2.charAt(i) == '1') {
                while (n1.length() != length + n2.length() - 1 - i)
                    n1 += "0";
                result = binaryAddition(n1, String.valueOf(result));
            }
        }
        return ConvertToDirectCode(result);
    }

    public static String Remainder(String n1, String n2) {
        while (CompareForDivision(n1, n2)) {
            n1 = Subtraction(n1, n2);
        }
        return n1;
    }

    public static String Division(String n1, String n2) {
        String result = "0";
        while (CompareForDivision(n1, n2)) {
            n1 = Subtraction(n1, n2);
            result = binaryAddition(result, "1");
        }
        return ConvertToDirectCode(result);
    }

    private static boolean CompareForDivision(String n1, String n2) {
        return Convertor.convertToBase10(n1, 2) >= Convertor.convertToBase10(n2, 2);
    }

    private static int length(String n1) {
        return n1.charAt(0) == '-' ? n1.length() - 1 : n1.length();
    }
}
