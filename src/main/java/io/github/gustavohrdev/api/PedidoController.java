package io.github.gustavohrdev.api;

import io.github.gustavohrdev.api.dto.request.AdicionarItemRequest;
import io.github.gustavohrdev.api.dto.request.AtualizarStatusRequest;
import io.github.gustavohrdev.api.dto.request.CriarPedidoRequest;
import io.github.gustavohrdev.api.dto.response.PedidoResponse;
import io.github.gustavohrdev.exception.InvalidOrderStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import io.github.gustavohrdev.model.Pedido;
import io.github.gustavohrdev.model.StatusPedido;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.github.gustavohrdev.service.PedidoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Validated
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoResponse> listarPedidos() {
        return pedidoService.listarPedidos().stream()
                .map(PedidoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPedidoPorId(@PathVariable("id") @Positive(message = "id: deve ser maior que zero.") int id) {
        return PedidoResponse.from(pedidoService.buscarPedidoPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        Pedido pedido = pedidoService.criarPedido(request.clienteId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.getId())
                .toUri();
        return ResponseEntity.created(location).body(PedidoResponse.from(pedido));
    }

    @PostMapping("/{id}/itens")
    public PedidoResponse adicionarItem(@PathVariable("id") @Positive(message = "id: deve ser maior que zero.") int id,
                                        @Valid @RequestBody AdicionarItemRequest request) {
        pedidoService.adicionarItem(id, request.produtoId(), request.quantidade());
        return PedidoResponse.from(pedidoService.buscarPedidoPorId(id));
    }

    @PatchMapping("/{id}/status")
    public PedidoResponse atualizarStatus(@PathVariable("id") @Positive(message = "id: deve ser maior que zero.") int id,
                                          @Valid @RequestBody AtualizarStatusRequest request) {
        pedidoService.atualizarStatus(id, parseStatus(request.status()));
        return PedidoResponse.from(pedidoService.buscarPedidoPorId(id));
    }

    private StatusPedido parseStatus(String status) {
        try {
            return StatusPedido.valueOf(status.trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException();
        }
    }
}

