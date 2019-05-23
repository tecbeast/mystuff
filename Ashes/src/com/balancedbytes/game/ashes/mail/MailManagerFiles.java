package com.balancedbytes.game.ashes.mail;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesException;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.IAshesPropertyKey;

public class MailManagerFiles implements IMailManager, IAshesPropertyKey {
	
	private static final Log LOG = LogFactory.getLog(MailManagerFiles.class);

	private static final String MAIL_FILE_PREFIX = "mail";
	private static final String MAIL_FILE_SUFFIX = ".txt";
	
	private File fMailDirIn;
	private File fMailDirOut;
	private int fLastMailNr;
	private String fMailFrom;
	
	public MailManagerFiles() {
		super();
	}
	
	@Override
	public void init(Properties properties) {
		File baseDir = new File(properties.getProperty(ASHES_DIR));
		fMailDirIn = new File(baseDir, properties.getProperty(MAIL_DIR_IN));
		if (!fMailDirIn.exists() || !fMailDirIn.isDirectory()) {
			throw new AshesException("Invalid incoming mail directory \"" + fMailDirIn.getAbsolutePath() + "\"");
		}
		fMailDirOut = new File(baseDir, properties.getProperty(MAIL_DIR_OUT));
		if (!fMailDirOut.exists() || !fMailDirOut.isDirectory()) {
			throw new AshesException("Invalid outgoing mail directory \"" + fMailDirOut.getAbsolutePath() + "\"");
		}
		fLastMailNr = findLastMailNumber();
		fMailFrom = properties.getProperty(MAIL_FROM, null);
	}
	
	@Override
	public String getEmailAddress() {
		return fMailFrom;
	}
	
	@Override
	public void processMails() {
		MailProcessor mailProcessor = new MailProcessor();
		for (Mail mail : fetchMail()) {
			mailProcessor.process(mail);
		}
	}
	
	private List<Mail> fetchMail() {
		List<Mail> mails = new ArrayList<Mail>();
		String[] filenames = fMailDirIn.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(MAIL_FILE_PREFIX) && name.endsWith(MAIL_FILE_SUFFIX);
			}
		});
		for (String filename : filenames) {
			File inFile = new File(fMailDirIn, filename);
			try {
				mails.add(new Mail().readFrom(new FileReader(inFile)));
			} catch (IOException ioe) {
				// do nothing
			}
		}
		return mails;
	}
	
	@Override
	public void sendMail(Mail mail) {
		if (mail == null) {
			return;
		}
		fLastMailNr++;
		String fileName = new StringBuilder()
			.append(MAIL_FILE_PREFIX)
			.append(AshesUtil.toStringWithLeadingZeroes(fLastMailNr, 5))
			.append(MAIL_FILE_SUFFIX)
			.toString();
		File outFile = new File(fMailDirOut, fileName);
		try {
			mail.writeTo(new FileWriter(outFile));
			LOG.info("Mail sent to " + mail.getTo() + ": " + mail.getSubject());
		} catch (IOException ioe) {
			throw new AshesException("Error writing mail.", ioe);
		}
	}

	private int findLastMailNumber() {
		int highestMailNumber = 0;
		int minLength = MAIL_FILE_PREFIX.length() + MAIL_FILE_SUFFIX.length();
		String[] filenames = fMailDirOut.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(MAIL_FILE_PREFIX) && name.endsWith(MAIL_FILE_SUFFIX) && (name.length() > minLength);
			}
		});
		for (String filename : filenames) {
			String numberString = AshesUtil.stripLeadingZeroes(
				filename.substring(MAIL_FILE_PREFIX.length(), filename.length() - MAIL_FILE_SUFFIX.length())
			);
			try {
				int number = Integer.parseInt(numberString);
				highestMailNumber = Math.max(highestMailNumber, number);
			} catch (NumberFormatException nfe) {
				// do nothing
			}
		}
		return highestMailNumber;
	}

}
