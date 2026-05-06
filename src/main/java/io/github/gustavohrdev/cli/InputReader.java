package io.github.gustavohrdev.cli;

import java.util.Scanner;

public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        while (true) {
            if (prompt != null && !prompt.isBlank()) {
                System.out.println(prompt);
            }

            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("Entrada inválida!");
                scanner.nextLine();
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            if (prompt != null && !prompt.isBlank()) {
                System.out.println(prompt);
            }

            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("Entrada inválida!");
                scanner.nextLine();
            }
        }
    }

    public String readLine(String prompt) {
        if (prompt != null && !prompt.isBlank()) {
            System.out.println(prompt);
        }

        return scanner.nextLine();
    }
}

