package uf3_pvp_ex2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailManager {
    private Session session;

    public EmailManager() throws IOException {
        Properties serverProperties = new Properties();

        InputStream SMTPServerPropertiesFile = this.getClass().getClassLoader().getResourceAsStream("SMTPServer.properties");
        serverProperties.load(SMTPServerPropertiesFile);

        Properties authProperties = new Properties();

        InputStream senderAccountPropertiesFile = this.getClass().getClassLoader().getResourceAsStream("auth.properties");
        authProperties.load(senderAccountPropertiesFile);

        session = Session.getDefaultInstance(serverProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authProperties.getProperty("mail.user.account"), authProperties.getProperty("mail.app.password"));
            }
        });
    }

    public void sendMail(String recipient, String subject, String body, File attachment) throws MessagingException, IOException {
        Message message = new MimeMessage(session);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        if (attachment == null) {
            message.setText(body);
        } else {
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.attachFile(attachment);
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
        }

        Transport.send(message);
    }
}
