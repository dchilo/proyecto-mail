/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.analex.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronal
 */
// NO TOCAR
public class TokenCommand {
    private int name; // CASO DE USO
    private int action; // ACCION
    private List<Integer> params; // LISTA DE IDS DE PARAMETROS QUE ESTAN TSP

    public TokenCommand() {
        params = new ArrayList<>();
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<Integer> getParams() {
        return params;
    }

    public void setParams(List<Integer> params) {
        this.params = params;
    }
    
    public void addParams(int pos){
        params.add(pos);
    }
    
    public int getParams(int pos){
        return params.get(pos);
    }
    
    public int countParams(){
        return params.size();
    }
}
