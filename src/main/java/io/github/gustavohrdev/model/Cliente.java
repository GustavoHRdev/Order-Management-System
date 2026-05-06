package io.github.gustavohrdev.model;

public class Cliente {

    private int id;
    private final String nome;
    private final String email;

    public Cliente(String nome, String email) {
        this(0, nome, email);
    }

    public Cliente(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("ID do cliente já foi definido.");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

