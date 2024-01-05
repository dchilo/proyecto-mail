
package business;

import data.DCita;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BCita {
    private DCita dCita;
    
    public BCita() {
        dCita = new DCita();
    }
    
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dCita.guardar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)),
                parametros.get(2), parametros.get(3), Float.parseFloat(parametros.get(4)));
        dCita.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dCita.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Integer.parseInt(parametros.get(2)),
                parametros.get(3), parametros.get(4), Float.parseFloat(parametros.get(5)));
        dCita.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dCita.eliminar(Integer.parseInt(parametros.get(0)));
        dCita.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> citas = (ArrayList<String[]>) dCita.listar();
        dCita.desconectar();
        return citas;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> citas = (ArrayList<String[]>) dCita.listarGrafica();
        dCita.desconectar();
        return citas;
    }
    
    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = (ArrayList<String[]>) dCita.ayuda();
        return ayudas;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> citas = new ArrayList<>();
        citas.add(dCita.ver(Integer.parseInt(parametros.get(0))));
        dCita.desconectar();
        return citas;
    }
    
}