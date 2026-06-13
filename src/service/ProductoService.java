package service;

import entities.Categoria;
import entities.Producto;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private List<Producto> productos = new ArrayList<>();
    private Long contadorId = 1L;

    public void crear(String nombre, Double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria) {
        if (precio < 0) {
            throw new StockInvalidoException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo.");
        }
        if (nombre == null || nombre.isBlank()) {
            System.out.println("Error: el nombre no puede estar vacio.");
            return;
        }
        Producto nuevo = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        nuevo.setId(contadorId++);
        productos.add(nuevo);
        System.out.println("Producto creado con ID: " + nuevo.getId());
    }

    public List<Producto> listar() {
        List<Producto> activos = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public List<Producto> listarPorCategoria(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (!p.isEliminado() && p.getCategoria().getId().equals(categoria.getId())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Producto buscarPorId(Long id) {
        for (Producto p : productos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("Producto con ID " + id + " no encontrado.");
    }

    public void editar(Long id, String nuevoNombre, Double nuevoPrecio, int nuevoStock, boolean disponible) {
        Producto p = buscarPorId(id);
        if (nuevoPrecio < 0) {
            throw new StockInvalidoException("El precio no puede ser negativo.");
        }
        if (nuevoStock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo.");
        }
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            p.setNombre(nuevoNombre);
        }
        p.setPrecio(nuevoPrecio);
        p.setStock(nuevoStock);
        p.setDisponible(disponible);
        System.out.println("Producto actualizado correctamente.");
    }

    public void eliminar(Long id) {
        Producto p = buscarPorId(id);
        p.setEliminado(true);
        System.out.println("Producto eliminado correctamente.");
    }
}
