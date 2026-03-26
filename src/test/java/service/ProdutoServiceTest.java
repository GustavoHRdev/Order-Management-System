package service;

import model.Produto;
import org.junit.jupiter.api.Test;
import repository.ProdutoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProdutoServiceTest {

    @Test
    void cadastrarProdutoDeveSalvarProdutoValido() {
        ProdutoRepository repository = new ProdutoRepository();
        ProdutoService service = new ProdutoService(repository);

        service.cadastrarProduto("Teclado", 150.0);

        List<Produto> produtos = service.listarProdutos();
        assertEquals(1, produtos.size());
        assertEquals("Teclado", produtos.get(0).getNome());
        assertEquals(150.0, produtos.get(0).getPreco(), 0.0001);
    }

    @Test
    void cadastrarProdutoDeveFalharComNomeInvalido() {
        ProdutoRepository repository = new ProdutoRepository();
        ProdutoService service = new ProdutoService(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarProduto(" ", 150.0));
    }

    @Test
    void cadastrarProdutoDeveFalharComPrecoInvalido() {
        ProdutoRepository repository = new ProdutoRepository();
        ProdutoService service = new ProdutoService(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarProduto("Teclado", 0.0));
    }
}
