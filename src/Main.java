import service.CategoriaService;
import service.ProductoService;
import service.UsuarioService;
import service.PedidoService;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static CategoriaService categoriaService = new CategoriaService();
    static ProductoService productoService = new ProductoService();
    static UsuarioService usuarioService = new UsuarioService();
    static PedidoService pedidoService = new PedidoService();

    public static void main(String[] args) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> MenuCategoria.mostrar(scanner, categoriaService);
                    case 2 -> MenuProducto.mostrar(scanner, categoriaService, productoService);
                    case 3 -> MenuUsuario.mostrar(scanner, usuarioService);
                    case 4 -> MenuPedido.mostrar(scanner, usuarioService, productoService, pedidoService);
                    case 0 -> System.out.println("Hasta luego!");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
        scanner.close();
    }
}