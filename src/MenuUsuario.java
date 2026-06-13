import entities.Usuario;
import enums.Rol;
import exceptions.EntidadNoEncontradaException;
import exceptions.MailDuplicadoException;
import service.UsuarioService;
import java.util.List;
import java.util.Scanner;

public class MenuUsuario {

    public static void mostrar(Scanner scanner, UsuarioService usuarioService) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> listar(usuarioService);
                    case 2 -> crear(scanner, usuarioService);
                    case 3 -> editar(scanner, usuarioService);
                    case 4 -> eliminar(scanner, usuarioService);
                    case 0 -> System.out.println("Volviendo al menu principal...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }

    private static void listar(UsuarioService usuarioService) {
        List<Usuario> lista = usuarioService.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
        } else {
            System.out.println("\n--- USUARIOS ---");
            for (Usuario u : lista) {
                System.out.println(u);
            }
        }
    }

    private static void crear(Scanner scanner, UsuarioService usuarioService) {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            System.out.print("Mail: ");
            String mail = scanner.nextLine();
            System.out.print("Celular: ");
            String celular = scanner.nextLine();
            System.out.print("Contrasenia: ");
            String contrasenia = scanner.nextLine();
            System.out.println("Rol:");
            System.out.println("1. ADMIN");
            System.out.println("2. USUARIO");
            System.out.print("Seleccione: ");
            int rolOpcion = Integer.parseInt(scanner.nextLine());
            Rol rol = rolOpcion == 1 ? Rol.ADMIN : Rol.USUARIO;
            usuarioService.crear(nombre, apellido, mail, celular, contrasenia, rol);
        } catch (NumberFormatException e) {
            System.out.println("Error: valor invalido.");
        } catch (MailDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editar(Scanner scanner, UsuarioService usuarioService) {
        listar(usuarioService);
        System.out.print("Ingrese ID del usuario a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Nuevo nombre (Enter para no cambiar): ");
            String nombre = scanner.nextLine();
            System.out.print("Nuevo apellido (Enter para no cambiar): ");
            String apellido = scanner.nextLine();
            System.out.print("Nuevo mail (Enter para no cambiar): ");
            String mail = scanner.nextLine();
            System.out.print("Nuevo celular (Enter para no cambiar): ");
            String celular = scanner.nextLine();
            usuarioService.editar(id, nombre, apellido, mail, celular);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID invalido.");
        } catch (EntidadNoEncontradaException | MailDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminar(Scanner scanner, UsuarioService usuarioService) {
        listar(usuarioService);
        System.out.print("Ingrese ID del usuario a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            System.out.print("Confirma eliminacion? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("S")) {
                usuarioService.eliminar(id);
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
