package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Create Tables in HSQLDB and fill with initial test data.
 */
public class DbInitializer {
	
	private static final Log LOG = LogFactory.getLog(DbInitializer.class);

	private DbManager fDbManager;
	
	protected DbInitializer(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	private int dropTable(Statement statement, String table) throws SQLException {
		LOG.info("drop table " + table);
		StringBuilder sql = new StringBuilder();
		sql.append("DROP TABLE IF EXISTS ").append(table).append(";");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableUsers(Statement statement) throws SQLException {
		LOG.info("create table users");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE users (");
		sql.append("  id VARCHAR(20) NOT NULL PRIMARY KEY,");
		sql.append("  name VARCHAR(80) NOT NULL,");
		sql.append("  email VARCHAR(100) NOT NULL,");
		sql.append("  registered TIMESTAMP NOT NULL,");
		sql.append("  last_processed TIMESTAMP NOT NULL,");
		sql.append("  games_joined TIMESTAMP NOT NULL,");
		sql.append("  games_finished TIMESTAMP NOT NULL,");
		sql.append("  games_won TIMESTAMP NOT NULL");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	public void init(boolean testData) throws SQLException {

		Connection connection = fDbManager.getConnection();
		Statement statement = connection.createStatement();

		dropTable(statement, "users");
		connection.commit();

		createTableUsers(statement);
		connection.commit();
		
	}

}