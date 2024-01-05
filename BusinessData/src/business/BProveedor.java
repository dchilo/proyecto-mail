/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DProveedor;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BProveedor {
    private DProveedor dProveedor;
    
    public BProveedor() {
        dProveedor = new DProveedor();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dProveedor.guardar(parametros.get(0), parametros.get(1), parametros.get(2),
                parametros.get(3), parametros.get(4));
        dProveedor.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dProveedor.modificar(Integer.parseInt(parametros.get(0)), parametros.get(1), 
                parametros.get(2), parametros.get(3), parametros.get(4), 
                parametros.get(5));
        dProveedor.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dProveedor.eliminar(Integer.parseInt(parametros.get(0)));
        dProveedor.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> proveedores = (ArrayList<String[]>) dProveedor.listar();
        dProveedor.desconectar();
        return proveedores;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> proveedores = (ArrayList<String[]>) dProveedor.listarGrafica();
        dProveedor.desconectar();
        return proveedores;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> proveedores = new ArrayList<>();
        proveedores.add(dProveedor.ver(Integer.parseInt(parametros.get(0))));
        dProveedor.desconectar();
        return proveedores;
    }
     
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dProveedor.ayuda();
        return ayudas;
    }
    
}
