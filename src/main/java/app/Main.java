package app;

import cli.ConsoleApp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ApplicationContext applicationContext = ApplicationContext.createDefault();
            ConsoleApp app = new ConsoleAppFactory().create(applicationContext, scanner);
            app.run();
        }
    }
}
