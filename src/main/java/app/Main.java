package app;

import cli.ConsoleApp;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(OrderManagementApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
             Scanner scanner = new Scanner(System.in)) {
            ConsoleAppFactory consoleAppFactory = context.getBean(ConsoleAppFactory.class);
            ConsoleApp app = consoleAppFactory.create(scanner);
            app.run();
        }
    }
}
