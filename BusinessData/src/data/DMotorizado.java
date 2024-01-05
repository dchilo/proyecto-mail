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

public class DMotorizado {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = {"ID", "CLIENTE_ID", "MARCA", "MODELO", "AÑO", "PLACA", "ESTADO"};
    
    private SqlConnection connection;
    
    public DMotorizado() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);       
    }
    
    public void guardar(int cliente_id, String marca, String modelo, int anio, String placa, String estado) throws SQLException, ParseException {
        
        String query = "INSERT INTO motorizados(cliente_id,marca,modelo,anio,placa,estado)"
                + " values(?,?,?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cliente_id);
        ps.setString(2, marca);
        ps.setString(3, modelo);
        ps.setInt(4, anio);
        ps.setString(5, placa);
        ps.setString(6, estado);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DMotorizado.java dice: "
                    + "Ocurrio un error al insertar un insumo guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int cliente_id, String marca, String modelo, int anio, String placa, String estado) 
            throws SQLException, ParseException {
        
        String query = "UPDATE motorizados SET cliente_id=?, marca=?, modelo=?, anio=?, placa=?, estado=?"
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cliente_id);
        ps.setString(2, marca);
        ps.setString(3, modelo);
        ps.setInt(4, anio);
        ps.setString(5, placa);
        ps.setString(6, estado);
        ps.setInt(7, id);
     
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DMotorizado.java dice: "
                    + "Ocurrio un error al modificar un motorizado modificar()");
            throw new SQLException();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM motorizados WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DMotorizado.java dice: "
                    + "Ocurrio un error al eliminar un motorizado eliminar()");
            throw new SQLException();
        }
    }
    
    public List<String[]> listar() throws SQLException {
        List<String[]> motorizados = new ArrayList<>();
        String query = "SELECT * FROM motorizados";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            motorizados.add(new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("cliente_id"),
                set.getString("marca"),
                set.getString("modelo"),
                set.getString("anio"),
                set.getString("placa"),
                set.getString("estado"),
            });
        }
        return motorizados;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> motorizados = new ArrayList<>();
        String query = "SELECT estado, COUNT(*) AS total_motorizados FROM motorizados GROUP BY estado";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            motorizados.add(new String[] {
                set.getString("estado"),
                set.getString("total_motorizados"),
            });
        }
        return motorizados;
    }
    
    public String[] ver(int id) throws SQLException {
        String[] insumos = null;
        String query = "SELECT * FROM motorizados WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            insumos = new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("cliente_id"),
                set.getString("marca"),
                set.getString("modelo"),
                set.getString("anio"),
                set.getString("placa"),
                set.getString("estado"),
            };
        }        
        return insumos;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = " motorizado agregar [cliente_id;marca;modelo;anio;placa;estado]";
        String editar = " motorizado modificar [cliente_id;marca;model;anio;placa;estado]";
        String eliminar = " motorizado eliminar [MOTORIZADO_ID]";
        //String ver = " motorizado ver (MOTORIZADO_ID)";
        String listar = " motorizado mostrar";
        String ayu = " motorizado ayuda";

        ayudas.add(new String[]{String.valueOf(1), "Registrar MOTORIZADO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Editar MOTORIZADO", editar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar MOTORIZADO", eliminar});
        //.add(new String[]{String.valueOf(4), "Ver MOTORIZADO", ver});
        ayudas.add(new String[]{String.valueOf(4), "Listar MOTORIZADO", listar});
        ayudas.add(new String[]{String.valueOf(5), "Ver Comandos CU: Usuarios", ayu});

        return ayudas;
    }
           
    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }    
}
