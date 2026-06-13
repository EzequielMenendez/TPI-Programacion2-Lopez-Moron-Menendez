import entities.Categoria;
import entities.Producto;
import exceptions.EntidadNoEncontradaException;
import exceptions.StockInvalidoException;
import service.CategoriaService;
import service.ProductoService;
import java.util.List;
import java.util.Scanner;

public class MenuProducto {

    public static void mostrar(Scanner scanner, CategoriaService categoriaService, ProductoService productoService) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listar(productoService);
                    case 2 -> crear(scanner, categoriaService, productoService);
                    case 3 -> editar(scanner, productoService);
                    case 4 -> eliminar(scanner, productoService);
                    case 0 -> System.out.println("Volviendo al menu principal...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }

    private static void listar(ProductoService productoService) {
        List<Producto> lista = productoService.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay productos cargados.");
        } else {
            System.out.println("\n--- PRODUCTOS ---");
            for (Producto p : lista) {
                System.out.println(p);
            }
        }
    }

    private static void crear(Scanner scanner, CategoriaService categoriaService, ProductoService productoService) {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
            System.out.print("Precio: ");
            Double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            System.out.print("Imagen (nombre archivo): ");
            String imagen = scanner.nextLine();
            System.out.print("Disponible (S/N): ");
            boolean disponible = scanner.nextLine().equalsIgnoreCase("S");

            List<Categoria> categorias = categoriaService.listar();
            if (categorias.isEmpty()) {
                System.out.println("Error: no hay categorias disponibles. Cree una primero.");
                return;
            }
            System.out.println("Categorias disponibles:");
            for (Categoria c : categorias) {
                System.out.println(c);
            }
            System.out.print("ID de categoria: ");
            Long categoriaId = Long.parseLong(scanner.nextLine());
            Categoria categoria = categoriaService.buscarPorId(categoriaId);

            productoService.crear(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        } catch (NumberFormatException e) {
            System.out.println("Error: valor numerico invalido.");
        } catch (EntidadNoEncontradaException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editar(Scanner scanner, ProductoService productoService) {
        listar(productoService);
        System.out.print("Ingrese ID del producto a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Producto p = productoService.buscarPorId(id);
            System.out.print("Nuevo nombre (Enter para no cambiar): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo precio (actual: " + p.getPrecio() + "): ");
            Double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Nuevo stock (actual: " + p.getStock() + "): ");
            int stock = Integer.parseInt(scanner.nextLine());
            System.out.print("Disponible (S/N): ");
            boolean disponible = scanner.nextLine().equalsIgnoreCase("S");
            productoService.editar(id, nombre, precio, stock, disponible);
        } catch (NumberFormatException e) {
            System.out.println("Error: valor invalido.");
        } catch (EntidadNoEncontradaException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminar(Scanner scanner, ProductoService productoService) {
        listar(productoService);
        System.out.print("Ingrese ID del producto a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Confirma eliminacion? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("S")) {
                productoService.eliminar(id);
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