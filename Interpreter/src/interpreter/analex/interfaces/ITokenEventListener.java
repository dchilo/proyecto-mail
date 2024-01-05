
package interpreter.analex.interfaces;

import interpreter.events.TokenEvent;

//AJUSTAR DE ACUERDO A NUESTRO CU
public interface ITokenEventListener {
    
    void usuario(TokenEvent event); 
    void error(TokenEvent event);
    //agregar mas casos de uso

    void proveedor(TokenEvent event);
    void insumo(TokenEvent event);
    void inventario(TokenEvent event);
    void motorizado(TokenEvent event);
    void servicio(TokenEvent event);
    void pago(TokenEvent event);
    void cita(TokenEvent event);
    void citaInsumo(TokenEvent event);
    void citaServicio(TokenEvent event);
    void help(TokenEvent event);
}
