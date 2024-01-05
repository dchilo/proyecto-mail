/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.analex.utils;

import java.util.ArrayList;

/**
 *
 * @author dchil
 */
//NO TOCAR
public class TSParams {
    
    private static String TITLE = "TSParams";
    private ArrayList <String> L;

    /**
     * Conctructor de parametros vacio.
     */
    public TSParams(){
        L = new ArrayList<String>();
    }

    /**
     * Inicializa la TSParams. Es decir, TSParams=(Vacía)
     */
    public void init(){
        L.clear();
    }

    /**
     * Retorna la cantidad de parametros.
     * @return 
     */
    public int length(){
        return L.size();
    }

    /**
     * Devuelve la posición (índice de L) donde se encuentra el Params
     * Si no existe devuelve -1.
     * @param Str
     * @return 
     */
    public int Existe(String Str){ 
        //Str = Str.toUpperCase(); //activar si sus comando son uppercase sensitive
        for (int i=0; i<=length()-1; i++){
            if (L.get(i).equals(Str))
                return i;
        }
        return -1;
    }

    /**
     * Inserta un nuevo Params 
     * Devuelve la posición donde está/fue alojado
     * Si no se pudo insertar return -1.
     * @param Str
     * @return 
     */
    public int add(String Str){ 
        int pos = Existe(Str);
        if (pos !=-1)
            return pos;//La Str ya existe en la TSParams.

        //Str = Str.toUpperCase(); //activar si sus comando son uppercase sensitive
        if(Str.isEmpty())
            return -1;//el parametro es vacío.
        
        L.add(Str);
        return L.size()-1;//La Str fue insertada al final.
    }

    /**
     * Devuelve el Params de la posición Index
     * @param Index
     * @return 
     */
    public String getStr(int Index){
        if (!PosValida(Index)){ //Diverge.
            System.err.println("TSS.getStr:Posición inválida.");
            return "";
        }
        return L.get(Index);
    }


    public boolean PosValida(int Index){
        return (0 <= Index && Index <= length()-1);
    }

//De aqui en adelante son procedimientos auxiliares para imprimir una tabla de TSParams
    @Override
    public String toString(){
        if (length()==0)
            return "("+TITLE+" Vacía)";

        final char   LF ='\n';
        final String PADDLEFT = "   ";

        String Result;
        int n = LongitudFila();

        String BordeSup  = PADDLEFT+' '+Utils.RunLength('_', n);
        String Titulo    = PADDLEFT+'|'+Utils.FieldCenter(TITLE, n)+'|';
        String BordeInf  = PADDLEFT+'+'+Utils.RunLength('-', n)+'+';

        Result = BordeSup + LF + Titulo + LF + BordeInf + LF;

        int FieldPos = PADDLEFT.length();
        for (int i=0; i<=length()-1; i++){
            String Posicion = Utils.FieldRight(""+i, FieldPos);
            String Fila     = '|' +  Utils.FieldLeft('"'+L.get(i)+'"', n) + '|';
            Result += Posicion + Fila + LF;
        }

        return Result+BordeInf+LF;
    }

    private int LongitudFila(){ //Corrutina de print().
        int May=TITLE.length();
        for (int i=0; i<=length()-1; i++){
            int LonStr = L.get(i).length();
            if (LonStr > May)
                May = LonStr;
        }
        return May+2;   //+2 comillas
    }
}
