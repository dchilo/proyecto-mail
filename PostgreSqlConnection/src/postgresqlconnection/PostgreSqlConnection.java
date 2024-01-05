package postgresqlconnection;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlConnection {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbHost = dotenv.get("DB_HOST");
        String dbPort = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");

        try {
            SqlConnection sqlConnection =
                    new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
            String query = "SELECT * FROM usuarios WHERE id = 1";
            PreparedStatement ps = sqlConnection.connect().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("resultado: " + rs.next());
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSqlConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
