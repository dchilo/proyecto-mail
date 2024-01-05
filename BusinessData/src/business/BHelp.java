
package business;

import data.DHelp;

import java.sql.SQLException;
import java.util.ArrayList;

public class BHelp {
    private DHelp dHelp;
    
    public BHelp() {
        dHelp = new DHelp();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> help = (ArrayList<String[]>) dHelp.listar();
        dHelp.desconectar();
        return help;
    }

}
