package io.github.gustavohrdev.service;

import io.github.gustavohrdev.exception.ClienteNotFoundException;
import io.github.gustavohrdev.exception.InvalidClientNameException;
import io.github.gustavohrdev.exception.InvalidEmailException;
import io.github.gustavohrdev.model.Cliente;
import org.springframework.stereotype.Service;
import io.github.gustavohrdev.repository.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente cadastrarCliente(String nome, String email) {

        if (nome == null || nome.isBlank()) {
            throw new InvalidClientNameException();
        }

        if (!emailValido(email)) {
            throw new InvalidEmailException();
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
                .orElseThrow(ClienteNotFoundException::new);
    }

    private boolean emailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

