package cli;

import model.Cliente;
import service.ClienteService;

import java.util.List;

public class ListarClientesCommand implements Command {

    private final ClienteService clienteService;

    public ListarClientesCommand(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public void execute() {
        List<Cliente> listaClientes = clienteService.listarClientes();

        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            System.out.println("\nClientes:");
            for (Cliente cliente : listaClientes) {
                System.out.println(cliente);
            }
        }
    }
}
