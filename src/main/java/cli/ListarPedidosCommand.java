package cli;

import model.Pedido;
import service.PedidoService;

import java.util.List;

public class ListarPedidosCommand implements Command {

    private final PedidoService pedidoService;

    public ListarPedidosCommand(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute() {
        List<Pedido> pedidos = pedidoService.listarPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
        } else {
            System.out.println("\nPedidos:");
            for (Pedido pedido : pedidos) {
                System.out.println(pedido);
            }
        }
    }
}
