package com.balancedbytes.game.ashes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class AshesOfEmpire {
	
	private static final Log LOG = LogFactory.getLog(AshesOfEmpire.class);

	public static final String MAIL_HOST_IMAP = "mail.host.imap";
	public static final String MAIL_HOST_SMTP = "mail.host.smtp";
	public static final String MAIL_USER = "mail.user";
	public static final String MAIL_PASSWORD = "mail.password";

	private Properties fProperties;
	private UserCache fUserCache;

	public AshesOfEmpire() {
		fProperties = new Properties();
		fUserCache = new UserCache();
	}
	
	public void init(File dir) {
		try {
			try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(dir, "conf/log.properties")))) {
				LogManager.getLogManager().readConfiguration(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading log configuration.", ioe);
		}
		try {
			try (BufferedReader in = new BufferedReader(new FileReader(new File(dir, "conf/ashes.properties")))) {
				fProperties.load(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading ashes properties.", ioe);
		}
		boolean userCompression = Boolean.parseBoolean(fProperties.getProperty("user.compression", "false"));
		fUserCache.init(new File(dir, "user"), userCompression);
	}
	
	public boolean save() {
		try {
			fUserCache.save();
			return true;
		} catch (Exception e) {
			LOG.error("Error while saving Ashes.", e);
			return false;
		}
	}
	
	public String getProperty(String key) {
		return fProperties.getProperty(key);
	}
	
	public User getUser(String id) {
		return fUserCache.get(id);
	}
	
	public static void main(String[] args) throws Exception {
		AshesOfEmpire ashes = new AshesOfEmpire();
		File dir = null;
		if ((args != null) && (args.length > 0)) {
			dir = new File(args[0]);
		} else {
			dir = new File(AshesOfEmpire.class.getResource("/").toURI());
		}
		LOG.info("AshesOfEmpire starting in directory " + dir.getAbsolutePath());
		ashes.init(dir);
		User user = ashes.getUser("kalimar");
		user.setGamesJoined(user.getGamesJoined() + 1);
		ashes.save();
	}

}
