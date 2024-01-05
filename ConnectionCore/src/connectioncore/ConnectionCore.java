/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package connectioncore;

import communication.SendEmailThread;
import io.github.cdimascio.dotenv.Dotenv;
import utils.Email;

/**
 *
 * @author dchil
 */
public class ConnectionCore {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().load();
        final String toMAIL = dotenv.get("SMTP_MAIL");

        Email emailObject = new Email(toMAIL, Email.SUBJECT,
                "Peticion Realizada Correctamente");

        SendEmailThread sendEmail = new SendEmailThread(emailObject);
        Thread thread = new Thread(sendEmail);
        thread.setName("send Email Thread");
        thread.start();
    }
    /*
     * MailVerificationThread mail = new MailVerificationThread();
     * mail.setEmailEventListener(new IEmailEventListener() {
     * 
     * @Override
     * public void onReceiveEmailEvent(List<Email> emails) {
     * for (Email email : emails) {
     * System.out.println(email);
     * }
     * }
     * });
     * 
     * Thread thread = new Thread(mail);
     * thread.setName("Mail Verification Thread");
     * thread.start();
     * }
     */
}