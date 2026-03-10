package model;

public class ItemPedido {

    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "produto=" + produto.getNome() +
                ", quantidade=" + quantidade +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}