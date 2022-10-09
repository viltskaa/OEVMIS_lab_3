package com.company;

public class Convertor {
  private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static long convertToBase10(String number, int base) {
    if (!checkNumberWithBase(number, base)) return Long.MAX_VALUE;
    long result = 0;
    for (int i = number.length() - 1; i >= 0; i--) {
      result += alphabet.indexOf(
          Character.toUpperCase(number.charAt(i)))
          * Math.pow(base, i);
    }
    return result;
  }

  public static void convertToNewBase(String number, int oldBase, int newBase) {
    if (number.length() == 0) {
      System.out.println("Invalid number");
      return;
    }

    if (oldBase == newBase) return;

    boolean isNegative = false;
    if (number.charAt(0) == '-') {
      isNegative = true;
      number = number.substring(1);
    }

    StringBuilder result = new StringBuilder();
    long numberWithBase10 = oldBase == 10 ? (long) Integer.parseInt(number) : convertToBase10(number, oldBase);
    if (numberWithBase10 == Long.MAX_VALUE) {
      int error = findError(number, oldBase);
      System.out.println(
          number + "\n"
              + "\u001B[31m" + new String(new char[error]).replace("\0", " ") + "\u001B[0m" + "^\n"
              + "Invalid character " + number.charAt(error) + " with base " + oldBase
      );
      return;
    }
    while (numberWithBase10 > 0 && newBase != 10) {
      result.insert(0, alphabet.charAt((int) (numberWithBase10 % newBase)));
      numberWithBase10 /= newBase;
    }
    if (newBase == 10) result = new StringBuilder(
        String.valueOf(numberWithBase10)
    );
    PrintResult(String.valueOf(result), isNegative, newBase);
  }

  public static String ConvertToBinaryCode(String number, int base) {
    if (base == 2) return  number;

    StringBuilder result = new StringBuilder();
    long number10 = base != 10 ? convertToBase10(number, base) : Integer.parseInt(number);
    while (number10 > 0) {
      result.insert(0, number10 % 2);
      number10 /= 2;
    }
    return String.valueOf(result);
  }

  public static void PrintResult(String number, boolean isNegative, int base) {
    System.out.println((isNegative ? "-" : "") + number);
    if (base == 2) System.out.println(secondFormBinary(number, isNegative));
  }

  private static String secondFormBinary(String number, boolean isNegative) {
    int sizeOfChunk = 4,
        newLength = number.length() + sizeOfChunk - number.length() % sizeOfChunk,
        spaceIndex = sizeOfChunk;
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < newLength; i++) {
      if (i >= number.length()) {
        if (i == newLength - 1) result.insert(0, isNegative ? "1" : "0");
        else {
          if (i % 5 == spaceIndex) {
            result.insert(0, " ");
            spaceIndex--;
            if (spaceIndex < 0) spaceIndex = sizeOfChunk;
          }
          result.insert(0, "0");
        }
      } else {
        if (i % 5 == spaceIndex) {
          result.insert(0, " ");
          spaceIndex--;
          if (spaceIndex < 0) spaceIndex = sizeOfChunk;
        }
        result.insert(0, number.charAt(number.length() - i - 1));
      }
    }

    return "ПК " + result;
  }

  private static boolean checkNumberWithBase(String number, int base) {
    for (char digit : number.toCharArray()) {
      if (alphabet.indexOf(digit) >= base) return false;
    }
    return true;
  }

  private static int findError(String number, int base) {
    for (int i = 0; i < number.length(); i++)
      if (alphabet.indexOf(number.charAt(i)) >= base) return i;
    return 0;
  }
}
