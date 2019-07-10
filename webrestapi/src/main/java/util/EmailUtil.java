package util;

import enumerate.Status;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Set;

import static util.Messages.Email.RESTRICT_ANSWER_BACK_MESSAGE;

public class EmailUtil {
    private Properties props;

    public EmailUtil() {
        this.props = configure();
    }

    public void sendEmailTo(String email) {
        this.configure();

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            StringBuilder message = new StringBuilder();
            message.append(RESTRICT_ANSWER_BACK_MESSAGE).append(System.lineSeparator())
                    .append(Messages.Email.VACATION_REQUEST);

            msg.setText(message.toString());
            msg.setSubject(Messages.Email.SUBJECT);
            msg.setFrom(new InternetAddress(Setup.Email.email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public void sendEmailTo(Status status, String email_to, String reason) {
        this.configure();

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            StringBuilder message = new StringBuilder();
            message.append(RESTRICT_ANSWER_BACK_MESSAGE).append(System.lineSeparator())
                    .append(Messages.Email.STATUS)
                    .append(status).append(System.lineSeparator())
                    .append(Messages.Email.REASON).append(reason);

            msg.setText(message.toString());
            msg.setSubject(Messages.Email.SUBJECT);
            msg.setFrom(new InternetAddress(Setup.Email.email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email_to));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(Setup.Email.email, Setup.Email.password);
        }
    }

    private Properties configure() {
        Properties props = new Properties();
        props.put("mail.smtp.user", Setup.Email.email);
        props.put("mail.smtp.host", Setup.Email.host);
        props.put("mail.smtp.port", Setup.Email.port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", Setup.Email.port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

}
