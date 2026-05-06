package service;

import model.Cliente;
import org.springframework.stereotype.Service;
import repository.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente cadastrarCliente(String nome, String email) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (!emailValido(email)) {
            throw new IllegalArgumentException("Email inválido.");
        }

        Cliente cliente = new Cliente(nome, email);
        repository.salvar(cliente);
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return repository.listar();
    }

    public Cliente buscarClientePorId(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente inválido!"));
    }

    private boolean emailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
