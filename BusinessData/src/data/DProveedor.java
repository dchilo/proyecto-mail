/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 *
 * @author dchil
 */
public class DProveedor {

    

    private SqlConnection connection;

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "ID", "NOMBRE", "CONTACTO", "TELEFONO", "EMAIL", "DIRECCION" };

    public DProveedor() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);       
    }

    public void guardar(String nombre, String contacto, String telefono, String email, String direccion)
            throws SQLException, ParseException {

        String query = "INSERT INTO proveedores(nombre, contacto, telefono, email, direccion)"
                + " values(?,?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, contacto);
        ps.setString(3, telefono);
        ps.setString(4, email);
        ps.setString(5, direccion);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al insertar un proveedor guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, String nombre, String contacto, String telefono, String email, String direccion)
            throws SQLException, ParseException {

        String query = "UPDATE proveedores SET nombre=?, apellido=?, tipo_usuario=?,"
                + " telefono=?, email=? "
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, contacto);
        ps.setString(3, telefono);
        ps.setString(4, email);
        ps.setString(5, direccion);
        ps.setInt(6, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al modificar un proveedor modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM proveedores WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DUsuario.java dice: "
                    + "Ocurrio un error al eliminar un proveedor eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> proveedores = new ArrayList<>();
        String query = "SELECT * FROM proveedores";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            proveedores.add(new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("contacto"),
                    set.getString("telefono"),
                    set.getString("email"),
                    set.getString("direccion"),
            });
        }
        return proveedores;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> proveedores = new ArrayList<>();
        String query = "SELECT p.nombre AS proveedor, COUNT(i.id) AS total_insumos FROM proveedores p LEFT JOIN insumos i ON p.id = i.proveedor_id GROUP BY proveedor";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            proveedores.add(new String[] {
                    set.getString("proveedor"),
                    set.getString("total_insumos"),
            });
        }
        return proveedores;
    }

    public String[] ver(int id) throws SQLException {
        String[] proveedores = null;
        String query = "SELECT * FROM proveedores WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            proveedores = new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("apellido"),
                    set.getString("tipo_usuario"),
                    set.getString("telefono"),
                    set.getString("email"),
            };
        }
        return proveedores;
    }

    public int getIdByCorreo(String correo) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM proveedores WHERE email=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, correo);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            id = set.getInt("id");
        }
        return id;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = " proveedor agregar [nombre, contacto, telefono, email, direccion]";
        String editar = " proveedor modificar [nombre, contacto, telefono, email, direccion]";
        String eliminar = " proveedor eliminar [PROVEEDOR_ID]";
        // String ver = " proveedor ver (PROVEEDOR_ID)";
        String listar = " proveedor mostrar";
        String ayu = " proveedor ayuda";

        ayudas.add(new String[] { String.valueOf(1), "Registrar PROVEEDOR", registrar });
        ayudas.add(new String[] { String.valueOf(2), "Editar PROVEEDOR", editar });
        ayudas.add(new String[] { String.valueOf(3), "Eliminar PROVEEDOR", eliminar });
        // .add(new String[]{String.valueOf(4), "Ver PROVEEDOR", ver});
        ayudas.add(new String[] { String.valueOf(4), "Listar PROVEEDOR", listar });
        ayudas.add(new String[] { String.valueOf(5), "Ver Comandos CU: Usuarios", ayu });

        return ayudas;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
