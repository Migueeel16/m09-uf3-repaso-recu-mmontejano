package uf3_examen.ex2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class GestorEnviaCorreu {

    private Session session;

    public GestorEnviaCorreu() throws IOException {

        Properties SMTPServerProperties = new Properties();

        InputStream SMTPServerPropertiesFile = this.getClass().getClassLoader()
                .getResourceAsStream("SMTPserver.properties");
        SMTPServerProperties.load(SMTPServerPropertiesFile);
        System.out.println(SMTPServerProperties);

        Properties senderAccountProperties = new Properties();
        InputStream senderAccountPropertiesFile = this.getClass().getClassLoader()
                .getResourceAsStream("auth.properties");

        senderAccountProperties.load(senderAccountPropertiesFile);

        this.session = Session.getDefaultInstance(SMTPServerProperties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderAccountProperties.getProperty("mail.user.account")
                                , senderAccountProperties.getProperty("mail.app.password"));
                    }
        });


    }

    public void enviaCorreu(String destinatari, String titol, String contingut, File[] adjunts) throws MessagingException, IOException {

        Message message = new MimeMessage(this.session);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatari));
        message.setSubject(titol);

        if(adjunts == null){
            message.setText(contingut);
        } else {
            Multipart multipart = new MimeMultipart();

            MimeBodyPart partContigut = new MimeBodyPart();
            partContigut.setText(contingut);
            multipart.addBodyPart(partContigut);

            for(File file : adjunts){
                MimeBodyPart partAdjunt = new MimeBodyPart();
                partAdjunt.attachFile(file);
                multipart.addBodyPart(partAdjunt);
            }
            message.setContent(multipart);
        }

        System.out.println("Enviant correu...");
        Transport.send(message);
        System.out.println("Correu enviat!");


    }

    public static void main(String[] args) {

        try {
            GestorEnviaCorreu gestorEnviaCorreu = new GestorEnviaCorreu();
            gestorEnviaCorreu.enviaCorreu("jllaberia@iespoblenou.org", "SeAprueba", "Finaaaaaa esto funca+!", null);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }


}