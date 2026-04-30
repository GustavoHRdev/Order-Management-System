package api.dto.response;

import model.Produto;

public record ProdutoResponse(int id, String nome, double preco) {

    public static ProdutoResponse from(Produto produto) {
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }
}
