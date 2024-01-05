
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

public class DCita {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "ID", "CLIENTE_ID", "MOTORIZADO_ID", "FECHA_HORA", "ESTADO", "MONTO_TOTAL" };
    public static final String[] HEADERSROL = { "ID", "ROL" };

    private SqlConnection connection;

    public DCita() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int cliente_id, int motorizado_id, String fecha_hora, String estado, float monto_total)
            throws SQLException, ParseException {

        String query = "INSERT INTO citas(cliente_id,motorizado_id,fecha_hora,estado,monto_total)"
                + " values(?,?,?,?,?)";   
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cliente_id);
        ps.setInt(2, motorizado_id);
        ps.setString(3, fecha_hora);
        ps.setString(4, estado);
        ps.setFloat(5, monto_total);
        System.out.println(ps);
        
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCita.java dice: "
                    + "Ocurrio un error al insertar un cita guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int cliente_id, int motorizado_id, String fecha_hora, String estado, float monto_total)
            throws SQLException, ParseException {

        String query = "UPDATE citas SET cliente_id=?, motorizado_id=?, fecha_hora=?, estado=?, monto_total=?"
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cliente_id);
        ps.setInt(2, motorizado_id);
        ps.setString(3, fecha_hora);
        ps.setString(4, estado);
        ps.setFloat(5, monto_total);
        ps.setInt(6, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCita.java dice: "
                    + "Ocurrio un error al modificar un cita modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM citas WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCita.java dice: "
                    + "Ocurrio un error al eliminar un cita eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> citas = new ArrayList<>();
        String query = "SELECT * FROM citas";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            citas.add(new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("cliente_id"),
                    set.getString("motorizado_id"),
                    set.getString("fecha_hora"),
                    set.getString("estado"),
                    set.getString("monto_total"),
            });
        }
        return citas;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> citas = new ArrayList<>();
        String query = "SELECT estado, COUNT(*) AS cantidad_citas FROM citas GROUP BY estado";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            citas.add(new String[] {
                    set.getString("estado"),
                    set.getString("cantidad_citas"),
            });
        }
        return citas;
    }

    public String[] ver(int id) throws SQLException {
        String[] citas = null;
        String query = "SELECT * FROM citas WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            citas = new String[] {
                    String.valueOf(set.getInt("id")),
                    set.getString("cliente_id"),
                    set.getString("motorizado_id"),
                    set.getString("fecha_hora"),
                    set.getString("estado"),
                    set.getString("monto_total"),
            };
        }
        return citas;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "cita agregar [nombre;proveedor_id;precio;cantidad]";
        String editar = "cita modificar [nombre;proveedor_id;precio;cantidad]";
        String eliminar = "cita eliminar [cita_ID]";
        // String ver = "cita ver (CITA_ID)";
        String listar = "cita mostrar";
        String ayu = "cita ayuda";

        ayudas.add(new String[] { String.valueOf(1), "Registrar CITA", registrar });
        ayudas.add(new String[] { String.valueOf(2), "Editar CITA", editar });
        ayudas.add(new String[] { String.valueOf(3), "Eliminar CITA", eliminar });
        // .add(new String[]{String.valueOf(4), "Ver CITA", ver});
        ayudas.add(new String[] { String.valueOf(4), "Listar CITA", listar });
        ayudas.add(new String[] { String.valueOf(5), "Ver Comandos CU: Usuarios", ayu });

        return ayudas;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
