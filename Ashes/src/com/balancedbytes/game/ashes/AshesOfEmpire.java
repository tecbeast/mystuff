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
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class AshesOfEmpire {
	
	private static final Log LOG = LogFactory.getLog(AshesOfEmpire.class);

	private DbManager fDbManager;
	private UserCache fUserCache;

	private AshesOfEmpire() {
		fDbManager = new DbManager();
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
		LOG.info("AshesOfEmpire starting in directory " + dir.getAbsolutePath());
		Properties mailProperties = new Properties();
		try {
			try (BufferedReader in = new BufferedReader(new FileReader(new File(dir, "conf/mail.properties")))) {
				mailProperties.load(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading mail properties.", ioe);
		}
		Properties dbProperties = new Properties();
		try {
			try (BufferedReader in = new BufferedReader(new FileReader(new File(dir, "conf/db.properties")))) {
				dbProperties.load(in);
			}
		} catch (IOException ioe) {
			throw new AshesException("Error reading db properties.", ioe);
		}
		dbProperties.setProperty(DbManager.DB_SERVER_DIR, new File(dir, "db").getAbsolutePath());
		try {
			fDbManager.init(dbProperties);
			fDbManager.startServer();
		} catch (SQLException sqle) {
			throw new AshesException("Error starting db server.", sqle);
		}
		fUserCache.init(fDbManager.getUserDataAccess());
	}
	
	public DbManager getDbManager() {
		return fDbManager;
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
	
	public User getUser(String id) {
		return fUserCache.get(id);
	}
	
	public static void main(String[] args) throws Exception {
		AshesOfEmpire ashes = new AshesOfEmpire();
		String dirname = System.getProperties().getProperty("ashes.dir", null);
		File dir = (dirname != null) ? new File(dirname) : new File(AshesOfEmpire.class.getResource("/").toURI());
		ashes.init(dir);
		if (AshesUtil.isProvided(args) && "initdb".equalsIgnoreCase(args[0])) {
			ashes.getDbManager().getDbInitializer().init(true);
			ashes.getDbManager().stopServer();
		}
	}

}
