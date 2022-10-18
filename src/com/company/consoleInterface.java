package com.company;

import java.util.Scanner;

public class consoleInterface {
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean startCommand() {
        System.out.println("Enter <old base> <new base> <number> and press enter");
        String line = scanner.nextLine();
        int oldBase, newBase;
        if (line.equals("q")) return false;
        String[] arg = line.split(" ");

        if (arg.length != 3) {
            System.out.println("Error: invalid input");
            return true;
        }

        try {
            oldBase = Integer.parseInt(arg[0]);
            newBase = Integer.parseInt(arg[1]);
        }
        catch (Exception e) {
            System.out.println("Error: invalid input" + "\n" + e);
            return true;
        }

        if (oldBase < 2 || oldBase > 16 || newBase < 2 || newBase > 16) {
            System.out.println("Error: invalid input");
            return true;
        }

        Convertor.convertToNewBase(arg[2], oldBase, newBase);
        return true;
    }

    public static boolean startCommandBinary() {
        System.out.println("Enter <base> <number> <number> <math operation> and press enter");
        String line = scanner.nextLine();
        String result = "";
        int base;
        if (line.equals("q")) return false;
        String[] arg = line.split(" ");

        if (arg.length != 4) {
            System.out.println("Error: invalid input");
            return true;
        }

        try {
            base = Integer.parseInt(arg[0]);
        }
        catch (Exception e) {
            System.out.println("Error: invalid base" + "\n" + e);
            return true;
        }

        if (base < 2 || base > 16) {
            System.out.println("Error: invalid input");
            return true;
        }
        if (CheckErrorInput(arg[1], base) && CheckErrorInput(arg[2], base)) {
            switch (arg[3]) {
                case "+" -> result = BinaryMath.Addition(
                    Convertor.ConvertToBinaryCode(arg[1], base),
                    Convertor.ConvertToBinaryCode(arg[2], base)
                );
                case "-" -> result = BinaryMath.Subtraction(
                    Convertor.ConvertToBinaryCode(arg[1], base),
                    Convertor.ConvertToBinaryCode(arg[2], base)
                );
                case "*" -> result = BinaryMath.Multiplication(
                    Convertor.ConvertToBinaryCode(arg[1], base),
                    Convertor.ConvertToBinaryCode(arg[2], base)
                );
                case "/" -> result = BinaryMath.Division(
                    Convertor.ConvertToBinaryCode(arg[1], base),
                    Convertor.ConvertToBinaryCode(arg[2], base)
                );
                default -> System.out.println("Error: invalid operation");
            }
            System.out.println(result);
        }
        return true;
    }

    public static void LoopCommand() {
        boolean loopActive = true;
        while (loopActive) {
            loopActive = startCommand();
        }
    }

    public static void LoopCommandBinary() {
        boolean loopActive = true;
        while (loopActive) {
            loopActive = startCommandBinary();
        }
    }

    private static boolean CheckErrorInput(String number, int base) {
        int error = findError(number, base);
        if (error == -1) return true;
        System.out.println(
            number + "\n"
                + "\u001B[31m" + new String(new char[error]).replace("\0", " ") + "\u001B[0m" + "^\n"
                + "Invalid character " + number.charAt(error) + " with base " + base
        );
        return false;
    }

    private static int findError(String number, int base) {
        for (int i = 0; i < number.length(); i++)
            if ("0123456789ABCDEF".indexOf(number.charAt(i)) >= base) return i;
        return -1;
    }
}
