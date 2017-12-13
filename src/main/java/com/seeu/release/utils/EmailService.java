package com.seeu.release.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by neo on 25/11/2017.
 */
@Service
public class EmailService {

    private String host = "seeucoco.com";
    private int port = 455;
    private String from = "shopper@seeucoco.com";
    private String username = "shopper@seeucoco.com";
    private String password = "SEEUps000";

    Boolean isSSL = true;

    /**
     * 默认采取 apache 方式，然后第二种 JavaMail
     *
     * @param who
     * @param title
     * @param text
     * @return
     * @throws Exception
     */
    public boolean send(String who, String title, String text) throws AddressException, MessagingException, EmailException {
        return sendApacheMail(who, title, text) ? true : sendJavaMail2(who, title, text);
    }

    /**
     * Java Mail
     *
     * @param who
     * @param title
     * @param text
     * @return
     * @throws Exception
     */
    private boolean sendJAMail(String who, String title, String text) throws AddressException, MessagingException {


        boolean isAuth = true;

        Properties props = System.getProperties();
        props.put("mail.smtp.ssl.enable", isSSL);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", isAuth);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(who));
            message.setSubject(title);
            message.setContent(text, "text/html; charset=UTF-8");


            Transport.send(message);
            return true;
        } catch (AddressException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    /**
     * Apache Mail
     *
     * @param who
     * @param title
     * @param text
     * @return
     * @throws Exception
     */
    private boolean sendApacheMail(String who, String title, String text) throws EmailException {

        String to = who;
        try {
//            Email email = new SimpleEmail();
            HtmlEmail email = new HtmlEmail();
            email.setDebug(true);

            email.setCharset("UTF-8");
            HashMap map = new HashMap<>();
            map.put("X-Mailer", "SeeU Studio Mailer");
            String now = "" + new Date().getTime();
            map.put("Content-ID", "SeeUCreater" + (now).substring(4, now.length()));
            email.setHeaders(map);
            email.setSentDate(new Date());
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthentication(username, password);
            email.setFrom(from, from);
            email.addTo(to);
            email.setSubject(title);
//            email.setMsg(text);
//            email.setContent(text, "text/html; charset=UTF-8");
            email.setHtmlMsg("<html><body><pre>" + text + "</pre></body></html>");
            email.setSSLOnConnect(isSSL);

            email.send();
        } catch (EmailException e) {
            return false;
        }
        return true;
    }

    /**
     * JavaMail 第二种实现方式
     *
     * @param who
     * @param title
     * @param text
     * @return
     * @throws Exception
     */
    private boolean sendJavaMail2(String who, String title, String text) throws MessagingException {


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.ssl.enable", isSSL ? "true" : "false");
        props.setProperty("mail.smtp.host", "" + host);
        props.setProperty("mail.smtp.port", "" + port);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug", "true");

        try {
            Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(who)); // 邮件的收件人
//        message.setRecipient(Message.RecipientType.CC, new InternetAddress(MAIL_CC)); // 邮件的抄送人
//        message.setRecipient(Message.RecipientType.BCC, new InternetAddress(MAIL_BCC)); // 邮件的密送人
            message.setSubject(title);

            MimeBodyPart html = new MimeBodyPart();
            html.setContent(text, "text/html; charset=UTF-8");

            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(html);
            mm.setSubType("related");
            message.setContent(mm);
            message.saveChanges();

            Transport ts = session.getTransport();
            ts.connect(host, username, password);
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
