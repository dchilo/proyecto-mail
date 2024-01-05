/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import interfaces.IEmailEventListener;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.sasl.AuthenticationException;
import utils.Command;
import utils.Email;
import utils.Extractor;

/**
 *
 * @author Ronaldo Rivero
 */
public class MailVerificationThread implements Runnable {

    Dotenv dotenv = Dotenv.configure().load();
    private final  int PORT_POP = Integer.parseInt(dotenv.get("POP_PORT"));
    private final String HOST = dotenv.get("POP_HOST");
    private final String USER = dotenv.get("POP_USER");
    private final String PASSWORD = dotenv.get("POP_PASSWORD");

    
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    
    private IEmailEventListener emailEventListener;

    public IEmailEventListener getEmailEventListener() {
        return emailEventListener;
    }

    public void setEmailEventListener(IEmailEventListener emailEventListener) {
        this.emailEventListener = emailEventListener;
    }
    
    public MailVerificationThread() {
        socket = null;
        input = null;
        output = null;
    }

    @Override
    public void run() {
        while(true) {
            try {
                List<Email> emails = null;
                socket = new Socket(HOST, PORT_POP);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());
                System.out.println("**************** Conexion establecida *************");
                
                authUser(USER, PASSWORD);
                
                int count = getEmailCount();
                if(count > 0 ) {
                    emails = getEmails(count);
                    System.out.println(emails);
                    deleteEmails(count);
                }
                output.writeBytes(Command.quit());
                input.readLine();
                input.close();
                output.close();
                socket.close();
                System.out.println("************** Conexion cerrada ************");
                
                if(count > 0) {
                   emailEventListener.onReceiveEmailEvent(emails);
                }
                
                Thread.sleep(5000);
                
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(MailVerificationThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void authUser(String email, String password) throws IOException {
        if(socket != null && input != null && output != null) {
            input.readLine();
            output.writeBytes(Command.user(email));
            input.readLine();
            output.writeBytes(Command.pass(password));
            String message = input.readLine();
            System.out.println(message);
            if(message.contains("-ERR")) {
                throw new AuthenticationException();
            }
        }
    }
    
    private void deleteEmails(int emails) throws IOException {
        for(int i = 1; i <= emails; i++) {
            output.writeBytes(Command.dele(i));
        }
    }
    
    private int getEmailCount() throws IOException {
        output.writeBytes(Command.stat());
        String line = input.readLine();
        String[] data = line.split(" ");        
        return Integer.parseInt(data[1]);
    }
    
    private List<Email> getEmails(int count) throws IOException {
        List<Email> emails = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            output.writeBytes(Command.retr(i));
            String text = readMultiline();
            emails.add(Extractor.getEmail(text));
        }
        return emails;
    }
    
    private String readMultiline() throws IOException {
        String lines = "";
        while(true) {
            String line = input.readLine();
            if(line == null) {
                throw new IOException("Server no responde (ocurrio un error al abrir el correo)");
            }
            if(line.equals(".")) {
                break;
            }
            lines = lines + "\n" + line;
        }
        return lines;
    }
}
