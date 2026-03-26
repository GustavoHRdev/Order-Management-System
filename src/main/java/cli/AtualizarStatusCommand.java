package cli;

import model.Pedido;
import model.StatusPedido;
import service.PedidoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AtualizarStatusCommand implements Command {

    private final Scanner scanner;
    private final PedidoService pedidoService;

    public AtualizarStatusCommand(Scanner scanner, PedidoService pedidoService) {
        this.scanner = scanner;
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
            for (int i = 0; i < pedidosLista.size(); i++) {
                System.out.println(i + " - " + pedidosLista.get(i));
            }

            int pedidoIndex = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\nEscolha o status:");
            System.out.println("1 - PENDENTE");
            System.out.println("2 - PROCESSANDO");
            System.out.println("3 - ENVIADO");
            System.out.println("4 - ENTREGUE");
            System.out.println("5 - CANCELADO");

            int statusOpcao = scanner.nextInt();
            scanner.nextLine();

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

            pedidoService.atualizarStatus(pedidoIndex, status);
            System.out.println("Status atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
        }
    }
}
