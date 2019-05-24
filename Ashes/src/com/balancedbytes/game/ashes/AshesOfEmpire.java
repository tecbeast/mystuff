package com.balancedbytes.game.ashes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.db.DbManager;
import com.balancedbytes.game.ashes.mail.IMailManager;
import com.balancedbytes.game.ashes.mail.MailManagerFiles;
import com.balancedbytes.game.ashes.mail.MailManagerImap;
import com.balancedbytes.game.ashes.model.GameCache;
import com.balancedbytes.game.ashes.model.PlayerMoveCache;
import com.balancedbytes.game.ashes.model.UserCache;

public class AshesOfEmpire implements IAshesPropertyKey {
	
	private static final Log LOG = LogFactory.getLog(AshesOfEmpire.class);
	private static final AshesOfEmpire INSTANCE = new AshesOfEmpire();
	
	private DbManager fDbManager;
	private IMailManager fMailManager;
	private UserCache fUserCache;
	private PlayerMoveCache fMoveCache;
	private GameCache fGameCache;

	private AshesOfEmpire() {
		fDbManager = new DbManager();
		fUserCache = new UserCache();
		fMoveCache = new PlayerMoveCache();
		fGameCache = new GameCache();
		fMailManager = new MailManagerImap();
	}
	
	public static AshesOfEmpire getInstance() {
		return INSTANCE;
	}
	
	private void init(File dir) {
		try {
			try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(dir, "conf/log.properties")))) {
				LogManager.getLogManager().readConfiguration(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading log configuration.", ioe);
		}
		LOG.info("AshesOfEmpire starting in directory " + dir.getAbsolutePath());
		Properties mailProperties = new Properties();
		try {
			try (BufferedReader in = new BufferedReader(new FileReader(new File(dir, "conf/mail.properties")))) {
				mailProperties.load(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading mail properties.", ioe);
		}
		if (IMailManager.MAIL_MODE_FILES.equals(mailProperties.getProperty(MAIL_MODE, ""))) {
			fMailManager = new MailManagerFiles();
			mailProperties.setProperty(ASHES_DIR, dir.getAbsolutePath());
		}
		fMailManager.init(mailProperties);
		Properties dbProperties = new Properties();
		try {
			try (BufferedReader in = new BufferedReader(new FileReader(new File(dir, "conf/db.properties")))) {
				dbProperties.load(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading db properties.", ioe);
		}
		dbProperties.setProperty(DB_SERVER_DIR, new File(dir, "db").getAbsolutePath());
		try {
			fDbManager.init(dbProperties);
			fDbManager.startServer();
		} catch (SQLException sqle) {
			throw new AshesException("Error starting db server.", sqle);
		}
		fUserCache.init(fDbManager);
		fMoveCache.init(fDbManager);
		fGameCache.init(fDbManager);
	}
	
	public DbManager getDbManager() {
		return fDbManager;
	}
	
	public IMailManager getMailManager() {
		return fMailManager;
	}
	
	public UserCache getUserCache() {
		return fUserCache;
	}
	
	public GameCache getGameCache() {
		return fGameCache;
	}
	
	public PlayerMoveCache getMoveCache() {
		return fMoveCache;
	}
	
	public void run() {
		getMailManager().processMails();
		getUserCache().save();
		getMoveCache().save();
		getGameCache().save();
	}
	
	public static void main(String[] args) {
		try {
			String dirname = System.getProperties().getProperty(ASHES_DIR, null);
			File dir = (dirname != null) ? new File(dirname) : new File(AshesOfEmpire.class.getResource("/").toURI());
			AshesOfEmpire ashes = getInstance();
			ashes.init(dir);
			if (AshesUtil.isProvided(args) && "initdb".equalsIgnoreCase(args[0])) {
				ashes.getDbManager().getDbInitializer().init(false);
			} else {
				ashes.run();
			}
		} catch (Exception any) {
			LOG.error("", any);
		} finally {
			getInstance().getDbManager().stopServer();
		}
	}

}
