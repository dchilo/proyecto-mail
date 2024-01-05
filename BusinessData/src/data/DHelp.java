/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;


public class DHelp {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "CU", "ACCION", "PARAMETROS", "EJEMPLO"};
    
    private SqlConnection connection;
    
    public DHelp() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);   
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> usuarios = new ArrayList<>();
        String query = "SELECT * FROM help";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            usuarios.add(new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("cu"),
                set.getString("accion"),
                set.getString("parametros"),
                set.getString("ejemplo"),
            });
        }
        return usuarios;
    }

    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }    
}
