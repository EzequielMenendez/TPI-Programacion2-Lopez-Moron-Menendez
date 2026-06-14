package dao;

import config.DatabaseConnection;
import entities.Usuario;
import enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDUsuario {
    //Creación de un usuario
    public Long crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, mail, celular, contraseña, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getMail());
            stmt.setString(4, usuario.getCelular());
            stmt.setString(5, usuario.getContrasenia()); // o getContraseña() según como lo tengas en tu entidad
            stmt.setString(6, usuario.getRol().name());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            throw new SQLException("No se pudo obtener el ID del usuario.");
        }
    }

    //Listado de usuarios
    public List<Usuario> listarUsuarios() throws SQLException {
        String sql = "SELECT * FROM usuario WHERE eliminado = FALSE";
        List<Usuario> lista = new ArrayList<>();

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
                u.setId(rs.getLong("id"));
                lista.add(u);
            }
        }
        return lista;
    }

    //Busqueda de usuario por id
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("mail"),
                            rs.getString("celular"),
                            rs.getString("contraseña"),
                            Rol.valueOf(rs.getString("rol"))
                    );
                    u.setId(rs.getLong("id"));
                    return u;
                }
            }
        }
        return null;
    }

    // Función para buscar un usuario por email
    public Usuario buscarPorMail(String mail) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE mail = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mail);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("mail"),
                            rs.getString("celular"),
                            rs.getString("contraseña"),
                            Rol.valueOf(rs.getString("rol"))
                    );
                    u.setId(rs.getLong("id"));
                    return u;
                }
            }
        }
        return null;
    }

    //Actualización de un usuario
    public void actualizarUsuario(Usuario usuario) throws SQLException {
        // En este CRUD básico de edición de perfil no actualizamos contraseña ni rol, solo datos de contacto
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, mail = ?, celular = ? WHERE id = ? AND eliminado = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getMail());
            stmt.setString(4, usuario.getCelular());
            stmt.setLong(5, usuario.getId());

            stmt.executeUpdate();
        }
    }

    //Eliminación lógica de usuario
    public void eliminarUsuario(Long id) throws SQLException {
        String sql = "UPDATE usuario SET eliminado = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}