package com.company;

import java.util.Objects;

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

        StringBuilder result = new StringBuilder(
            code.charAt(0) == '-' ? code.substring(1) : code
        );
        for (int i = length(code); i < newLength - 1; i++) result.insert(0, "0");
        return (code.charAt(0) == '-' ? "1" : "0") + result;
    }

    public static String CompleteToDirect(String code) {
        int newLength = code.length() + 4 - code.length() % 4;
        if (newLength < 8) newLength = 8;

        StringBuilder result = new StringBuilder(code.substring(1));
        for (int i = length(code); i < newLength - 1; i++) result.insert(0, "0");
        return String.valueOf(result);
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
                : ConvertToDirectCode(additionResult);
    }

    public static String Subtraction(String n1, String n2) {
        return Addition(n1, "-"+n2);
    }

    public static String Multiplication(String n1, String n2) {
        int maxLength = Math.max(length(n1), length(n2));
        int sign = 0;
        if (n1.charAt(0) == '-') {
            sign = 1;
        }
        if (n2.charAt(0) == '-') {
            sign = 1;
        }
        if (n1.charAt(0) == '-' && n2.charAt(0) == '-') {
            sign = 0;
        }
        n1 = ConvertToAdditionalCode(
            ConvertToDirectCode(n1, maxLength)
        );
        n2 = ConvertToAdditionalCode(
            ConvertToDirectCode(n2, maxLength)
        );

        String result = "0";
        int length = n1.length();
        for (int i = n2.length() - 1; i >= 0; i--) {
            if (n2.charAt(i) == '1') {
                while (n1.length() != length + n2.length() - 1 - i)
                    n1 += "0";
                result = Addition(n1, String.valueOf(result));
            }
        }

        result = StripZerosFromStart(result);
        return sign == 1
            ? binaryAddition(ConvertToBackCode(result), "1").substring(1)
            : StripZerosFromStart(result);
    }

    public static String Division(String n1, String n2) {
        if (CompareForDivision(n1, n2) == 2) return "0";
        String result = "0";
        int sign = 0;
        if (n1.charAt(0) == '-') {
            sign = 1;
        }
        if (n2.charAt(0) == '-') {
            sign = 1;
        }
        if (n1.charAt(0) == '-' && n2.charAt(0) == '-') {
            sign = 0;
        }
        n1 = ConvertToAdditionalCode(ConvertToDirectCode(n1));
        n2 = ConvertToAdditionalCode(ConvertToDirectCode(n2));
        n1 = StripZerosFromStart(n1);
        n2 = StripZerosFromStart(n2);

        int N = n2.length();
        while (CompareForDivision(n1, n2) != 2) {
            String temp = n1.substring(0, N);
            if (CompareForDivision(temp, n2) == 2) {
                N++;
                result += "0";
            }
            else {
                temp = Subtraction(temp, n2);
                n1 = (OnlyNull(temp) ? "" : StripZerosFromStart(temp)) + n1.substring(N);
                result += "1";

                if (OnlyNull(n1)) {
                    result += n1;
                    break;
                }
            }
        }
        if (sign == 1) {
            return binaryAddition(ConvertToBackCode(result.substring(1)), "1");
        }
        return ConvertToDirectCode(result);
    }

    private static boolean OnlyNull(String number) {
        int NullCount = 0;
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '0') NullCount++;
        }
        return NullCount == number.length();
    }

    private static String StripZerosFromStart(String number) {
        if (number.length() == 1) return number;
        int firstOne = number.indexOf("1");
        return number.substring(
            firstOne != -1 ? firstOne : 0
        );
    }

    private static int CompareForDivision(String n1, String n2) {
        if (n1.length() > n2.length()) return 1;
        if (n1.length() < n2.length()) return 2;

        for (int i = 0; i < n1.length(); i++) {
            if (n1.charAt(i) != n2.charAt(i)) return n1.charAt(i) == '1' ? 1 : 2;
        }
        return 0;
    }

    private static int length(String n1) {
        return n1.charAt(0) == '-' ? n1.length() - 1 : n1.length();
    }

    public static String fromDirectCodeToBinary(String number) {
        String sign = number.charAt(0) == '1' ? "-" : "";
        number = StripZerosFromStart(number.substring(1));
        return sign + number;
    }
}
