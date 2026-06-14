package service;

import dao.CRUDUsuario;
import entities.Usuario;
import enums.Rol;
import exceptions.EntidadNoEncontradaException;
import exceptions.MailDuplicadoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private CRUDUsuario gestor = new CRUDUsuario();

    public void crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        if (mail == null || mail.isBlank()) {
            System.out.println("Error: el mail no puede estar vacío.");
            return;
        }

        try {
            if (gestor.buscarPorMail(mail) != null) {
                throw new MailDuplicadoException("Ya existe un usuario con el mail: " + mail);
            }

            Usuario nuevo = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
            Long nuevoId = gestor.crearUsuario(nuevo);
            nuevo.setId(nuevoId);
            System.out.println("Usuario creado en la base de datos con ID: " + nuevo.getId());

        } catch (SQLException e) {
            System.out.println("Error al guardar el usuario en BD: " + e.getMessage());
        } catch (MailDuplicadoException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Usuario> listar() {
        try {
            return gestor.listarUsuarios();
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios desde la BD: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Usuario buscarPorId(Long id) {
        try {
            Usuario u = gestor.buscarPorId(id);
            if (u != null) {
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario en la BD: " + e.getMessage());
        }
        throw new EntidadNoEncontradaException("Usuario con ID " + id + " no encontrado.");
    }

    public void editar(Long id, String nuevoNombre, String nuevoApellido, String nuevoMail, String nuevoCelular) {
        Usuario u = buscarPorId(id);

        try {
            if (nuevoMail != null && !nuevoMail.isBlank() && !nuevoMail.equalsIgnoreCase(u.getMail())) {
                if (gestor.buscarPorMail(nuevoMail) != null) {
                    throw new MailDuplicadoException("Ya existe un usuario con el mail: " + nuevoMail);
                }
                u.setMail(nuevoMail);
            }
            if (nuevoNombre != null && !nuevoNombre.isBlank()) {
                u.setNombre(nuevoNombre);
            }
            if (nuevoApellido != null && !nuevoApellido.isBlank()) {
                u.setApellido(nuevoApellido);
            }
            if (nuevoCelular != null && !nuevoCelular.isBlank()) {
                u.setCelular(nuevoCelular);
            }

            gestor.actualizarUsuario(u);
            System.out.println("Usuario actualizado correctamente en la base de datos.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario en BD: " + e.getMessage());
        } catch (MailDuplicadoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);

        try {
            gestor.eliminarUsuario(id);
            System.out.println("Usuario eliminado lógicamente de la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario en BD: " + e.getMessage());
        }
    }
}