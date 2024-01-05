/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DInventario;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BInventario {
    private DInventario dInventario;
    
    public BInventario() {
        dInventario = new DInventario();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dInventario.guardar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), parametros.get(2), parametros.get(3));
        dInventario.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dInventario.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Integer.parseInt(parametros.get(2)), parametros.get(3), parametros.get(4));
        dInventario.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dInventario.eliminar(Integer.parseInt(parametros.get(0)));
        dInventario.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> inventarios = (ArrayList<String[]>) dInventario.listar();
        dInventario.desconectar();
        return inventarios;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> inventarios = (ArrayList<String[]>) dInventario.listarGrafica();
        dInventario.desconectar();
        return inventarios;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dInventario.ayuda();
        return ayudas;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> inventarios = new ArrayList<>();
        inventarios.add(dInventario.ver(Integer.parseInt(parametros.get(0))));
        dInventario.desconectar();
        return inventarios;
    }
    
}
