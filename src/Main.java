import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Scanner scanner = new Scanner(System.in);

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            //establecer conexion
            String url = "jdbc:mysql://localhost:3306/bibliojuegos";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión a la base de datos exitosa.");

            //aca viene el resto del codigo


        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                if(resultSet != null) resultSet.close();
                if(preparedStatement != null) preparedStatement.close();
                if(connection != null) connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}