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

public class DInventario {

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = {"ID", "INSUMO_ID", "CANTIDAD", "FECHA_MOVIMIENTO", "TIPO_MOVIMIENTO"};
    
    private SqlConnection connection;
    
    public DInventario() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);  
    }
    
    public void guardar(int insumo_id, int cantidad, String fecha_movimiento, String tipo_movimiento) throws SQLException, ParseException {
        
        String query = "INSERT INTO inventarios(nombre,proveeodr_id,precio,cantidad)"
                + " values(?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, insumo_id);
        ps.setInt(2, cantidad);
        ps.setString(3, fecha_movimiento);
        ps.setString(4, tipo_movimiento);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al insertar un inventario guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int insumo_id, int cantidad, String fecha_movimiento, String tipo_movimiento) 
            throws SQLException, ParseException {
        
        String query = "UPDATE inventarios SET insumo_id=?, cantidad=?, fecha_movimiento=?, tipo_movimiento=?"
                + " WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);

        ps.setInt(1, insumo_id);
        ps.setInt(2, cantidad);
        ps.setString(3, fecha_movimiento);
        ps.setString(4, tipo_movimiento);
        ps.setInt(5, id);
        System.out.println(ps);
        
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al modificar un inventario modificar()");
            throw new SQLException();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM inventarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al eliminar un inventario eliminar()");
            throw new SQLException();
        }
    }
    
    public List<String[]> listar() throws SQLException {
        List<String[]> inventarios = new ArrayList<>();
        String query = "SELECT * FROM inventarios";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            inventarios.add(new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("insumo_id"),
                set.getString("cantidad"),
                set.getString("fecha_movimiento"),
                set.getString("tipo_movimiento"),
            });
        }
        return inventarios;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> inventarios = new ArrayList<>();
        String query = "SELECT i.nombre AS insumo, SUM(inv.cantidad) AS cantidad_disponible FROM insumos i INNER JOIN inventarios inv ON i.id = inv.insumo_id WHERE inv.tipo_movimiento = 'Entrada' GROUP BY insumo";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while(set.next()) {
            inventarios.add(new String[] {
                set.getString("insumo"),
                set.getString("cantidad_disponible"),
            });
        }
        return inventarios;
    }
    
    public String[] ver(int id) throws SQLException {
        String[] inventarios = null;
        String query = "SELECT * FROM inventarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
                
        ResultSet set = ps.executeQuery();
        if(set.next()) {
            inventarios = new String[] {
                String.valueOf(set.getInt("id")),
                set.getString("insumo_id"),
                set.getString("cantidad"),
                set.getString("fecha_movimiento"),
                set.getString("tipo_movimiento"),
            };
        }        
        return inventarios;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "inventario agregar [nombre;proveeodr_id;precio;cantidad]";
        String editar = "inventario modificar [nombre;proveeodr_id;precio;cantidad]";
        String eliminar = "inventario eliminar [INVENTARIO_ID]";
        //String ver = "inventario ver (INVENTARIO_ID)";
        String listar = "inventario mostrar";
        String ayu = "inventario ayuda";

        ayudas.add(new String[]{String.valueOf(1), "Registrar INVENTARIO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Editar INVENTARIO", editar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar INVENTARIO", eliminar});
        //.add(new String[]{String.valueOf(4), "Ver INVENTARIO", ver});
        ayudas.add(new String[]{String.valueOf(4), "Listar INVENTARIO", listar});
        ayudas.add(new String[]{String.valueOf(5), "Ver Comandos CU: Usuarios", ayu});

        return ayudas;
    }
           
    public void desconectar() {
        if(connection != null) {
            connection.closeConnection();
        }
    }    
}
