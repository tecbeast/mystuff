package com.balancedbytes.game.ashes.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.IAshesPropertyKey;

/**
 * 
 */
public class MailManagerImap implements IMailManager, IAshesPropertyKey {
	
	private static final Log LOG = LogFactory.getLog(MailManagerImap.class);
	
	private String fMailImapHost;
	private String fMailHostSmtp;
	private String fMailFrom;
	private String fMailUser;
	private String fMailPassword;

	public MailManagerImap() {
		super();
	}
	
	/**
	 * 
	 */
	public void init(Properties properties) {
		fMailImapHost = properties.getProperty(MAIL_IMAP_HOST, null);
		fMailHostSmtp = properties.getProperty(MAIL_SMTP_HOST, null);
		fMailFrom = properties.getProperty(MAIL_FROM, null);
		fMailUser = properties.getProperty(MAIL_USER, null);
		fMailPassword = properties.getProperty(MAIL_PASSWORD, null);
	}
	
	/**
	 * 
	 */
	public void processMails() {
		MailProcessor mailProcessor = new MailProcessor();
		for (Mail mail : fetchMail()) {
			mailProcessor.process(mail);
		}
	}
	
	/**
	 * 
	 */
	private List<Mail> fetchMail() {

		List<Mail> mails = new ArrayList<Mail>();
		
		Properties properties = System.getProperties();
		Session session = Session.getDefaultInstance(properties);
		try (Store store = session.getStore("imap")) {
			
			store.connect(fMailImapHost, fMailUser, fMailPassword);
			Folder inbox = store.getFolder("inbox");

			try {
				
				inbox.open(Folder.READ_WRITE);
	
				// search for all "unseen" messages
				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
				Message messages[] = inbox.search(unseenFlagTerm);
	
				if (AshesUtil.isProvided(messages)) {
					for (int i = 0; i < messages.length; i++) {
						Mail mail = new Mail();
						mail.setSubject(messages[i].getSubject());
						mail.setFrom(messages[i].getFrom()[0].toString());
						StringBuilder body = new StringBuilder();
						addBodyPart(messages[i], body);
						mail.setBody(body.toString());
						mails.add(mail);
						messages[i].setFlag(Flags.Flag.SEEN, true);
						// TODO: change to this after testing
						// messages[i].setFlag(Flags.Flag.DELETED, true);
					}
				}

			} finally {
				inbox.close(true);  // delete all mails marked for deletion
			}
			
		} catch (Exception any) {
			throw new AshesException("Error fetching mail.", any);
		}
			
		return mails;

	}
	
	/**
	 * 
	 */
	public void sendMail(Mail mail) {
		
		if (mail == null) {
			return;
		}
		
		Properties properties = System.getProperties();
		properties.setProperty(MAIL_SMTP_HOST, fMailHostSmtp);
		properties.setProperty(MAIL_USER, fMailUser);
		properties.setProperty(MAIL_PASSWORD, fMailPassword);
		Session session = Session.getDefaultInstance(properties);

		try {
		
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fMailFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
			message.setSubject(mail.getSubject());
			message.setContent(mail.getBody(), "text/plain");
			Transport.send(message);
		
			LOG.info("Mail sent to " + mail.getTo() + ": " + mail.getSubject());
			
		} catch (MessagingException me) {
			throw new AshesException("Error sending mail.", me);
		}

	}

	/**
	 * 
	 */
	private void addBodyPart(Part part, StringBuilder body) throws MessagingException, IOException {
		if (part.isMimeType("text/plain")) {
			body.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multiPart = (Multipart) part.getContent();
			int count = multiPart.getCount();
			for (int i = 0; i < count; i++) {
				addBodyPart(multiPart.getBodyPart(i), body);
			}
		} else if (part.isMimeType("message/rfc822")) {
			addBodyPart((Part) part.getContent(), body);
		} else {
			Object content = part.getContent();
			if (content instanceof String) {
				body.append((String) content);
			} else if (content instanceof InputStream) {
				try (InputStream is = (InputStream) content) {
					int c;
					while ((c = is.read()) != -1) {
						body.append(c);
					}
				}
			} else {
				body.append(content.toString());
			}
		}
	}

}