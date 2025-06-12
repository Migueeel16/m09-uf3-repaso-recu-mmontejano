package p4;

import javax.mail.*;
import java.io.InputStream;
import java.util.Properties;

public class ReadMailsManager {
    private Session session;
    private Properties IMAPServer;

    public ReadMailsManager() {
        try {
            this.IMAPServer = new Properties();

            InputStream input = this.getClass().getClassLoader().getResourceAsStream("IMAPserver.properties");
            IMAPServer.load(input);
            System.out.println(IMAPServer);

            InputStream inputAuth = this.getClass().getClassLoader().getResourceAsStream("auth.properties");
            Properties senderAccount = new Properties();
            senderAccount.load(inputAuth);

            this.session = Session.getDefaultInstance(IMAPServer, new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(senderAccount.getProperty("mail.user.account"),
                            senderAccount.getProperty("mail.app.password"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            Store store = session.getStore(IMAPServer.getProperty("mail.store.protocol"));
            store.connect(IMAPServer.getProperty("mail.imap.host"),
                    Integer.parseInt(IMAPServer.getProperty("mail.imap.port")),
                    session.getProperty("mail.user"),
                    session.getProperty("mail.password"));

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Sent Date: " + message.getSentDate());

                if (message.isMimeType("text/plain")) {
                    System.out.println("Content: " + message.getContent());
                } else if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            System.out.println("Content: " + bodyPart.getContent());
                            break;
                        }
                    }
                }
                System.out.println("-----------------------------");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            ReadMailsManager readMail = new ReadMailsManager();
            readMail.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}