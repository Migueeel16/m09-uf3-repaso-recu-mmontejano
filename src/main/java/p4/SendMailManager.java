package p4;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SendMailManager {
    private Session session;

    public SendMailManager() throws IOException {
        Properties serverProperties = new Properties();

        InputStream SMTPServerPropertiesFile = this.getClass().getClassLoader().getResourceAsStream("SMTPServer.properties");
        serverProperties.load(SMTPServerPropertiesFile);
        System.out.println(serverProperties);

        Properties authProperties = new Properties();

        InputStream senderAccountPropertiesFile = this.getClass().getClassLoader().getResourceAsStream("auth.properties");
        authProperties.load(senderAccountPropertiesFile);
        System.out.println(authProperties);

        session = Session.getDefaultInstance(serverProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authProperties.getProperty("mail.user.account"), authProperties.getProperty("mail.app.password"));
            }
        });
    }

    public void sendMail(String recipient, String subject, String body, File[] attachments) throws MessagingException, IOException {
        Message message = new MimeMessage(session);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        if (attachments == null) {
            message.setText(body);
        } else {
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            for (File attachment : attachments) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.attachFile(attachment);
                multipart.addBodyPart(mimeBodyPart);
            }
            message.setContent(multipart);
        }

        Transport.send(message);
    }

    public void sendMail(String recipient, String subject, String body) throws MessagingException, IOException {
        sendMail(recipient, subject, body, null);
    }
}
