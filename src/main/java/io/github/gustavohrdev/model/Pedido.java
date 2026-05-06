package io.github.gustavohrdev.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {

    private int id;
    private final Cliente cliente;
    private final List<ItemPedido> itens;
    private StatusPedido status;

    public int getId() {
        return id;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public boolean temItens() {
        return !itens.isEmpty();
    }

    public Pedido(Cliente cliente) {
        this(0, cliente, new ArrayList<>(), StatusPedido.PENDENTE);
    }

    public Pedido(int id, Cliente cliente, List<ItemPedido> itens, StatusPedido status) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);
        this.status = status;
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

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("ID do pedido já foi definido.");
        }
        this.id = id;
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

