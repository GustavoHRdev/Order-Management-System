package api;

import api.dto.request.CriarProdutoRequest;
import api.dto.response.ProdutoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import model.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.ProdutoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@Validated
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listarProdutos() {
        return produtoService.listarProdutos().stream()
                .map(ProdutoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarProdutoPorId(@PathVariable("id") @Positive(message = "id: deve ser maior que zero.") int id) {
        return ProdutoResponse.from(produtoService.buscarProdutoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrarProduto(@Valid @RequestBody CriarProdutoRequest request) {
        Produto produto = produtoService.cadastrarProduto(request.nome(), request.preco());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.getId())
                .toUri();
        return ResponseEntity.created(location).body(ProdutoResponse.from(produto));
    }
}
