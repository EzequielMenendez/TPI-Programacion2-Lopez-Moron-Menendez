package dao;

import config.DatabaseConnection;
import entities.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDCategoria {

    // Función para crear categorias
    public int crearCategoria(String nombre, String descripcion) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retorna el nuevo ID al Service
                    }
                }
            }

            throw new SQLException("No se pudo obtener el ID generado para la categoría.");
        }
    }

    // Función para listar las categorias
    public List<Categoria> listarCategorias() throws SQLException {
        String sql = "SELECT * FROM categoria WHERE eliminado = FALSE";

        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria cat = new Categoria(
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );

                cat.setId(rs.getLong("id"));

                categorias.add(cat);
            }
        }

        return categorias;
    }

    //Busqueda de categoria por nombre
    public Categoria buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nombre = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                    cat.setId(rs.getLong("id"));
                    return cat;
                }
            }
        }
        return null;
    }

    //Busqueda de categoria por id
    public Categoria buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                    cat.setId(rs.getLong("id"));
                    return cat;
                }
            }
        }
        return null;
    }

    // Edición de categoria
    public void actualizarCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setLong(3, categoria.getId());

            stmt.executeUpdate();
        }
    }

    // Eliminación(lóigca) de categoria
    public void eliminarCategoria(Long id) throws SQLException {
        // Hacemos un UPDATE en vez de DELETE para respetar la baja lógica
        String sql = "UPDATE categoria SET eliminado = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
