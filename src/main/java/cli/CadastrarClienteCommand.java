package cli;

import service.ClienteService;

import java.util.Scanner;

public class CadastrarClienteCommand implements Command {

    private final Scanner scanner;
    private final ClienteService clienteService;

    public CadastrarClienteCommand(Scanner scanner, ClienteService clienteService) {
        this.scanner = scanner;
        this.clienteService = clienteService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Digite o nome:");
            String nome = scanner.nextLine();

            System.out.println("Digite o email:");
            String email = scanner.nextLine();

            clienteService.cadastrarCliente(nome, email);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
