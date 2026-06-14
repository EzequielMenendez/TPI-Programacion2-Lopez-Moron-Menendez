package service;

import dao.CRUDCategoria;
import entities.Categoria;
import exceptions.EntidadNoEncontradaException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    CRUDCategoria gestor = new CRUDCategoria();

    public void crear(String nombre, String descripcion) {
        try {
            Categoria existente = gestor.buscarPorNombre(nombre);

            if (existente != null) {
                System.out.println("Error: ya existe una categoría activa con el nombre '" + nombre + "'.");
                return;
            }

            int nuevoId = gestor.crearCategoria(nombre, descripcion);
            System.out.println("Categoría creada exitosamente en la base de datos con ID: " + nuevoId);

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    public List<Categoria> listar() {
        try {
            return gestor.listarCategorias();

        } catch (SQLException e) {
            System.out.println("Error al intentar obtener las categorías: " + e.getMessage());

            return new ArrayList<>();
        }
    }

    public Categoria buscarPorId(Long id) {
        try {
            Categoria c = gestor.buscarPorId(id);
            if (c != null) {
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos al buscar la categoría: " + e.getMessage());
        }
        throw new EntidadNoEncontradaException("Categoría con ID " + id + " no encontrada o eliminada.");
    }

    public void editar(Long id, String nuevoNombre, String nuevaDescripcion) {
        try {
            Categoria c = buscarPorId(id);

            if (nuevoNombre != null && !nuevoNombre.isBlank()) {
                c.setNombre(nuevoNombre);
            }
            if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
                c.setDescripcion(nuevaDescripcion);
            }

            gestor.actualizarCategoria(c);
            System.out.println("Categoría actualizada correctamente en la base de datos.");

        } catch (EntidadNoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al actualizar en la base de datos: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        try {
            buscarPorId(id);

            gestor.eliminarCategoria(id);
            System.out.println("Categoría eliminada correctamente del sistema.");

        } catch (EntidadNoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al intentar eliminar la categoría: " + e.getMessage());
        }
    }
}