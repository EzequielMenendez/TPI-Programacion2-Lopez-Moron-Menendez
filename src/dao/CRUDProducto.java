package dao;

import config.DatabaseConnection;
import entities.Categoria;
import entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDProducto {
    //Creación de un producto
    public Long crearProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagen());
            stmt.setBoolean(6, producto.isDisponible());
            stmt.setLong(7, producto.getCategoria().getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            throw new SQLException("No se pudo obtener el ID del producto.");
        }
    }

    //Listado de productos
    public List<Producto> listarProductos() throws SQLException {
        String sql = "SELECT p.*, c.nombre AS c_nombre, c.descripcion AS c_desc " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.eliminado = FALSE";

        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Reconstruimos la categoría asociada
                Categoria cat = new Categoria(rs.getString("c_nombre"), rs.getString("c_desc"));
                cat.setId(rs.getLong("categoria_id"));

                // Reconstruimos el producto
                Producto p = new Producto(
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getString("imagen"),
                        rs.getBoolean("disponible"),
                        cat
                );
                p.setId(rs.getLong("id"));
                lista.add(p);
            }
        }
        return lista;
    }

    //Busqueda de producto por id
    public Producto buscarPorId(Long id) throws SQLException {
        String sql = "SELECT p.*, c.nombre AS c_nombre, c.descripcion AS c_desc " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.id = ? AND p.eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(rs.getString("c_nombre"), rs.getString("c_desc"));
                    cat.setId(rs.getLong("categoria_id"));

                    Producto p = new Producto(
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getString("descripcion"),
                            rs.getInt("stock"),
                            rs.getString("imagen"),
                            rs.getBoolean("disponible"),
                            cat
                    );
                    p.setId(rs.getLong("id"));
                    return p;
                }
            }
        }
        return null;
    }

    //Actualización de un producto
    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ?, categoria_id = ? WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagen());
            stmt.setBoolean(6, producto.isDisponible());
            stmt.setLong(7, producto.getCategoria().getId());
            stmt.setLong(8, producto.getId());

            stmt.executeUpdate();
        }
    }

    //Eliminación lógica de un producto
    public void eliminarProducto(Long id) throws SQLException {
        String sql = "UPDATE producto SET eliminado = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}