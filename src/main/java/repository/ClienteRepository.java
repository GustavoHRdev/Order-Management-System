package repository;

import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private  final List<Cliente> clientes = new ArrayList<>();

    public void salvar(Cliente cliente){
        clientes.add(cliente);
    }
    public List<Cliente> listar(){
        return new ArrayList<>(clientes);
    }

    public Cliente buscarPorIndex(int index) {
        if (index < 0 || index >= clientes.size()) {
            throw new IllegalArgumentException("Cliente inválido!");
        }
        return clientes.get(index);
    }

    public boolean estaVazio() {
        return clientes.isEmpty();
    }
}
