package service;

import entities.Categoria;
import exceptions.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    private List<Categoria> categorias = new ArrayList<>();
    private Long contadorId = 1L;

    public void crear(String nombre, String descripcion) {
        for (Categoria c : categorias) {
            if (!c.isEliminado() && c.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Error: ya existe una categoria con ese nombre.");
                return;
            }
        }
        Categoria nueva = new Categoria(nombre, descripcion);
        nueva.setId(contadorId++);
        categorias.add(nueva);
        System.out.println("Categoria creada con ID: " + nueva.getId());
    }

    public List<Categoria> listar() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria c : categorias) {
            if (!c.isEliminado()) {
                activas.add(c);
            }
        }
        return activas;
    }

    public Categoria buscarPorId(Long id) {
        for (Categoria c : categorias) {
            if (c.getId().equals(id) && !c.isEliminado()) {
                return c;
            }
        }
        throw new EntidadNoEncontradaException("Categoria con ID " + id + " no encontrada.");
    }

    public void editar(Long id, String nuevoNombre, String nuevaDescripcion) {
        Categoria c = buscarPorId(id);
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            c.setNombre(nuevoNombre);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            c.setDescripcion(nuevaDescripcion);
        }
        System.out.println("Categoria actualizada correctamente.");
    }

    public void eliminar(Long id) {
        Categoria c = buscarPorId(id);
        c.setEliminado(true);
        System.out.println("Categoria eliminada correctamente.");
    }
}