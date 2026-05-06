package io.github.gustavohrdev.api;

import io.github.gustavohrdev.api.dto.request.CriarClienteRequest;
import io.github.gustavohrdev.api.dto.response.ClienteResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import io.github.gustavohrdev.model.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.gustavohrdev.service.ClienteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {
        return clienteService.listarClientes().stream()
                .map(ClienteResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarClientePorId(@PathVariable("id") @Positive(message = "id: deve ser maior que zero.") int id) {
        return ClienteResponse.from(clienteService.buscarClientePorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrarCliente(@Valid @RequestBody CriarClienteRequest request) {
        Cliente cliente = clienteService.cadastrarCliente(request.nome(), request.email());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();
        return ResponseEntity.created(location).body(ClienteResponse.from(cliente));
    }
}

