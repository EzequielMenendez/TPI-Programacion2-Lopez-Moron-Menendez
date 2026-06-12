package config;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {

        try (Connection conexion = DatabaseConnection.getConnection()) {

            if (conexion != null) {
                System.out.println("¡Conexión establecida con éxito!");

            }

        } catch (SQLException e) {
            System.out.println("Error al operar con la base de datos.");
            e.printStackTrace();
        }
    }
}