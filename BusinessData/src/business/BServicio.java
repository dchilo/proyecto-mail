/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DServicio;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BServicio {
    private DServicio dServicio;
    
    public BServicio() {
        dServicio = new DServicio();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dServicio.guardar(parametros.get(0), parametros.get(1), Float.parseFloat(parametros.get(2)));
        dServicio.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dServicio.modificar(Integer.parseInt(parametros.get(0)), parametros.get(1), parametros.get(2), Float.parseFloat(parametros.get(3)));
        dServicio.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dServicio.eliminar(Integer.parseInt(parametros.get(0)));
        dServicio.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> servicios = (ArrayList<String[]>) dServicio.listar();
        dServicio.desconectar();
        return servicios;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> servicios = new ArrayList<>();
        servicios.add(dServicio.ver(Integer.parseInt(parametros.get(0))));
        dServicio.desconectar();
        return servicios;
    }
        
    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> servicios = (ArrayList<String[]>) dServicio.listarGrafica();
        dServicio.desconectar();
        return servicios;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dServicio.ayuda();
        return ayudas;
    }
}