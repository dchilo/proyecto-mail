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

public class DInsumo {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "ID", "NOMBRE", "PROVEEDOR_ID", "PRECIO", "CANTIDAD" };
    public static final String[] HEADERSROL = { "ID", "ROL" };

    private SqlConnection connection;

    public DInsumo() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(String nombre, int proveedor_id, float precio, int cantidad)
            throws SQLException, ParseException {

        String query = "INSERT INTO insumos(nombre,proveedor_id,precio,cantidad)"
                + " values(?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setInt(2, proveedor_id);
        ps.setFloat(3, precio);
        ps.setInt(4, cantidad);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DInsumo.java dice: "
                    + "Ocurrio un error al insertar un insumo guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, String nombre, int proveedor_id, float precio, int cantidad)
            throws SQLException, ParseException {

        String query = "UPDATE insumos SET nombre=?, proveedor_id=?, precio=?,"
                + " cantidad=? "
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setInt(2, proveedor_id);
        ps.setFloat(3, precio);
        ps.setInt(4, cantidad);
        ps.setInt(5, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DInsumo.java dice: "
                    + "Ocurrio un error al modificar un insumo modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM insumos WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DInsumo.java dice: "
                    + "Ocurrio un error al eliminar un insumo eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> insumos = new ArrayList<>();
        String query = "SELECT * FROM insumos";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            insumos.add(new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("proveedor_id"),
                    set.getString("precio"),
                    set.getString("cantidad"),
            });
        }
        return insumos;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> insumos = new ArrayList<>();
        String query = "SELECT i.id AS insumo_id, i.nombre AS nombre_insumo, SUM(inv.cantidad) AS cantidad_total FROM insumos i LEFT JOIN inventarios inv ON i.id = inv.insumo_id GROUP BY i.id, i.nombre;";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            insumos.add(new String[] {
                    set.getString("nombre_insumo"),
                    set.getString("cantidad_total"),
            });
        }
        return insumos;
    }

    public String[] ver(int id) throws SQLException {
        String[] insumos = null;
        String query = "SELECT * FROM insumos WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            insumos = new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("proveedor_id"),
                    set.getString("precio"),
                    set.getString("cantidad"),
            };
        }
        return insumos;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "insumo agregar [nombre;proveedor_id;precio;cantidad]";
        String editar = "insumo modificar [nombre;proveedor_id;precio;cantidad]";
        String eliminar = "insumo eliminar [INSUMO_ID]";
        // String ver = "insumo ver (INSUMO_ID)";
        String listar = "insumo mostrar";
        String ayu = "insumo ayuda";

        ayudas.add(new String[] { String.valueOf(1), "Registrar INSUMO", registrar });
        ayudas.add(new String[] { String.valueOf(2), "Editar INSUMO", editar });
        ayudas.add(new String[] { String.valueOf(3), "Eliminar INSUMO", eliminar });
        // .add(new String[]{String.valueOf(4), "Ver INSUMO", ver});
        ayudas.add(new String[] { String.valueOf(4), "Listar INSUMO", listar });
        ayudas.add(new String[] { String.valueOf(5), "Ver Comandos CU: Usuarios", ayu });

        return ayudas;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
