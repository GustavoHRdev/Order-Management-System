package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private static int contador = 1;

    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private StatusPedido status;
    public StatusPedido getStatus() {
        return status;
    }

    public Pedido(Cliente cliente) {
        this.id = contador++;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = StatusPedido.PENDENTE;
    }


    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

    @Override
    public String toString() {
        return "Pedido{id=" + id +
                ", cliente=" + cliente.getNome() +
                ", status=" + status +
                ", total=" + calcularTotal() +
                '}';
    }
}