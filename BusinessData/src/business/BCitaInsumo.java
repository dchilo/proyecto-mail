/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DCitaInsumo;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BCitaInsumo {
    private DCitaInsumo dCitaInsumo;
    
    public BCitaInsumo() {
        dCitaInsumo = new DCitaInsumo();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dCitaInsumo.guardar( Integer.parseInt(parametros.get(0)),
                parametros.get(1),Integer.parseInt(parametros.get(2)));
        dCitaInsumo.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dCitaInsumo.modificar(Integer.parseInt(parametros.get(0)),
                parametros.get(1),Integer.parseInt(parametros.get(2)));
        dCitaInsumo.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dCitaInsumo.eliminar(Integer.parseInt(parametros.get(0)),parametros.get(1));
        dCitaInsumo.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> data = (ArrayList<String[]>) dCitaInsumo.listar();
        System.out.println(data);
        dCitaInsumo.desconectar();
        return data;
    }        
}