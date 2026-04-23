package cli;

import service.ClienteService;

public class CadastrarClienteCommand implements Command {

    private final InputReader inputReader;
    private final ClienteService clienteService;

    public CadastrarClienteCommand(InputReader inputReader, ClienteService clienteService) {
        this.inputReader = inputReader;
        this.clienteService = clienteService;
    }

    @Override
    public void execute() {
        try {
            String nome = inputReader.readLine("Digite o nome:");
            String email = inputReader.readLine("Digite o email:");

            clienteService.cadastrarCliente(nome, email);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
