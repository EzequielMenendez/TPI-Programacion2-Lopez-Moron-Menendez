import entities.Categoria;
import exceptions.EntidadNoEncontradaException;
import service.CategoriaService;
import java.util.List;
import java.util.Scanner;

public class MenuCategoria {

    public static void mostrar(Scanner scanner, CategoriaService categoriaService) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listar(categoriaService);
                    case 2 -> crear(scanner, categoriaService);
                    case 3 -> editar(scanner, categoriaService);
                    case 4 -> eliminar(scanner, categoriaService);
                    case 0 -> System.out.println("Volviendo al menu principal...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }

    private static void listar(CategoriaService categoriaService) {
        List<Categoria> lista = categoriaService.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
        } else {
            System.out.println("\n--- CATEGORIAS ---");
            for (Categoria c : lista) {
                System.out.println(c);
            }
        }
    }

    private static void crear(Scanner scanner, CategoriaService categoriaService) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        categoriaService.crear(nombre, descripcion);
    }

    private static void editar(Scanner scanner, CategoriaService categoriaService) {
        listar(categoriaService);
        System.out.print("Ingrese ID de la categoria a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para no cambiar): ");
            String nombre = scanner.nextLine();
            System.out.print("Nueva descripcion (Enter para no cambiar): ");
            String descripcion = scanner.nextLine();
            categoriaService.editar(id, nombre, descripcion);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminar(Scanner scanner, CategoriaService categoriaService) {
        listar(categoriaService);
        System.out.print("Ingrese ID de la categoria a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Confirma eliminacion? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("S")) {
                categoriaService.eliminar(id);
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