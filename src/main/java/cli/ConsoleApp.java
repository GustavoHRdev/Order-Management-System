package cli;

import java.util.Map;

public class ConsoleApp {

    private final InputReader inputReader;
    private final Map<Integer, MenuItem> menu;
    private boolean running = true;

    public ConsoleApp(InputReader inputReader, Map<Integer, MenuItem> menuItems) {
        this.inputReader = inputReader;
        this.menu = menuItems;
    }

    public void run() {
        while (running) {
            printMenu();
            int opcao = readOption();

            MenuItem item = menu.get(opcao);
            if (item == null) {
                System.out.println("Opção inválida!");
                continue;
            }

            item.getCommand().execute();
        }
    }

    public void stop() {
        running = false;
    }

    private void printMenu() {
        System.out.println();
        for (Map.Entry<Integer, MenuItem> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().getLabel());
        }
        System.out.print("Escolha uma opção: ");
    }

    private int readOption() {
        return inputReader.readInt(null);
    }
}
