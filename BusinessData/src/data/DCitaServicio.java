package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class DCitaServicio {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "IDCITA", "SERVICIO", "CANTIDAD" };
    
    private SqlConnection connection;

    public DCitaServicio() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int cita_id ,String servicio, int cantidad)
            throws SQLException, ParseException {
                System.out.println(cita_id);
                System.out.println(servicio);
                System.out.println(cantidad);
        int servicio_id = getIdByNombre(servicio);
        System.out.println(servicio_id);
        if(servicio_id==-1){
            System.err.println("El servicio no existe");
            throw new SQLException();
        }
        String query = "INSERT INTO citas_servicios (cita_id, servicio_id, cantidad)"
            + " values(?,?,?)";   
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cita_id);
        ps.setInt(2, servicio_id);
        ps.setInt(3, cantidad);           
        
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCitaServicio.java dice: "
                    + "Ocurrio un error al insertar un cita-servicio guardar()");
            throw new SQLException();
        }            
    }

    public void modificar(int cita_id,String servicio, int cantidad )
            throws SQLException, ParseException {
        int servicio_id = getIdByNombre(servicio);
        if(servicio_id==-1){
            System.err.println("El servicio no existe");
            throw new SQLException();
        }
        String query = "UPDATE citas_servicios SET cantidad=?"
                + " WHERE cita_id=? and servicio_id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cantidad);
        ps.setInt(2, cita_id);
        ps.setInt(3, servicio_id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCita.java dice: "
                    + "Ocurrio un error al modificar un cita modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int cita_id,String servicio) throws SQLException {
        int servicio_id = getIdByNombre(servicio);
        if(servicio_id==-1){
            System.err.println("El servicio no existe");
            throw new SQLException();
        }
        String query = "DELETE FROM citas_servicios WHERE cita_id =? and servicio_id =?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cita_id);
        ps.setInt(2, servicio_id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCitaServicio.java dice: "
                    + "Ocurrio un error al eliminar una cita-servicio eliminar()");
            throw new SQLException();
        }            
    }

     public List<String[]> listar() throws SQLException {
        List<String[]> data = new ArrayList<>();
        String query = "SELECT citas_servicios.cita_id, servicios.nombre as nombre_servicio, citas_servicios.cantidad "
        + "FROM citas_servicios "
        + "JOIN servicios ON citas_servicios.servicio_id = servicios.id;";
        PreparedStatement ps = connection.connect().prepareStatement(query);        
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            data.add(new String[] {
                    String.valueOf(set.getInt("cita_id")),
                    set.getString("nombre_servicio"),
                    String.valueOf(set.getInt("cantidad"))
            });
        }
        return data;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM servicios WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            id = set.getInt("id");
        } 
        return id;
    }
    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }
}