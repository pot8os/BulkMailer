package net.pot8os.bulkmailer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author pot8os
 */
public class MailSender {

    private final String user, password, from, mailText;
    private String title = "メールアドレス変更のお知らせ";
    private String smtpHost = "smtp.spmode.ne.jp";
    private String smtpPort = "465";
    private MimeMessage msg;
    private final Queue<String> addressList = new ArrayBlockingQueue<>(10000);

    public MailSender(String addressFile, String user, String password, String from, String messageFile) throws FileNotFoundException, IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(addressFile)));
        final Pattern p = Pattern.compile("(.+@.+\\..+)");
        String line;
        while ((line = br.readLine()) != null) {
            final Matcher m = p.matcher(line);
            if (m.find()) {
                addressList.offer(m.group(1));
            }
        }
        final StringBuilder sb = new StringBuilder();
        final BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(messageFile), "utf-8"));
        String line2;
        while ((line2 = br2.readLine()) != null) {
            sb.append(line2).append("\n");
        }
        this.mailText = sb.toString();
        this.user = user;
        this.password = password;
        this.from = from;
    }

    public void setSmtpHost(String smtpHost) {
        final String[] hostAndPort = smtpPort.split(":");
        if (hostAndPort.length == 2) {
            this.smtpHost = hostAndPort[0];
            this.smtpPort = hostAndPort[1];
        } else {
            this.smtpHost = smtpHost;
        }
    }

    public boolean prepare() throws MessagingException, IOException {
        if (addressList.isEmpty()) {
            return false;
        }
        final Properties prop = new Properties();
        prop.put("mail.host", smtpHost);
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.connectiontimeout", "5000");
        prop.put("mail.smtp.timeout", "5000");
        prop.put("mail.user", "BulkMailer");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.socketFactory.port", smtpPort);
        final Session session = Session.getDefaultInstance(prop, new PasswordAuthenticator(user, password));
        session.setDebug(true);
        msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setSender(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(from));
        final List<InternetAddress> nextAddressList = new ArrayList<>();
        final int count = Math.min(100, this.addressList.size());
        for (int i = 0; i < count; i++) {
            nextAddressList.add(new InternetAddress(this.addressList.poll()));
        }
        msg.setRecipients(Message.RecipientType.BCC, nextAddressList.toArray(new InternetAddress[nextAddressList.size()]));
        msg.setSubject(title, "utf-8");
        msg.setText(mailText);
        msg.writeTo(System.out);
        return true;
    }

    public void send() throws MessagingException {
        Transport.send(msg);
        System.out.println("\n !!! send !!! \n");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    class PasswordAuthenticator extends Authenticator {

        private final String username;
        private final String password;

        PasswordAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
