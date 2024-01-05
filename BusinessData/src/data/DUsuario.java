/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;


public class DUsuario {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "NOMBRE", "APELLIDO", "TIPO DE USUARIO", "TELEFONO", "EMAIL"};
    public static final String[] HEADERSROL = {"ID", "ROL"};
    
    private SqlConnection connection;
    
    public DUsuario() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);   
    }

    public void guardar(String nombre, String apellido, String tipo_usuario, String telefono, String email) throws SQLException, ParseException {
        
        String query = "INSERT INTO usuarios(nombre,apellido,tipo_usuario,telefono,email)"
                + " values(?,?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, tipo_usuario);
        ps.setString(4, telefono);
        ps.setString(5, email);
        
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al insertar un usuario guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, String nombre, String apellido, String tipo_usuario, String telefono, String email) 
            throws SQLException, ParseException {
        
        String query = "UPDATE usuarios SET nombre=?, apellido=?, tipo_usuario=?,"
                + " telefono=?, email=? "
                + " WHERE id=?";
        System.out.println(query);
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, tipo_usuario);
        ps.setString(4, telefono);
        ps.setString(5, email);
        ps.setInt(6, id);
        System.out.println(ps);
        
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al modificar un usuario modificar()");
            throw new SQLException();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al eliminar un usuario eliminar()");
            throw new SQLException();
        }
    }
    
    public List<String[]> listar() throws SQLException {
        List<String[]> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            usuarios.add(new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("nombre"),
                set.getString("apellido"),
                set.getString("tipo_usuario"),
                set.getString("telefono"),
                set.getString("email"),
            });
        }
        return usuarios;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> usuarios = new ArrayList<>();
        String query = "SELECT u.id AS usuario_id, CONCAT(u.nombre, ' ', u.apellido) AS nombre_completo, COUNT(m.id) AS cantidad_autos FROM usuarios u LEFT JOIN motorizados m ON u.id = m.cliente_id GROUP BY u.id, nombre_completo";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            usuarios.add(new String[] {
                set.getString("nombre_completo"),
                set.getString("cantidad_autos"),
            });
        }
        return usuarios;
    }
    
    public String[] ver(int id) throws SQLException {
        String[] usuario = null;
        String query = "SELECT * FROM usuarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            usuario = new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("nombre"),
                set.getString("apellido"),
                set.getString("tipo_usuario"),
                set.getString("telefono"),
                set.getString("email"),
            };
        }        
        return usuario;
    }
        
    public boolean esAdministrativo(String correo) throws SQLException {
        boolean resp = false;
        System.out.println(correo);
        String query = "SELECT * FROM usuarios WHERE tipo_usuario = 'Administrador' AND email=?";
        PreparedStatement statement = connection.connect().prepareStatement(query);
        statement.setString(1, correo);
        System.out.println(statement);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            resp = true;
        }
        return resp;
    }
    
    
    public int getIdByCorreo(String correo) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM usuarios WHERE email=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, correo);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            id = set.getInt("id");
        } 
        return id;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = " usuario agregar [nombre, apellido, tipo_usuario, telefono, email]";
        String editar = " usuario modificar [nombre, apellido, tipo_usuario, telefono, email]";
        String eliminar = " usuario eliminar [USUARIO_ID]";
        //String ver = " usuario ver (USUARIO_ID)";
        String listar = " usuario mostrar";
        String ayu = " usuario ayuda";

        ayudas.add(new String[]{String.valueOf(1), "Registrar USUARIO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Editar USUARIO", editar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar USUARIO", eliminar});
        //.add(new String[]{String.valueOf(4), "Ver USUARIO", ver});
        ayudas.add(new String[]{String.valueOf(4), "Listar USUARIO", listar});
        ayudas.add(new String[]{String.valueOf(5), "Ver Comandos CU: Usuarios", ayu});

        return ayudas;
    }
    
    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }    
}
