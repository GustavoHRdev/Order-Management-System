package cli;

import model.Pedido;
import model.StatusPedido;
import service.PedidoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtualizarStatusCommand implements Command {

    private final InputReader inputReader;
    private final PedidoService pedidoService;

    public AtualizarStatusCommand(InputReader inputReader, PedidoService pedidoService) {
        this.inputReader = inputReader;
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute() {
        try {
            List<Pedido> pedidosLista = pedidoService.listarPedidos();

            if (pedidosLista.isEmpty()) {
                System.out.println("Nenhum pedido registrado.");
                return;
            }

            System.out.println("\nPedidos:");
            for (Pedido pedido : pedidosLista) {
                System.out.println(pedido.getId() + " - " + pedido);
            }

            int pedidoId = inputReader.readInt("Digite o ID do pedido:");

            System.out.println("\nEscolha o status:");
            System.out.println("1 - PENDENTE");
            System.out.println("2 - PROCESSANDO");
            System.out.println("3 - ENVIADO");
            System.out.println("4 - ENTREGUE");
            System.out.println("5 - CANCELADO");

            int statusOpcao = inputReader.readInt(null);

            Map<Integer, StatusPedido> statusMap = new HashMap<>();
            statusMap.put(1, StatusPedido.PENDENTE);
            statusMap.put(2, StatusPedido.PROCESSANDO);
            statusMap.put(3, StatusPedido.ENVIADO);
            statusMap.put(4, StatusPedido.ENTREGUE);
            statusMap.put(5, StatusPedido.CANCELADO);

            StatusPedido status = statusMap.get(statusOpcao);
            if (status == null) {
                System.out.println("Status inválido!");
                return;
            }

            pedidoService.atualizarStatus(pedidoId, status);
            System.out.println("Status atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
