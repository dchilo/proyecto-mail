/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DCitaServicio;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BCitaServicio {
    private DCitaServicio dCitaServicio;
    
    public BCitaServicio() {
        dCitaServicio = new DCitaServicio();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dCitaServicio.guardar( Integer.parseInt(parametros.get(0)),
                parametros.get(1),Integer.parseInt(parametros.get(2)));
        dCitaServicio.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dCitaServicio.modificar(Integer.parseInt(parametros.get(0)),
                parametros.get(1),Integer.parseInt(parametros.get(2)));
        dCitaServicio.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dCitaServicio.eliminar(Integer.parseInt(parametros.get(0)),parametros.get(1));
        dCitaServicio.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> data = (ArrayList<String[]>) dCitaServicio.listar();
        dCitaServicio.desconectar();
        return data;
    }        
}