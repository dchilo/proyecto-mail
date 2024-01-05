package sistemacentral;

import business.BCita;
import business.BCitaServicio;
import business.BCitaInsumo;
import business.BInsumo;
import business.BInventario;
import business.BMotorizado;
import business.BPago;
import business.BProveedor;
import business.BServicio;
import business.BUsuario;
import business.BHelp;
import communication.MailVerificationThread;
import communication.SendEmailThread;
import data.DCita;
import data.DInsumo;
import data.DInventario;
import data.DMotorizado;
import data.DPago;
import data.DProveedor;
import data.DServicio;
import data.DUsuario;
import data.DCitaServicio;
import data.DCitaInsumo;
import data.DHelp;
import interfaces.IEmailEventListener;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.analex.utils.Token;
import interpreter.analex.Interpreter;
import interpreter.events.TokenEvent;
import utils.Email;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utils.HtmlBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailApplication implements IEmailEventListener, ITokenEventListener {

    private static final int CONSTRAINTS_ERROR = -2;
    private static final int NUMBER_FORMAT_ERROR = -3;
    private static final int INDEX_OUT_OF_BOUND_ERROR = -4;
    private static final int PARSE_ERROR = -5;
    private static final int AUTHORIZATION_ERROR = -6;
    private static final int VALIDATION_ERROR = -7;

    private MailVerificationThread mailVerificationThread;
    private BUsuario busuario;
    private BProveedor bproveedor;
    private BInsumo binsumo;
    private BInventario binventario;
    private BMotorizado bmotorizado;
    private BServicio bservicio;
    private BPago bpago;
    private BCita bcita;
    private BCitaServicio bcitaservicio;
    private BCitaInsumo bcitainsumo;
    private BHelp bhelp;

    String[] headers2 = { "NRO", "ACCION", "COMANDO" };
    String explication = "Para Realizar una peticion debes seguir la siguiente estructura: CASO_USO ACCION [PARAMETRO1, PARAMETRO2] \n"
            + "Entre los corchetes van los parametros, cada parametro tiene que estar separado por coma y espacio: [, ] ";

    public MailApplication() {
        mailVerificationThread = new MailVerificationThread();
        mailVerificationThread.setEmailEventListener((IEmailEventListener) MailApplication.this);
        // MODELOS NEGOCIO
        busuario = new BUsuario();
        bproveedor = new BProveedor();
        binsumo = new BInsumo();
        binventario = new BInventario();
        bmotorizado = new BMotorizado();
        bservicio = new BServicio();
        bpago = new BPago();
        bcita = new BCita();
        bcitaservicio = new BCitaServicio();
        bcitainsumo = new BCitaInsumo();
        bhelp = new BHelp();
    }

    public void start() {
        Thread thread = new Thread(mailVerificationThread);
        thread.setName("Mail Verfication Thread");
        thread.start();
    }

    @Override
    public void onReceiveEmailEvent(List<Email> emails) {
        for (Email email : emails) {
            Interpreter interpreter = new Interpreter(email.getSubject(), email.getFrom());
            interpreter.setListener(MailApplication.this);
            Thread thread = new Thread((Runnable) interpreter);
            thread.setName("Interpreter Thread");
            thread.start();
        }
    }

    public static boolean validarEmail(String email) {
        // Implementación de la validación de correo electrónico
        String patronCorreo = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(patronCorreo);
    }

    public static boolean validarNumeroTelefono(String telefono) {
        // Implementación de la validación de número de teléfono
        String patronTelefono = "^[0-9]+$";
        return telefono.matches(patronTelefono);
    }

    public static boolean validarTipoUsuario(String tipoUsuario) {
        // Implementación de la validación de tipo de usuario
        return tipoUsuario.equalsIgnoreCase("Administrador") || tipoUsuario.equalsIgnoreCase("Cliente") || tipoUsuario.equalsIgnoreCase("Tecnico");
    }

    private void validarTipoInventario(String tipoInventario) {
        if (!tipoInventario.equalsIgnoreCase("Entrada") && !tipoInventario.equalsIgnoreCase("Salida")) {
            throw new IllegalArgumentException("El tipo_inventario debe ser 'Entrada' o 'Salida'");
        }
    }

    private void validarEstadoMotorizado(String estadoMotorizado) {
        if (!estadoMotorizado.equalsIgnoreCase("Activo") && !estadoMotorizado.equalsIgnoreCase("Inactivo")) {
            throw new IllegalArgumentException("El estado_motorizado debe ser 'Disponible' o 'No disponible'");
        }
    }

    private void validarEstadoCita(String estadoCita) {
        if (!estadoCita.equalsIgnoreCase("Pendiente") && !estadoCita.equalsIgnoreCase("Confirmada")) {
            throw new IllegalArgumentException("El estado_cita debe ser 'Activo' o 'Inactivo'");
        }
    }

    private void validarMetodoPago(String metodoPago) {
        if (!metodoPago.equalsIgnoreCase("Efectivo") && !metodoPago.equalsIgnoreCase("Tarjeta") && !metodoPago.equalsIgnoreCase("QR")) {
            throw new IllegalArgumentException("El metodo_pago debe ser 'Efectivo' o 'Tarjeta' o 'QR'");
        }
    }

    public static void validarFormatoFecha(String fecha) {
        // Establecer el formato deseado para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Para que la validación sea estricta

        try {
            // Intentar parsear la fecha, esto lanzará una excepción si no tiene el formato correcto
            sdf.parse(fecha);
        } catch (ParseException e) {
            throw new IllegalArgumentException("La fecha debe tener el formato 'yyyy-MM-dd'");
        }
    }

    public static void validarFormatoTimestamp(String timestamp) {
        // Establecer el formato deseado para el timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false); // Para que la validación sea estricta
        try {
            // Intentar parsear el timestamp, esto lanzará una excepción si no tiene el formato correcto
            sdf.parse(timestamp);
        } catch (ParseException e) {
            throw new IllegalArgumentException("El timestamp debe tener el formato 'yyyy-MM-dd HH:mm:ss'");
        }
    }


    private void validarParametrosUsuario(List<String> parametros) {
        // Realizar validaciones específicas para el caso de uso Usuario
        if (!validarEmail(parametros.get(4))) {
            throw new IllegalArgumentException("Formato de correo electrónico no válido");
        }
    
        if (!validarNumeroTelefono(parametros.get(3))) {
            throw new IllegalArgumentException("Formato de número de teléfono no válido");
        }
        // Puedes agregar más validaciones según tus necesidades

        if (!validarTipoUsuario(parametros.get(2))) {
            throw new IllegalArgumentException("El tipo de usuario debe ser 'Administrador' o 'Cliente'");
        }
    }

    private void validarParametrosProveedor(List<String> parametros) {
        // Realizar validaciones específicas para el caso de uso Proveedor
        if (!validarEmail(parametros.get(3))) {
            throw new IllegalArgumentException("Formato de correo electrónico no válido");
        }
    
        if (!validarNumeroTelefono(parametros.get(2))) {
            throw new IllegalArgumentException("Formato de número de teléfono no válido");
        }
        // Puedes agregar más validaciones según tus necesidades
    }

    private void validarParametrosInventario(List<String> parametros) {
        validarFormatoFecha(parametros.get(2));
        validarTipoInventario(parametros.get(3));
    }

    private void validarParametrosMotorizado(List<String> parametros) {
        validarEstadoMotorizado(parametros.get(5));
    }

    private void validarParametrosCita(List<String> parametros) {
        validarFormatoTimestamp(parametros.get(2));
        validarEstadoCita(parametros.get(3));
    }

    private void validarParametrosPago(List<String> parametros) {
        validarFormatoFecha(parametros.get(2));
        validarMetodoPago(parametros.get(3));
    }

    @Override
    public void usuario(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosUsuario(event.getParams());
                    busuario.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Usuario registrado correctamente");
                }
                case Token.GET -> {
                    busuario.listar();
                    tableNotifySuccess(event.getSender(), "Lista de usuarios", DUsuario.HEADERS,
                            (ArrayList<String[]>) busuario.listar());
                }
                case Token.DELETE -> {
                    busuario.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Usuario eliminado correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosUsuario(paramsEditar);
                    busuario.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Usuario modificado correctamente");
                }
                case Token.VER -> {
                    busuario.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Usuario", DUsuario.HEADERS,
                            (ArrayList<String[]>) busuario.ver(event.getParams()));
                }
                case Token.GRAFICA -> {
                    busuario.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Usuarios",
                            (ArrayList<String[]>) busuario.listarGrafica());
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso USUARIO",
                        explication, headers2, (ArrayList<String[]>) busuario.ayuda());
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }

    // Nueva función para convertir el mensaje de excepción a List<String>
    private List<String> exceptionToListString(Throwable throwable) {
    if (throwable == null) {
        return Collections.emptyList();
    }

    String mensajeDetalle = throwable.getMessage();
    return Collections.singletonList(mensajeDetalle != null ? mensajeDetalle : "No hay mensaje de detalle disponible.");
    }


    @Override
    public void error(TokenEvent event) {
        handleError(event.getAction(), event.getSender(), event.getParams());
    }

    private void handleError(int type, String email, List<String> args) {
        Email emailObject = null;

        switch (type) {
            case Token.ERROR_CHARACTER -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Caracter desconocido",
                            "No se pudo ejecutar el comando [" + args.get(0) + "] debido a: ",
                            "El caracter \"" + args.get(1) + "\" es desconocido."
                    }));
            case Token.ERROR_COMMAND -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Comando desconocido",
                            "No se pudo ejecutar el comando [" + args.get(0) + "] debido a: ",
                            "No se reconoce la palabra \"" + args.get(1) + "\" como un comando válido",
                            "Ejecute el comando 'help'"
                    }));
            case CONSTRAINTS_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Problemas con la base de datos",
                            "La información que deseas ingresar es incorrecta",
                            "Ejecute el comando 'help'"
                    }));
            case NUMBER_FORMAT_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Error en el tipo de parámetro",
                            "El tipo de uno de los parámetros es incorrecto",
                            "Ejecute el comando 'help'"
                    }));
            case INDEX_OUT_OF_BOUND_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Cantidad de parámetros incorrecta",
                            "La cantidad de parámetros para realizar la acción es incorrecta",
                            "Ejecute el comando 'help'"
                    }));
            case PARSE_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Error al procesar la fecha",
                            "La fecha introducida posee un formato incorrecto",
                            "Ejecute el comando 'help'"
                    }));
            case AUTHORIZATION_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Acceso denegado",
                            "Usted no posee los permisos necesarios para realizar la acción solicitada"
                    }));
            case VALIDATION_ERROR -> emailObject = new Email(email, Email.SUBJECT,
                    HtmlBuilder.generateText(new String[] {
                            "Error de validación",
                            args.get(0)
                    }));
        }
        sendEmail(emailObject);
    }

    private void simpleNotifySuccess(String email, String message) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateText(new String[] {
                        "Petición realizada correctamente",
                        message
                }));
        sendEmail(emailObject);
    }

    private void simpleNotifySuccessPago(String email, List<String> params) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTextPago(new String[] {
                        params.get(1)
                }));
        sendEmail(emailObject);
    }

    /*
    private void simpleNotify(String email, String title, String topic, String message) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateText(new String[] {
                        title, topic, message
                }));
        sendEmail(emailObject);
    }
    */

    private void tableNotifySuccess(String email, String title, String[] headers, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTable(title, headers, data));
        sendEmail(emailObject);
    }

    private void tableNotifySuccessHelp(String email, String title, String explication, String[] headers,
            ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTableHelp(title, explication, headers, data));
        sendEmail(emailObject);
    }

    private void tableGraficaSuccess(String email, String title, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateGrafica(title, data));
        sendEmail(emailObject);
    }

    /* 
    private void simpleTableNotifySuccess(String email, String title, String[] headers, String[] data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTableForSimpleData(title, headers, data));
        sendEmail(emailObject);
    }
    */

    private void tableGraficaPago(String email, String title, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateGraficaPago(title, data));
        sendEmail(emailObject);
    }

    private void sendEmail(Email email) {
        SendEmailThread sendEmail = new SendEmailThread(email);
        Thread thread = new Thread(sendEmail);
        thread.setName("Send email Thread");
        thread.start();
    }

    /* 
    private void generateCharBar(String email, String message) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateCharBar(new String[] {
                        "Petición realizada correctamente",
                        message
                }));
        sendEmail(emailObject);
    }
    */

    /*
    private void generatePieChart(String email) {
        Email emailObject = new Email(email, Email.SUBJECT, "Gráfica");
        // emailObject.setImageFilePath(HtmlBuilder.generateChartPieImage());
        sendEmail(emailObject);
    }
    */

    @Override
    public void proveedor(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosProveedor(event.getParams());
                    bproveedor.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Proveedor registrado correctamente");
                }
                case Token.GET -> {
                    bproveedor.listar();
                    tableNotifySuccess(event.getSender(), "Lista de proveedores", DProveedor.HEADERS,
                            (ArrayList<String[]>) bproveedor.listar());
                }
                case Token.DELETE -> {
                    bproveedor.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Proveedor eliminado correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosProveedor(paramsEditar);
                    bproveedor.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Proveedor modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso PROVEEDOR",
                        explication, headers2, (ArrayList<String[]>) bproveedor.ayuda());
                case Token.GRAFICA -> {
                    bproveedor.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Total Insumos por Proveedor",
                            (ArrayList<String[]>) bproveedor.listarGrafica());
                }
                case Token.VER -> {
                    bproveedor.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Proveedor", DProveedor.HEADERS,
                            (ArrayList<String[]>) bproveedor.ver(event.getParams()));
                }
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }

    @Override
    public void insumo(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    binsumo.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Insumo registrado correctamente");
                }
                case Token.GET -> {
                    binsumo.listar();
                    tableNotifySuccess(event.getSender(), "Lista de insumo", DInsumo.HEADERS,
                            (ArrayList<String[]>) binsumo.listar());
                }
                case Token.DELETE -> {
                    binsumo.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Insumo eliminado correctamente");
                }
                case Token.MODIFY -> {
                    binsumo.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Insumo modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso INSUMO",
                        explication, headers2, (ArrayList<String[]>) binsumo.ayuda());
                case Token.VER -> {
                    binsumo.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Insumo", DInsumo.HEADERS,
                            (ArrayList<String[]>) binsumo.ver(event.getParams()));
                }
                case Token.GRAFICA -> {
                    binsumo.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Insumos",
                            (ArrayList<String[]>) binsumo.listarGrafica());
                }
            }
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void inventario(TokenEvent event) {
        try {

            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosInventario(event.getParams());
                    binventario.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Inventario registrado correctamente");
                }
                case Token.GET -> {
                    binventario.listar();
                    tableNotifySuccess(event.getSender(), "Lista de inventario", DInventario.HEADERS,
                            (ArrayList<String[]>) binventario.listar());
                }
                case Token.DELETE -> {
                    binventario.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Inventario eliminado correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosInventario(paramsEditar);
                    binventario.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Inventario modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso INVENTARIO",
                        explication, headers2, (ArrayList<String[]>) binventario.ayuda());
                case Token.GRAFICA -> {
                    binventario.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Inventario",
                            (ArrayList<String[]>) binventario.listarGrafica());
                }
                case Token.VER -> {
                    binventario.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Inventario", DInventario.HEADERS,
                            (ArrayList<String[]>) binventario.ver(event.getParams()));
                }
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }

    @Override
    public void motorizado(TokenEvent event) {
        try {

            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosMotorizado(event.getParams());
                    bmotorizado.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Motorizado registrado correctamente");
                }
                case Token.GET -> {
                    bmotorizado.listar();
                    tableNotifySuccess(event.getSender(), "Lista de motorizados", DMotorizado.HEADERS,
                            (ArrayList<String[]>) bmotorizado.listar());
                }
                case Token.DELETE -> {
                    bmotorizado.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Motorizado eliminado correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosMotorizado(paramsEditar);
                    bmotorizado.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Motorizado modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso MOTORIZADO",
                        explication, headers2, (ArrayList<String[]>) bmotorizado.ayuda());
                case Token.VER -> {
                    bmotorizado.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Motorizado", DMotorizado.HEADERS,
                            (ArrayList<String[]>) bmotorizado.ver(event.getParams()));
                }
                case Token.GRAFICA -> {
                    bmotorizado.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Motorizados",
                            (ArrayList<String[]>) bmotorizado.listarGrafica());
                }
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }

    @Override
    public void servicio(TokenEvent event) {
        try {

            switch (event.getAction()) {
                case Token.ADD -> {
                    bservicio.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Servicio registrado correctamente");
                }
                case Token.GET -> {
                    bservicio.listar();
                    tableNotifySuccess(event.getSender(), "Lista de servicios", DServicio.HEADERS,
                            (ArrayList<String[]>) bservicio.listar());
                }
                case Token.DELETE -> {
                    bservicio.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Servicio eliminado correctamente");
                }
                case Token.MODIFY -> {
                    bservicio.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Servicio modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso SERVICIO",
                        explication, headers2, (ArrayList<String[]>) bservicio.ayuda());
                case Token.GRAFICA -> {
                    bservicio.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de Total de Servicios",
                            (ArrayList<String[]>) bservicio.listarGrafica());
                }
                case Token.VER -> {
                    bservicio.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Servicio", DServicio.HEADERS,
                            (ArrayList<String[]>) bservicio.ver(event.getParams()));
                }
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void pago(TokenEvent event) {
        try {

            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosPago(event.getParams());
                    bpago.guardar(event.getParams());
                    simpleNotifySuccessPago(event.getSender(), event.getParams());
                }
                case Token.GET -> {
                    bpago.listar();
                    tableNotifySuccess(event.getSender(), "Lista de pagos", DPago.HEADERS,
                            (ArrayList<String[]>) bpago.listar());
                }
                case Token.DELETE -> {
                    bpago.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Pago eliminado correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosPago(paramsEditar);
                    bpago.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Pago modificado correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso PAGO",
                        explication, headers2, (ArrayList<String[]>) bpago.ayuda());
                case Token.GRAFICA -> {
                    bpago.listarGrafica();
                    tableGraficaPago(event.getSender(), "Gráfica de pagos",
                            (ArrayList<String[]>) bpago.listarGrafica());
                }
                case Token.VER -> {
                    bpago.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Pago", DPago.HEADERS,
                            (ArrayList<String[]>) bpago.ver(event.getParams()));
                }
            }

        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }

    @Override
    public void cita(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    validarParametrosCita(event.getParams());
                    bcita.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Cita registrado correctamente");
                }
                case Token.GET -> {
                    bcita.listar();
                    tableNotifySuccess(event.getSender(), "Lista de citas", DCita.HEADERS,
                            (ArrayList<String[]>) bcita.listar());
                }
                case Token.DELETE -> {
                    bcita.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Cita eliminada correctamente");
                }
                case Token.MODIFY -> {
                    List<String> paramsEditar = new ArrayList<>(event.getParams());
                    paramsEditar.remove(0);
                    validarParametrosCita(paramsEditar);
                    bcita.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "Cita modificada correctamente");
                }
                case Token.AYUDA -> tableNotifySuccessHelp(event.getSender(), "Comandos para el caso de uso CITA",
                        explication, headers2, (ArrayList<String[]>) bcita.ayuda());
                case Token.GRAFICA -> {
                    bcita.listarGrafica();
                    tableGraficaSuccess(event.getSender(), "Gráfica de los Citas",
                            (ArrayList<String[]>) bcita.listarGrafica());
                }
                case Token.VER -> {
                    bcita.ver(event.getParams());
                    tableNotifySuccess(event.getSender(), "Cita", DCita.HEADERS,
                            (ArrayList<String[]>) bcita.ver(event.getParams()));
                }
            }
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            handleError(VALIDATION_ERROR, event.getSender(), exceptionToListString(ex));
        }
    }
    @Override
    public void citaServicio(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    bcitaservicio.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio registrado correctamente");
                }
                case Token.GET -> {
                    bcitaservicio.listar();
                    tableNotifySuccess(event.getSender(), "Lista de CitaServicio", DCitaServicio.HEADERS,
                            (ArrayList<String[]>) bcitaservicio.listar());
                }
                case Token.DELETE -> {
                    bcitaservicio.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio eliminado correctamente");
                }
                case Token.MODIFY -> {
                    bcitaservicio.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio modificado correctamente");
                }
            }
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void citaInsumo(TokenEvent event) {
        try {
            switch (event.getAction()) {
                case Token.ADD -> {
                    bcitainsumo.guardar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio registrado correctamente");
                }
                case Token.GET -> {
                    System.out.println("entro");
                    bcitainsumo.listar();
                    tableNotifySuccess(event.getSender(), "Lista de CitaServicio", DCitaInsumo.HEADERS,
                            (ArrayList<String[]>) bcitainsumo.listar());
                }
                case Token.DELETE -> {
                    bcitainsumo.eliminar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio eliminado correctamente");
                }
                case Token.MODIFY -> {
                    bcitainsumo.modificar(event.getParams());
                    simpleNotifySuccess(event.getSender(), "CitaServicio modificado correctamente");
                }
            }
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(MailApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void help(TokenEvent event) {
        try {
            bhelp.listar();
                    tableNotifySuccess(event.getSender(), "Lista de Comandos", DHelp.HEADERS,
                            (ArrayList<String[]>) bhelp.listar());
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (SQLException exes) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }
}
