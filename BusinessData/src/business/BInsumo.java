/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DInsumo;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BInsumo {
    private DInsumo dInsumo;
    
    public BInsumo() {
        dInsumo = new DInsumo();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dInsumo.guardar(parametros.get(0), Integer.parseInt(parametros.get(1)),
                Float.parseFloat(parametros.get(2)),Integer.parseInt(parametros.get(3)));
        dInsumo.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dInsumo.modificar(Integer.parseInt(parametros.get(0)), parametros.get(1), Integer.parseInt(parametros.get(2)),
                Float.parseFloat(parametros.get(3)),Integer.parseInt(parametros.get(4)));
        dInsumo.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dInsumo.eliminar(Integer.parseInt(parametros.get(0)));
        dInsumo.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> insumos = (ArrayList<String[]>) dInsumo.listar();
        dInsumo.desconectar();
        return insumos;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> insumos = (ArrayList<String[]>) dInsumo.listarGrafica();
        dInsumo.desconectar();
        return insumos;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dInsumo.ayuda();
        return ayudas;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> insumos = new ArrayList<>();
        insumos.add(dInsumo.ver(Integer.parseInt(parametros.get(0))));
        dInsumo.desconectar();
        return insumos;
    }
    
}