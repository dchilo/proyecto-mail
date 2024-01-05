/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package business;

import data.DMotorizado;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dchil
 */
public class BMotorizado {
    private DMotorizado dMotorizado;
    
    public BMotorizado() {
        dMotorizado = new DMotorizado();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dMotorizado.guardar(Integer.parseInt(parametros.get(0)), parametros.get(1), parametros.get(2), Integer.parseInt(parametros.get(3)), parametros.get(4), parametros.get(5));
        dMotorizado.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dMotorizado.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), parametros.get(2), parametros.get(3), Integer.parseInt(parametros.get(4)), parametros.get(5), parametros.get(6));
        dMotorizado.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dMotorizado.eliminar(Integer.parseInt(parametros.get(0)));
        dMotorizado.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> motorizados = (ArrayList<String[]>) dMotorizado.listar();
        dMotorizado.desconectar();
        return motorizados;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> motorizados = (ArrayList<String[]>) dMotorizado.listarGrafica();
        dMotorizado.desconectar();
        return motorizados;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dMotorizado.ayuda();
        return ayudas;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> motorizados = new ArrayList<>();
        motorizados.add(dMotorizado.ver(Integer.parseInt(parametros.get(0))));
        dMotorizado.desconectar();
        return motorizados;
    }
    
}
