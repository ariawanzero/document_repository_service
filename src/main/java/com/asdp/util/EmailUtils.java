package com.asdp.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtils {
	public static void sendEmail(String emailReceiver, String bodyMessage, String subject) throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("septa.rf2018@gmail.com", "SEPTA1509");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("septa.rf2018@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceiver));
		msg.setSubject(subject);
		msg.setContent(bodyMessage, "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(bodyMessage, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		/*MimeBodyPart attachPart = new MimeBodyPart();
		   attachPart.attachFile("/var/tmp/image19.png");
		   multipart.addBodyPart(attachPart);*/
		msg.setContent(multipart);
		Transport.send(msg);  
	}
}
