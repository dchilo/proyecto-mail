/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DPago;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BPago {
    private DPago dPago;
    
    public BPago() {
        dPago = new DPago();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dPago.guardar(Integer.parseInt(parametros.get(0)), Float.parseFloat(parametros.get(1)), parametros.get(2), parametros.get(3));
        dPago.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dPago.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Float.parseFloat(parametros.get(2)), parametros.get(3), parametros.get(4));
        dPago.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dPago.eliminar(Integer.parseInt(parametros.get(0)));
        dPago.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> pagos = (ArrayList<String[]>) dPago.listar();
        dPago.desconectar();
        return pagos;
    }   

    public ArrayList<String[]> listarGrafica() throws SQLException{
        ArrayList<String[]> pagos =  (ArrayList<String[]>) dPago.listarGrafica();
        dPago.desconectar();
        return pagos;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dPago.ayuda();
        return ayudas;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> pagos = new ArrayList<>();
        pagos.add(dPago.ver(Integer.parseInt(parametros.get(0))));
        dPago.desconectar();
        return pagos;
    }
}
