package io.github.gustavohrdev.repository;

import io.github.gustavohrdev.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClienteRepository implements ClienteRepository {

    private final List<Cliente> clientes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void salvar(Cliente cliente) {
        if (cliente.getId() == 0) {
            cliente.setId(nextId++);
        }
        clientes.add(cliente);
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(clientes);
    }

    @Override
    public java.util.Optional<Cliente> buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return java.util.Optional.of(cliente);
            }
        }
        return java.util.Optional.empty();
    }

    @Override
    public boolean estaVazio() {
        return clientes.isEmpty();
    }
}

