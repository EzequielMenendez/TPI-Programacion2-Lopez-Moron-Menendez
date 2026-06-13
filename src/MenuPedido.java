import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;
import java.util.List;
import java.util.Scanner;

public class MenuPedido {

    public static void mostrar(Scanner scanner, UsuarioService usuarioService, ProductoService productoService, PedidoService pedidoService) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Actualizar estado/forma de pago");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listar(pedidoService);
                    case 2 -> crear(scanner, usuarioService, productoService, pedidoService);
                    case 3 -> actualizar(scanner, pedidoService);
                    case 4 -> eliminar(scanner, pedidoService);
                    case 0 -> System.out.println("Volviendo al menu principal...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }

    private static void listar(PedidoService pedidoService) {
        List<Pedido> lista = pedidoService.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
        } else {
            System.out.println("\n--- PEDIDOS ---");
            for (Pedido p : lista) {
                System.out.println(p);
            }
        }
    }

    private static void crear(Scanner scanner, UsuarioService usuarioService, ProductoService productoService, PedidoService pedidoService) {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("Error: no hay usuarios disponibles. Cree uno primero.");
                return;
            }
            System.out.println("Usuarios disponibles:");
            for (Usuario u : usuarios) {
                System.out.println(u);
            }
            System.out.print("ID de usuario: ");
            Long usuarioId = Long.parseLong(scanner.nextLine());
            Usuario usuario = usuarioService.buscarPorId(usuarioId);

            System.out.println("Forma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            System.out.print("Seleccione: ");
            int fpOpcion = Integer.parseInt(scanner.nextLine());
            FormaPago formaPago = switch (fpOpcion) {
                case 1 -> FormaPago.TARJETA;
                case 2 -> FormaPago.TRANSFERENCIA;
                default -> FormaPago.EFECTIVO;
            };

            Pedido pedido = pedidoService.crear(usuario, formaPago);

            boolean agregarMas = true;
            while (agregarMas) {
                List<Producto> productos = productoService.listar();
                if (productos.isEmpty()) {
                    System.out.println("No hay productos disponibles.");
                    break;
                }
                System.out.println("Productos disponibles:");
                for (Producto p : productos) {
                    System.out.println(p);
                }
                System.out.print("ID de producto: ");
                Long productoId = Long.parseLong(scanner.nextLine());
                Producto producto = productoService.buscarPorId(productoId);
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                pedidoService.agregarDetalle(pedido, cantidad, producto);
                System.out.print("Agregar otro producto? (S/N): ");
                agregarMas = scanner.nextLine().equalsIgnoreCase("S");
            }

            System.out.println("Pedido finalizado. Total: $" + pedido.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("Error: valor invalido.");
        } catch (EntidadNoEncontradaException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void actualizar(Scanner scanner, PedidoService pedidoService) {
        listar(pedidoService);
        System.out.print("Ingrese ID del pedido a actualizar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.println("Nuevo estado:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");
            System.out.print("Seleccione: ");
            int estadoOpcion = Integer.parseInt(scanner.nextLine());
            Estado estado = switch (estadoOpcion) {
                case 1 -> Estado.PENDIENTE;
                case 2 -> Estado.CONFIRMADO;
                case 3 -> Estado.TERMINADO;
                default -> Estado.CANCELADO;
            };
            System.out.println("Nueva forma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            System.out.print("Seleccione: ");
            int fpOpcion = Integer.parseInt(scanner.nextLine());
            FormaPago formaPago = switch (fpOpcion) {
                case 1 -> FormaPago.TARJETA;
                case 2 -> FormaPago.TRANSFERENCIA;
                default -> FormaPago.EFECTIVO;
            };
            pedidoService.actualizarEstadoYFormaPago(id, estado, formaPago);
        } catch (NumberFormatException e) {
            System.out.println("Error: valor invalido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminar(Scanner scanner, PedidoService pedidoService) {
        listar(pedidoService);
        System.out.print("Ingrese ID del pedido a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Confirma eliminacion? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("S")) {
                pedidoService.eliminar(id);
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}