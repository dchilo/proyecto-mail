/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import business.BProveedor;
import business.BUsuario;
import business.BInsumo;
import interpreter.analex.Interpreter;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.analex.utils.Token;
import interpreter.events.TokenEvent;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronal
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //AGREGAR USUARIO [Ronaldo, Rivero Gonzales, 76042142]
        //Nombre del caso uso | accion | parametros
        //[mascota|add|Firulay|25|Negro]
        //mascota add 
        //String comando = "producto agregar [Producto 200; 125]";
        Dotenv dotenv = Dotenv.configure().load();
        final String toMAIL = dotenv.get("SMTP_MAIL");
        String comando = "usuario modificar [8; Fernando; Gomez; Cliente; 555666777; fernando.gomez@example.com]";
        //String comando = "usuario eliminar [9]";
        //String comando = "insumo mostrar ";
        String correo = toMAIL;
        /*
        //{Firulay, 25, Negro} //
        //CU: String - producto
        //ACTION: String - agregar
        //PARAMETROS: List<String> [Firulay,25,Negro]
        
        String CU = "producto";
        String ACTION = "agregar";
        List<String> PARAMETROS = new ArrayList<>();
        PARAMETROS.add("Producto 100"); PARAMETROS.add("25");
        
        BUsuario bUsuario = new BUsuario();
        BProducto bProducto = new BProducto();
            try {
                if(CU == "usuario") {
                    if(ACTION == "agregar") {

                    } else if(ACTION == "modificar") {

                    } else if(ACTION == "eliminar") {

                    } else if(ACTION == "listar") {

                    } else if(ACTION == "ver") {

                    }            
                } else if(CU == "producto") {
                    if(ACTION == "agregar") {                        
                        bProducto.guardar(PARAMETROS, correo);
                        System.out.println("Guardado del producto exitoso");
                        //enviar un correo de notificacion al usuario
                    } else if(ACTION == "modificar") {

                    } else if(ACTION == "eliminar") {

                    } else if(ACTION == "listar") {

                    } else if(ACTION == "ver") {

                    } else {
                        System.out.println("Comando no recorrido");
                        //enviar un correo notificando el error
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        */

        BUsuario bUsuario = new BUsuario();
        BProveedor bProveedor = new BProveedor();
        BInsumo bInsumo = new BInsumo();
        
        Interpreter interpreter = new Interpreter(comando, correo);       
        interpreter.setListener(new ITokenEventListener() {
            
            @Override
            public void usuario(TokenEvent event) {
                System.out.println("CU: USUARIO");
                System.out.println(event);  
                System.out.println(Token.ADD);
                System.out.println(Token.DELETE);
                System.out.println(event.getAction());
                
                try {
                    switch (event.getAction()) {
                        case Token.GET:
                            ArrayList<String[]> lista = bUsuario.listar();
                            String s = "";
                            for(int i = 0; i < lista.size(); i++) {
                                s = s + "["+i+"] : ";
                                for(int j = 0; j <lista.get(i).length; j++) {
                                    s = s + lista.get(i)[j] + " | ";
                                }
                                s = s + "\n";
                            }   System.out.println(s);
                            break;
                        case Token.ADD:
                            bUsuario.guardar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.MODIFY:
                            bUsuario.guardar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.DELETE:
                            bUsuario.eliminar(event.getParams());
                            System.out.println("Deleted");
                            break;
                        default:
                            break;
                    }
                 
                } catch (SQLException ex) {
                    System.out.println("Mensaje: "+ex.getSQLState());
                    //enviar notificacion de error
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void proveedor(TokenEvent event) {
                try {
                    System.out.print("Hola");
                    switch (event.getAction()) {
                        case Token.GET:
                            System.out.print("Hola");
                            ArrayList<String[]> lista = bProveedor.listar();
                            String s = "";
                            for(int i = 0; i < lista.size(); i++) {
                                s = s + "["+i+"] : ";
                                for(int j = 0; j <lista.get(i).length; j++) {
                                    s = s + lista.get(i)[j] + " | ";
                                }
                                s = s + "\n";
                            }   System.out.println(s);
                            break;
                            
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void error(TokenEvent event) {
                System.out.println("DESCONOCIDO");
                System.out.println(event);
                //enviar una notificacion
            }

            @Override
            public void insumo(TokenEvent event) {
                System.out.println(event);  
                System.out.println(event.getAction());
                try {
                    switch (event.getAction()) {
                        case Token.GET:
                            ArrayList<String[]> lista = bInsumo.listar();
                            String s = "";
                            for(int i = 0; i < lista.size(); i++) {
                                s = s + "["+i+"] : ";
                                for(int j = 0; j <lista.get(i).length; j++) {
                                    s = s + lista.get(i)[j] + " | ";
                                }
                                s = s + "\n";
                            }   System.out.println(s);
                            break;
                        case Token.ADD:
                            bInsumo.guardar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.MODIFY:
                            bInsumo.guardar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.DELETE:
                            bInsumo.eliminar(event.getParams());
                            System.out.println("Deleted");
                            break;
                        default:
                            break;
                    }
                 
                } catch (SQLException ex) {
                    System.out.println("Mensaje: "+ex.getSQLState());
                    //enviar notificacion de error
                } catch (ParseException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void inventario(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void motorizado(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void servicio(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void pago(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            @Override
            public void citaInsumo(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            @Override
            public void citaServicio(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
        
        
        Thread thread = new Thread(interpreter);
        thread.setName("Interpreter Thread");
        thread.start();
    }

    
}
