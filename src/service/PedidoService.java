package service;

import dao.CRUDPedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private CRUDPedido gestor = new CRUDPedido();

    public Pedido crear(Usuario usuario, FormaPago formaPago) {
        if (usuario == null) {
            System.out.println("Error: el pedido debe tener un usuario.");
            return null;
        }

        Pedido nuevo = new Pedido(usuario, formaPago);

        try {
            Long nuevoId = gestor.crearPedido(nuevo);
            nuevo.setId(nuevoId);
            System.out.println("Pedido creado en la base de datos con ID: " + nuevo.getId());
            return nuevo;
        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido en BD: " + e.getMessage());
            return null;
        }
    }

    public void agregarDetalle(Pedido pedido, int cantidad, Producto producto) {
        if (cantidad <= 0) {
            throw new StockInvalidoException("La cantidad debe ser mayor a 0.");
        }
        if (producto.getStock() < cantidad) {
            throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);

        try {
            double subtotal = cantidad * producto.getPrecio();
            gestor.guardarDetalleBD(pedido.getId(), cantidad, subtotal, producto.getId());
            System.out.println("Detalle agregado correctamente a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar el detalle en BD: " + e.getMessage());
        }
    }

    public List<Pedido> listar() {
        try {
            return gestor.listarPedidos();
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos desde la BD: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Pedido buscarPorId(Long id) {
        try {
            Pedido p = gestor.buscarPorId(id);
            if (p != null) {
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el pedido en la BD: " + e.getMessage());
        }
        throw new EntidadNoEncontradaException("Pedido con ID " + id + " no encontrado.");
    }

    public void actualizarEstadoYFormaPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago) {
        Pedido p = buscarPorId(id);

        if (nuevoEstado != null) {
            p.setEstado(nuevoEstado);
        }
        if (nuevaFormaPago != null) {
            p.setFormaPago(nuevaFormaPago);
        }

        try {
            gestor.actualizarEstadoYFormaPago(id, p.getEstado(), p.getFormaPago());
            System.out.println("Pedido actualizado correctamente en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el pedido en BD: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        buscarPorId(id);

        try {
            gestor.eliminarPedido(id);
            System.out.println("Pedido y sus detalles eliminados correctamente de la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido en BD: " + e.getMessage());
        }
    }
}