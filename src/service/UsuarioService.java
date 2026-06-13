package service;

import entities.Usuario;
import enums.Rol;
import exceptions.EntidadNoEncontradaException;
import exceptions.MailDuplicadoException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios = new ArrayList<>();
    private Long contadorId = 1L;

    public void crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        if (mail == null || mail.isBlank()) {
            System.out.println("Error: el mail no puede estar vacio.");
            return;
        }
        for (Usuario u : usuarios) {
            if (!u.isEliminado() && u.getMail().equalsIgnoreCase(mail)) {
                throw new MailDuplicadoException("Ya existe un usuario con el mail: " + mail);
            }
        }
        Usuario nuevo = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
        nuevo.setId(contadorId++);
        usuarios.add(nuevo);
        System.out.println("Usuario creado con ID: " + nuevo.getId());
    }

    public List<Usuario> listar() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (!u.isEliminado()) {
                activos.add(u);
            }
        }
        return activos;
    }

    public Usuario buscarPorId(Long id) {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id) && !u.isEliminado()) {
                return u;
            }
        }
        throw new EntidadNoEncontradaException("Usuario con ID " + id + " no encontrado.");
    }

    public void editar(Long id, String nuevoNombre, String nuevoApellido, String nuevoMail, String nuevoCelular) {
        Usuario u = buscarPorId(id);
        if (nuevoMail != null && !nuevoMail.isBlank() && !nuevoMail.equalsIgnoreCase(u.getMail())) {
            for (Usuario otro : usuarios) {
                if (!otro.isEliminado() && otro.getMail().equalsIgnoreCase(nuevoMail)) {
                    throw new MailDuplicadoException("Ya existe un usuario con el mail: " + nuevoMail);
                }
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
        System.out.println("Usuario actualizado correctamente.");
    }

    public void eliminar(Long id) {
        Usuario u = buscarPorId(id);
        u.setEliminado(true);
        System.out.println("Usuario eliminado correctamente.");
    }
}