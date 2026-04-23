package app;

import cli.ConsoleApp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleApp app = new ConsoleAppFactory().create(scanner);
            app.run();
        }
    }
}
