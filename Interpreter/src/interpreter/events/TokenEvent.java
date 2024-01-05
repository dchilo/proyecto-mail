/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.events;

import interpreter.analex.utils.Token;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 *
 * @author ronal
 */
//NO TOCAR
public class TokenEvent extends EventObject {
    
    private int action;// accion del caso de uso: add. modify, delete y otros
    private List<String> params; // lista de parametros
    
    private String sender;// correo que envio el comando
    
    public TokenEvent(Object source) {
        super(source);
        params = new ArrayList<>();
    }
    
    public TokenEvent(Object source, String sender) {
        super(source);
        this.sender = sender;
        params = new ArrayList<>();
    }
    
    public TokenEvent(Object source, List<String> params, int action){
        super(source);
        this.params = params;
        this.action = action;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }    
    
    public void addParams(String param){
        if(!param.isEmpty())
            params.add(param);
    }
    
    public String getParams(int pos){
        return params.get(pos);
    }
    
    public int countParams(){
        return params.size();
    }
    
    @Override
    public String toString(){
        Token token = new Token();
        String s = "";
        s = s + "Remitente= " + sender + "\n";
        s = s + "Action= " + token.getStringToken(action) + "\n";
        s = s + "Params= \n";
        for(String param : params){
            s = s + "   " + param + "\n";
        }
        return s;
    }
}
