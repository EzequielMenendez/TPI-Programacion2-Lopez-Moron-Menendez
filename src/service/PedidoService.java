package service;

import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private List<Pedido> pedidos = new ArrayList<>();
    private Long contadorId = 1L;

    public Pedido crear(Usuario usuario, FormaPago formaPago) {
        if (usuario == null) {
            System.out.println("Error: el pedido debe tener un usuario.");
            return null;
        }
        Pedido nuevo = new Pedido(usuario, formaPago);
        nuevo.setId(contadorId++);
        pedidos.add(nuevo);
        System.out.println("Pedido creado con ID: " + nuevo.getId());
        return nuevo;
    }

    public void agregarDetalle(Pedido pedido, int cantidad, Producto producto) {
        if (cantidad <= 0) {
            throw new StockInvalidoException("La cantidad debe ser mayor a 0.");
        }
        if (producto.getStock() < cantidad) {
            throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
        System.out.println("Detalle agregado correctamente.");
    }

    public List<Pedido> listar() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public List<Pedido> listarPorUsuario(Usuario usuario) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado() && p.getUsuario().getId().equals(usuario.getId())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Pedido buscarPorId(Long id) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
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
        System.out.println("Pedido actualizado correctamente.");
    }

    public void eliminar(Long id) {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
        for (var detalle : p.getDetalles()) {
            detalle.setEliminado(true);
        }
        System.out.println("Pedido eliminado correctamente.");
    }
}
