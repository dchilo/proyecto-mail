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
public class Utils {
    
    public static String FieldLeft(String S, int Ancho){ //Devuelve S con Ancho caracteres (padding derecho con espacios).  (Padding singnifica "relleno").
        return S + Espacios(Ancho-S.length());
    }

    public static String FieldRight(String S, int Ancho){ //Devuelve S con Ancho caracteres (padding izquierdo con espacios).
        return Espacios(Ancho-S.length()) + S;
    }

    public static String FieldCenter(String S, int Ancho){
        if (S.length() > Ancho)
            return S.substring(0, Ancho-1);

        int Padding = (Ancho - S.length())/2;

        S = Espacios(Padding) + S;
        return S + Espacios(Ancho-S.length());
    }

    public static String Espacios(int n){
        final char BLANK = 32;
        return RunLength(BLANK, n);
    }

    public static String RunLength(char c, int n){
        String S = "";
        for (int i=1; i<=n; i++)
            S += c;

        return S;
    }
}
