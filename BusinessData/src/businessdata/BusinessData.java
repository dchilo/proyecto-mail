/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessdata;

import business.BProveedor;
import business.BReporte;
import business.BUsuario;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        reporte();
    }

    public static void reporte() {
        BReporte bReporte = new BReporte();
        System.out.println();
        try {
            bReporte.guardar();                   
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(BusinessData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public static void usuario() {
        BUsuario bUsuariro = new BUsuario();
        List<String> proveedores = new ArrayList<String>();
        proveedores.add("Carla");
        proveedores.add("Romero Suarez");
        proveedores.add("1");
        proveedores.add("76042143");
        proveedores.add("carlaromero@uagrm.edu.bo");
        System.out.println(proveedores);
     
        try {
            bUsuariro.guardar(proveedores);            
            
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(BusinessData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void proveedor() {
        BProveedor bProveedores = new BProveedor();
        List<String> proveedores = new ArrayList<String>();
        proveedores.add("Intel");
        proveedores.add("Carlos Perez");
        proveedores.add("75893456");
        proveedores.add("carlos.perez@gmail.com");
        proveedores.add("Calle 24 de Septiembre");
        System.out.println(proveedores);
     
        try {
            bProveedores.guardar(proveedores);            
            
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(BusinessData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
