package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.h2.tools.Server;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.IAshesPropertyKey;

public class DbManager implements IAshesPropertyKey {

	private String fDbUrl;
	private String fDbUser;
	private String fDbPassword;
	private Server fDbServer;

	public DbManager() {
		super();
	}
	
	public void init(Properties properties) throws SQLException {
		fDbUrl = properties.getProperty(DB_URL);
		fDbUser = properties.getProperty(DB_USER);
		fDbPassword = properties.getProperty(DB_PASSWORD);
		List<String> dbServerArgs = new ArrayList<String>();
		dbServerArgs.add("-ifNotExists");
		String dbServerPort = properties.getProperty(DB_SERVER_PORT);
		if (AshesUtil.provided(dbServerPort)) {
			dbServerArgs.add("-tcpPort");
			dbServerArgs.add(dbServerPort);
		}
		String dbServerDir = properties.getProperty(DB_SERVER_DIR);
		if (AshesUtil.provided(dbServerDir)) {
			dbServerArgs.add("-baseDir");
			dbServerArgs.add(dbServerDir);
		}
		try {
			Class.forName(properties.getProperty(DB_JDBC_DRIVER));
		} catch (ClassNotFoundException scnfe) {
			throw new SQLException("JDBCDriver class not found.", scnfe);
		}
		fDbServer = Server.createTcpServer(dbServerArgs.toArray(new String[dbServerArgs.size()]));
	}
	
	public void startServer() throws SQLException {
		if (fDbServer != null) {
			fDbServer.start();
		}
	}
	
	public void stopServer() {
		if (fDbServer != null) {
			fDbServer.stop();
		}
	}

	public Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(fDbUrl, fDbUser, fDbPassword);
		connection.setAutoCommit(false);
		return connection;
	}
	
	public DbInitializer getDbInitializer() {
		return new DbInitializer(this);
	}
	
	public UserDataAccess getUserDataAccess() {
		return new UserDataAccess(this);
	}
	
	public PlayerMoveDataAccess getPlayerMoveDataAccess() {
		return new PlayerMoveDataAccess(this);
	}
	
	public GameDataAccess getGameDataAccess() {
		return new GameDataAccess(this);
	}

}