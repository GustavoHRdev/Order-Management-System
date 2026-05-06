package io.github.gustavohrdev.repository;

import io.github.gustavohrdev.model.Produto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcProdutoRepository implements ProdutoRepository {

    private final DataSource dataSource;

    public JdbcProdutoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    produto.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao salvar produto.", e);
        }
    }

    @Override
    public List<Produto> listar() {
        String sql = "SELECT id, nome, preco FROM produtos ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                produtos.add(mapProduto(resultSet));
            }
            return produtos;
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao listar produtos.", e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        String sql = "SELECT id, nome, preco FROM produtos WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProduto(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao buscar produto.", e);
        }
    }

    @Override
    public boolean estaVazio() {
        String sql = "SELECT 1 FROM produtos FETCH FIRST ROW ONLY";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return !resultSet.next();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao verificar produtos.", e);
        }
    }

    private Produto mapProduto(ResultSet resultSet) throws SQLException {
        return new Produto(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                resultSet.getDouble("preco")
        );
    }
}

