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
public class DPago {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "CITA_ID", "MONTO", "FECHA_PAGO", "MOTODO_PAGO"};
    
    private SqlConnection connection;
    
    public DPago() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }
    
    public void guardar(int cita_id, float monto, String fecha_pago, String metodo_pago) throws SQLException, ParseException {
        String query = "INSERT INTO pagos(cita_id,monto,fecha_pago,metodo_pago)"
                + " values(?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cita_id);
        ps.setFloat(2, monto);
        ps.setString(3, fecha_pago);
        ps.setString(4, metodo_pago);
        System.out.println(ps);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al insertar un insumo guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int cita_id, float monto, String fecha_pago, String metodo_pago) 
            throws SQLException, ParseException {
      
        String query = "UPDATE pagos SET cita_id=?, monto=?, fecha_pago=?, metodo_pago=?"
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, cita_id);
        ps.setFloat(2, monto);
        ps.setString(3, fecha_pago);
        ps.setString(4, metodo_pago);
        ps.setInt(5, id);
        
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al modificar un pago modificar()");
            throw new SQLException();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM pagos WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al eliminar un pago eliminar()");
            throw new SQLException();
        }
    }
    
    public List<String[]> listar() throws SQLException {
        List<String[]> pagos = new ArrayList<>();
        String query = "SELECT * FROM pagos";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            pagos.add(new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("cita_id"),
                set.getString("monto"),
                set.getString("fecha_pago"),
                set.getString("metodo_pago"),
            });
        }
        return pagos;
    }
    
    public String[] ver(int id) throws SQLException {
        String[] pagos = null;
        String query = "SELECT * FROM servicios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            pagos = new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("cita_id"),
                set.getString("monto"),
                set.getString("fecha_pago"),
                set.getString("metodo_pago"),
            };
        }        
        return pagos;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> pagos = new ArrayList<>();
        String query = "SELECT EXTRACT(MONTH FROM TO_DATE(pagos.fecha_pago, 'YYYY-MM-DD')) AS mes, SUM(pagos.monto) AS total_pagado FROM pagos GROUP BY mes ORDER BY mes";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            pagos.add(new String[] {
                set.getString("mes"),
                set.getString("total_pagado"),
            });
        }
        return pagos;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "pago agregar [cita_id,monto,fecha_pago,metodo_pago]";
        String editar = "pago modificar [cita_id,monto,fecha_pago,metodo_pago]";
        String eliminar = "pago eliminar [PAGO_ID]";
        //String ver = " pago ver (PAGO_ID)";
        String listar = "pago mostrar";
        String ayu = "pago ayuda";

        ayudas.add(new String[]{String.valueOf(1), "Registrar PAGO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Editar PAGO", editar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar PAGO", eliminar});
        //.add(new String[]{String.valueOf(4), "Ver PAGO", ver});
        ayudas.add(new String[]{String.valueOf(4), "Listar PAGO", listar});
        ayudas.add(new String[]{String.valueOf(5), "Ver Comandos CU: Usuarios", ayu});

        return ayudas;
    }
           
    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }
}
