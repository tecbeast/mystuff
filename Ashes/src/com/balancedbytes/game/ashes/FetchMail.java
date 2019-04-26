package com.balancedbytes.game.ashes;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import javax.mail.search.FlagTerm;

public class FetchMail {

	public static void main(String args[]) throws Exception {

		// mail server info
		String host = "pop.ionos.de";
		String user = "ashes@balancedbytes.com";
		String password = "AoE_2019";

		// connect to my pop3 inbox in read-only mode
		Properties properties = System.getProperties();
		Session session = Session.getDefaultInstance(properties);
		Store store = session.getStore("pop3");
		store.connect(host, user, password);
		Folder inbox = store.getFolder("inbox");
		inbox.open(Folder.READ_ONLY);

		// search for all "unseen" messages
		Flags seen = new Flags(Flags.Flag.SEEN);
		FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		Message messages[] = inbox.search(unseenFlagTerm);

		if (messages.length == 0) {
			System.out.println("No messages found.");
		}

		for (int i = 0; i < messages.length; i++) {
			System.out.println("Message " + (i + 1));
			System.out.println("From : " + messages[i].getFrom()[0]);
			System.out.println("Subject : " + messages[i].getSubject());
			System.out.println("Sent Date : " + messages[i].getSentDate());
			dumpPart(messages[i]);
			System.out.println();
		}

		inbox.close(true);
		store.close();

	}

	private static void dumpPart(Part p) throws Exception {
		String ct = p.getContentType();
		try {
			System.out.println("CONTENT-TYPE: " + (new ContentType(ct)).toString());
		} catch (ParseException pex) {
			System.out.println("BAD CONTENT-TYPE: " + ct);
		}
		String filename = p.getFileName();
		if (filename != null)
			System.out.println("FILENAME: " + filename);

		/*
		 * Using isMimeType to determine the content type avoids fetching the actual
		 * content data until we need it.
		 */
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		} else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				dumpPart(mp.getBodyPart(i));
			}
		} else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			dumpPart((Part) p.getContent());
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				try (InputStream is = (InputStream) o) {
					int c;
					while ((c = is.read()) != -1) {
						System.out.write(c);
					}
				}
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}
	}

}
