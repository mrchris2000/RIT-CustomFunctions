package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

//import java.io.FileWriter;
//import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart; 
//import java.net.InetAddress;

public class SendEmail extends Function
{
   private Function m_Recipients = null;
   private Function m_Subject = null;
   private Function m_Message = null;

   public SendEmail() {
   }
   
   protected SendEmail(Function f1, Function f2, Function f3)
   {
	   m_Recipients = f1;
	   m_Subject = f2;
	   m_Message = f3;
   }

   public Object evaluate(Object data)
   { 
       String sRecipients = m_Recipients.evaluateAsString(data);
       String sSubject = m_Subject.evaluateAsString(data);
       String sMessage = m_Message.evaluateAsString(data);
	   
	   /**
	   * Sends an email with HTML content
	   *
	   * @param sRecipients String - List of recipients using internet addresses delimited with commas
	   * @param sSubject    String - subject text
	   * @param sMessage    String - Body of the email in HTML content
	   */
//	   public static void emailHTMLSend(String sRecipients, String sSubject, String sMessage) {

		emailSend(sRecipients, sSubject, sMessage, "text/html; charset=utf-8", new ArrayList<String>());
		return 0;
   }

	   /**
	   * Sends an email with text content and attachments
	   *
	   * @param sRecipients String - List of recipients using internet addresses delimited with commas
	   * @param sSubject    String - subject text
	   * @param sMessage    String - message text
	   * @param attachments List - List of file paths to attach to email
	   */
	   public static void emailTextAttachmentsSend(String sRecipients, String sSubject, String sMessage, ArrayList<String> attachments) {
	       emailSend(sRecipients, sSubject, sMessage, "text/plain; charset=utf-8", attachments);
	   }
	   
	   /**
	   * This sends an email notification
	   *
	   * @param sRecipients  String - List of recipients using internet addresses delimited with commas
	   * @param sSubject     String - subject text
	   * @param sMessage     String - message text
	   * @param sContentType String - Content type for the email body
	   * @param attachments  List - List of file paths to attach to email
	   */
	   public static void emailSend(String sRecipients, String sSubject, String sMessage, String sContentType, ArrayList<String> attachments) {
	       Properties properties = System.getProperties();
	       properties.setProperty("mail.smtp.host", "smtp.us.bank-dns.com");
	       Session session = Session.getDefaultInstance(properties);
	       MimeMessage oMessage = new MimeMessage(session);
	       try {
	           oMessage.setFrom(new InternetAddress(getUserID() + "@usbank.com"));
	           String[] aRecipients = sRecipients.split(",");
	           for (String recipient : aRecipients) {
	               oMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
	           }
	           oMessage.setSubject(sSubject);
	           if (attachments.size() > 0) {
	               BodyPart messageBodyPart = new MimeBodyPart();
	               messageBodyPart.setText(sMessage);
	               Multipart multipart = new MimeMultipart();
	               multipart.addBodyPart(messageBodyPart);
	               for (String attachment : attachments) {
	                   messageBodyPart = new MimeBodyPart();
	                   DataSource source = new FileDataSource(attachment);
	                   messageBodyPart.setDataHandler(new DataHandler(source));
	                   messageBodyPart.setFileName(attachment.substring(attachment.lastIndexOf("/")));
	                   multipart.addBodyPart(messageBodyPart);
	               }
	               oMessage.setContent(multipart, sContentType);
	           } else {
	               oMessage.setContent(sMessage, sContentType);
	           }
	           Transport.send(oMessage);
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	   }


	   /**
	   * Returns the logged in user
	   *
	   * @return String - User name
	   */
	   public static String getUserID() {
	       return (System.getenv("BUILD_USER_ID") == null) ? System.getProperty("user.name") : System.getenv("BUILD_USER_ID");
	   }	   

   @SuppressWarnings("rawtypes")
   public Function create(int size, Vector params) 
   {
	   return new SendEmail((Function)params.get(0),
	   							(Function)params.get(1),
	   							(Function)params.get(2));
   }
}
