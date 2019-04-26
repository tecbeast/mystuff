package com.balancedbytes.game.roborally.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoboRallyDb {

	private static final Logger LOG = LogManager.getLogger(RoboRallyDb.class);

	private static DataSource dataSource = null;

	public static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				if (envCtx != null) {
					dataSource = (DataSource) envCtx.lookup("jdbc/roborally");
				}
			} catch (NamingException e) {
				throw new SQLException("Unable to find named resource", e);
			}
		}
		return (dataSource != null) ? dataSource.getConnection() : null;
	}
	
	public static void init() throws SQLException {
		try (Connection connection = getConnection()) {
			Statement statement = connection.createStatement();
			createTableUsers(statement);
			if (countUsers(statement) == 0) {			
				LOG.info("Fill table users");
				addUser(statement, "TecBeast", "salt", "password");
			}
		}
	}
	
	private static void createTableUsers(Statement statement) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS users (");
		sql.append("  name VARCHAR(80) NOT NULL PRIMARY KEY,");
		sql.append("  salt VARCHAR(80) NOT NULL,");
		sql.append("  password VARCHAR(80) NOT NULL");
		sql.append(");");
		statement.executeUpdate(sql.toString());
	}
	
	private static int countUsers(Statement statement) throws SQLException {
		int userCount = 0;
		try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users;")) {
			while (resultSet.next()) {
				userCount = resultSet.getInt(1); 
			}
			return userCount;
		}
	}
	
	private static int addUser(Statement statement, String name, String salt, String password) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO users (name,salt,password) VALUES(");
		sql.append(quote(name)).append(",");
		sql.append(quote(salt)).append(",");
		sql.append(quote(password));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}
	
	private static String quote(String text) {
		if (text == null) {
			return "null";
		}
		StringBuilder quotedText = new StringBuilder();
		quotedText.append("'").append(text).append("'");
		return quotedText.toString();
	}

}