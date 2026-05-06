package io.github.gustavohrdev.model;

public class Produto {

    private int id;
    private final String nome;
    private final double preco;

    public Produto(String nome, double preco) {
        this(0, nome, preco);
    }

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("ID do produto já foi definido.");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "Produto{id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}

