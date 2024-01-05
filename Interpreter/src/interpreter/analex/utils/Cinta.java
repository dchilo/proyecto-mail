/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.analex.utils;

/**
 *
 * @author dchil
 */
//NO TOCAR
public class Cinta {
    public static final char EOF = 0;
    
    private String cinta;
    private int i;
    
    /**
     * Contructor recibe como parametro un String para trabajarlo como cinta
     * @param cinta 
     */
    public Cinta(String cinta){
        this.cinta = cinta;
        this.i = 0;
    }
    
    /**
     * El procedimiento forward avanza el cabezal de la cinta.
     */
    public void forward(){
        if(i <= cinta.length()){
            i++;
        } else {
            System.err.println("Class Cinta.forward dice: \n"
                    + " No se puede avanzar mas el cabezal.");
        }
    }
    
    /**
     * La funcion CC devuelve el character en el que esta el cabezal de la cinta
     * @return 
     */
    public char CC(){
        if(i < cinta.length()){
            return cinta.charAt(i);
        }
        return EOF;
    }
    
    /**
     * El procedimiento init reinicia el cabezal al inicio.
     */
    public void init(){
        i = 0;
    }
}
