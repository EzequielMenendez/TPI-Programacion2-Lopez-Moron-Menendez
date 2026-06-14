package dao;

import config.DatabaseConnection;
import entities.Categoria;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import enums.Rol;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CRUDPedido {

    //Creación del podido
    public Long crearPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (fecha, estado, total, forma_pago, usuario_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setString(2, Estado.PENDIENTE.name());
            stmt.setDouble(3, pedido.getTotal() != null ? pedido.getTotal() : 0.0);
            stmt.setString(4, pedido.getFormaPago().name());
            stmt.setLong(5, pedido.getUsuario().getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            throw new SQLException("No se pudo obtener el ID del pedido.");
        }
    }

    //Creación de un detalle pedido
    public void guardarDetalleBD(Long pedidoId, int cantidad, double subtotal, Long productoId) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (cantidad, subtotal, pedido_id, producto_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cantidad);
            stmt.setDouble(2, subtotal);
            stmt.setLong(3, pedidoId);
            stmt.setLong(4, productoId);

            stmt.executeUpdate();
        }
    }

    //Listado de pedidos
    public List<Pedido> listarPedidos() throws SQLException {
        String sql = "SELECT p.*, u.nombre, u.apellido, u.mail, u.celular, u.contraseña, u.rol " +
                "FROM pedido p " +
                "INNER JOIN usuario u ON p.usuario_id = u.id " +
                "WHERE p.eliminado = FALSE";

        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("mail"),
                        rs.getString("celular"),
                        rs.getString("contraseña"),
                        Rol.valueOf(rs.getString("rol"))
                );
                u.setId(rs.getLong("usuario_id"));

                FormaPago fp = FormaPago.valueOf(rs.getString("forma_pago"));
                Pedido p = new Pedido(u, fp);

                p.setId(rs.getLong("id"));
                if (rs.getDate("fecha") != null) {
                    p.setFecha(rs.getDate("fecha").toLocalDate());
                }
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));

                lista.add(p);
            }
        }
        return lista;
    }

    //Busqueda de pedido por id
    public Pedido buscarPorId(Long id) throws SQLException {
        String sqlPedido = "SELECT p.*, u.nombre, u.apellido, u.mail, u.celular, u.contraseña, u.rol " +
                "FROM pedido p " +
                "INNER JOIN usuario u ON p.usuario_id = u.id " +
                "WHERE p.id = ? AND p.eliminado = FALSE";

        String sqlDetalles = "SELECT dp.*, pr.nombre, pr.precio, pr.descripcion, pr.stock, pr.imagen, pr.disponible, pr.categoria_id " +
                "FROM detalle_pedido dp " +
                "INNER JOIN producto pr ON dp.producto_id = pr.id " +
                "WHERE dp.pedido_id = ? AND dp.eliminado = FALSE";

        Pedido p = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtP = conn.prepareStatement(sqlPedido)) {

            stmtP.setLong(1, id);

            try (ResultSet rs = stmtP.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("mail"),
                            rs.getString("celular"),
                            rs.getString("contraseña"),
                            Rol.valueOf(rs.getString("rol"))
                    );
                    u.setId(rs.getLong("usuario_id"));

                    FormaPago fp = FormaPago.valueOf(rs.getString("forma_pago"));
                    p = new Pedido(u, fp);
                    p.setId(rs.getLong("id"));

                    if (rs.getDate("fecha") != null) {
                        p.setFecha(rs.getDate("fecha").toLocalDate());
                    }
                    p.setEstado(Estado.valueOf(rs.getString("estado")));
                    p.setTotal(rs.getDouble("total"));
                }
            }
        }

        if (p != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmtD = conn.prepareStatement(sqlDetalles)) {

                stmtD.setLong(1, id);

                try (ResultSet rs = stmtD.executeQuery()) {
                    while (rs.next()) {
                        int cantidad = rs.getInt("cantidad");
                        double subtotal = rs.getDouble("subtotal");

                        // Creamos una categoría temporal (mock) para satisfacer el constructor del Producto
                        Categoria cat = new Categoria("Cat Temporal", "");
                        cat.setId(rs.getLong("categoria_id"));

                        // Instanciamos el producto con los datos del JOIN
                        Producto prod = new Producto(
                                rs.getString("nombre"),
                                rs.getDouble("precio"),
                                rs.getString("descripcion"),
                                rs.getInt("stock"),
                                rs.getString("imagen"),
                                rs.getBoolean("disponible"),
                                cat
                        );
                        prod.setId(rs.getLong("producto_id"));

                        p.addDetallePedido(cantidad, subtotal, prod);
                    }
                }
            }
        }
        return p;
    }

    //Edición de pedido
    public void actualizarEstadoYFormaPago(Long id, Estado estado, FormaPago formaPago) throws SQLException {
        String sql = "UPDATE pedido SET estado = ?, forma_pago = ? WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.name());
            stmt.setString(2, formaPago.name());
            stmt.setLong(3, id);

            stmt.executeUpdate();
        }
    }

    //Eliminación lógica de pedido
    public void eliminarPedido(Long id) throws SQLException {
        String sqlPedido = "UPDATE pedido SET eliminado = TRUE WHERE id = ?";
        String sqlDetalles = "UPDATE detalle_pedido SET eliminado = TRUE WHERE pedido_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtP = conn.prepareStatement(sqlPedido);
             PreparedStatement stmtD = conn.prepareStatement(sqlDetalles)) {

            stmtP.setLong(1, id);
            stmtP.executeUpdate();

            stmtD.setLong(1, id);
            stmtD.executeUpdate();
        }
    }
}