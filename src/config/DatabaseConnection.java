package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Configuración de los datos de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/pedidos_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver JDBC de MySQL en el proyecto.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de conexión: Verifica que el servidor esté activo y los datos sean correctos.");
            e.printStackTrace();
        }
        return conexion;
    }
}
