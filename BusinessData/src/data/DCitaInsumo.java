package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class DCitaInsumo {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "IDCITA", "INSUMO", "CANTIDAD" };
    
    private SqlConnection connection;

    public DCitaInsumo() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int cita_id ,String insumo, int cantidad)
        throws SQLException, ParseException {
        int insumo_id = getIdByNombre(insumo);
        if(insumo_id==-1){
            System.err.println("El insumo no existe");
            throw new SQLException();
        }
        String query = "INSERT INTO citas_insumos (cita_id, insumo_id, cantidad)"
            + " values(?,?,?)";   
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1,cita_id);
        ps.setInt(2, insumo_id);
        ps.setInt(3, cantidad);         
        
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCitainsumo.java dice: "
                    + "Ocurrio un error al insertar un cita-insumo guardar()");
            throw new SQLException();
        }            
    }

    public void modificar(int cita_id,String insumo, int cantidad )
            throws SQLException, ParseException {
        int insumo_id = getIdByNombre(insumo);
        if(insumo_id==-1){
            System.err.println("El insumo no existe");
            throw new SQLException();
        }
        String query = "UPDATE citas_insumos SET cantidad=?"
                + " WHERE cita_id=? and insumo_id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cantidad);
        ps.setInt(2, cita_id);
        ps.setInt(3, insumo_id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCita.java dice: "
                    + "Ocurrio un error al modificar un cita modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int cita_id,String insumo) throws SQLException {
        int insumo_id = getIdByNombre(insumo);
        if(insumo_id==-1){
            System.err.println("El insumo no existe");
            throw new SQLException();
        }
        String query = "DELETE FROM citas_insumos WHERE cita_id =? and insumo_id =?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cita_id);
        ps.setInt(2, insumo_id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DCitainsumo.java dice: "
                    + "Ocurrio un error al eliminar una cita-insumo eliminar()");
            throw new SQLException();
        }            
    }

     public List<String[]> listar() throws SQLException {
        List<String[]> data = new ArrayList<>();
        String query = "SELECT citas_insumos.cita_id,insumos.nombre as nombre_insumo, citas_insumos.cantidad "
                    +"FROM citas_insumos "
                    +"JOIN insumos ON citas_insumos.insumo_id = insumos.id;";                    
        PreparedStatement ps = connection.connect().prepareStatement(query);        
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            data.add(new String[] {
                    String.valueOf(set.getInt("cita_id")),
                    set.getString("nombre_insumo"),
                    String.valueOf(set.getInt("cantidad"))
            });
        }
        return data;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM insumos WHERE nombre=?";
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