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
import javax.mail.search.FlagTerm;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;

public class MailManager {
	
	private static final String MAIL_HOST_IMAP = "mail.host.imap";
	private static final String MAIL_HOST_SMTP = "mail.host.smtp";
	private static final String MAIL_USER = "mail.user";
	private static final String MAIL_PASSWORD = "mail.password";

	private String fMailHostImap;
	private String fMailHostSmtp;
	private String fMailUser;
	private String fMailPassword;

	public MailManager() {
		super();
	}
	
	public void init(Properties properties) {
		fMailHostImap = properties.getProperty(MAIL_HOST_IMAP, null);
		fMailHostSmtp = properties.getProperty(MAIL_HOST_SMTP, null);
		fMailUser = properties.getProperty(MAIL_USER, null);
		fMailPassword = properties.getProperty(MAIL_PASSWORD, null);
	}
	
	public List<Mail> fetchMail() {

		List<Mail> mails = new ArrayList<Mail>();
		
		Store store = null;
		Folder inbox = null;
		
		try {
		
			// connect to my IMAP inbox in read-only mode
			Properties properties = System.getProperties();
			Session session = Session.getDefaultInstance(properties);
			store = session.getStore("imap");
			store.connect(fMailHostImap, fMailUser, fMailPassword);
			inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
	
			// search for all "unseen" messages
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message messages[] = inbox.search(unseenFlagTerm);
	
			if (AshesUtil.isProvided(messages)) {
				for (int i = 0; i < messages.length; i++) {
					Mail mail = new Mail();
					mail.setSubject(messages[i].getSubject());
					mail.setFrom(messages[i].getFrom()[0]);
					StringBuilder body = new StringBuilder();
					addBodyPart(messages[i], body);
					mail.setBody(body.toString());
				}
			}
			
			// TODO: delete processed mails

			inbox.close(true);
			store.close();
			
		} catch (Exception any) {
			throw new AshesException("Error fetching mail.", any);
		}
		
		return mails;

	}

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