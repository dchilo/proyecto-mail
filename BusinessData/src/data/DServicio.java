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

public class DServicio {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");
    public static final String[] HEADERS = { "ID", "NOMBRE", "DESCRIPCION", "PRECIO" };

    private SqlConnection connection;

    public DServicio() {
        connection =  new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);    
    }

    public void guardar(String nombre, String descripcion, float precio) throws SQLException, ParseException {

        String query = "INSERT INTO servicios(nombre,descripcion,precio)"
                + " values(?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setFloat(3, precio);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DServicio.java dice: "
                    + "Ocurrio un error al insertar un insumo guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, String nombre, String descripcion, float precio)
            throws SQLException, ParseException {

        String query = "UPDATE servicios SET nombre=?, descripcion=?, precio=?"
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);    
        ps.setString(2, descripcion);
        ps.setFloat(3, precio);
        ps.setInt(4, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DServicio.java dice: "
                    + "Ocurrio un error al modificar un usuario modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM servicios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DServicio.java dice: "
                    + "Ocurrio un error al eliminar un usuario eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> insumos = new ArrayList<>();
        String query = "SELECT * FROM servicios";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            insumos.add(new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("descripcion"),
                    set.getString("precio"),
            });
        }
        return insumos;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> insumos = new ArrayList<>();
        String query = "SELECT s.nombre AS servicio, COUNT(cs.cita_id) AS total_citas FROM servicios s LEFT JOIN citas_servicios cs ON s.id = cs.servicio_id GROUP BY servicio ORDER BY total_citas DESC LIMIT 5";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            insumos.add(new String[] {
                    set.getString("servicio"),
                    set.getString("total_citas"),
            });
        }
        return insumos;
    }

    public String[] ver(int id) throws SQLException {
        String[] insumos = null;
        String query = "SELECT * FROM servicios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            insumos = new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("nombre"),
                    set.getString("descripcion"),
                    set.getString("precio"),
            };
        }
        return insumos;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "servicio agregar [nombre,descripcion,precio]";
        String editar = "servicio modificar [nombre,descripcion,precio]";
        String eliminar = "servicio eliminar [SERVICIO_ID]";
        // String ver = "usuario ver (SERVICIO_ID)";
        String listar = "servicio mostrar";
        String ayu = "servicio ayuda";

        ayudas.add(new String[] { String.valueOf(1), "Registrar SERVICIO", registrar });
        ayudas.add(new String[] { String.valueOf(2), "Editar SERVICIO", editar });
        ayudas.add(new String[] { String.valueOf(3), "Eliminar SERVICIO", eliminar });
        // .add(new String[]{String.valueOf(4), "Ver SERVICIO", ver});
        ayudas.add(new String[] { String.valueOf(4), "Listar SERVICIO", listar });
        ayudas.add(new String[] { String.valueOf(5), "Ver Comandos CU: Usuarios", ayu });

        return ayudas;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
