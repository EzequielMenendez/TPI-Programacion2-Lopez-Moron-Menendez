package service;

import dao.CRUDProducto;
import entities.Categoria;
import entities.Producto;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private CRUDProducto gestor = new CRUDProducto();

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

        try {
            Long nuevoId = gestor.crearProducto(nuevo);
            nuevo.setId(nuevoId);
            System.out.println("Producto creado en la base de datos con ID: " + nuevo.getId());
        } catch (SQLException e) {
            System.out.println("Error al crear el producto en la BD: " + e.getMessage());
        }
    }

    public List<Producto> listar() {
        try {
            return gestor.listarProductos();
        } catch (SQLException e) {
            System.out.println("Error al listar los productos desde la BD: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Producto buscarPorId(Long id) {
        try {
            Producto p = gestor.buscarPorId(id);
            if (p != null) {
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el producto en la BD: " + e.getMessage());
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

        try {
            gestor.actualizarProducto(p);
            System.out.println("Producto actualizado correctamente en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto en la BD: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id); // Validamos que exista antes de eliminar

        try {
            gestor.eliminarProducto(id);
            System.out.println("Producto eliminado correctamente de la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto de la BD: " + e.getMessage());
        }
    }
}