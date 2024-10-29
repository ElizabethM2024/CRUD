package Formulario_registrate;  // Esta línea debe estar presente

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            String url = "jdbc:mysql://localhost:3306/formulario_registrate"; // Cambia por tu base de datos
            String user = "root"; // Cambia por tu usuario de MySQL
            String password = "Sonic0214"; // Cambia por tu contraseña de MySQL
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
