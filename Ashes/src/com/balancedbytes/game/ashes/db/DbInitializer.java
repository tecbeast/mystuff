package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.model.User;

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
		String sql = new StringBuilder()
			.append("CREATE TABLE users (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" name VARCHAR(20) NOT NULL,")
			.append(" real_name VARCHAR(100),")
			.append(" email VARCHAR(100) NOT NULL,")
			.append(" registered TIMESTAMP NOT NULL,")
			.append(" last_processed TIMESTAMP NOT NULL,")
			.append(" games_joined INTEGER NOT NULL,")
			.append(" games_finished INTEGER NOT NULL,")
			.append(" games_won INTEGER NOT NULL")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}
	
	private int createTablePlayerMoves(Statement statement) throws SQLException {
		LOG.info("create table player_moves");
		String sql = new StringBuilder()
			.append("CREATE TABLE player_moves (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" game_number INTEGER NOT NULL,")
			.append(" player_number INTEGER NOT NULL,")
			.append(" turn INTEGER NOT NULL,")
			.append(" deadline TIMESTAMP,")
			.append(" received TIMESTAMP,")
			.append(" turn_secret VARCHAR(40),")
			.append(" commands BLOB")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}

	public void init(boolean testData) throws SQLException {

		Connection connection = fDbManager.getConnection();
		Statement statement = connection.createStatement();

		dropTable(statement, "users");
		dropTable(statement, "player_moves");
		connection.commit();

		createTableUsers(statement);
		createTablePlayerMoves(statement);
		connection.commit();
		
		if (testData) {
			addTestData();
		}
		
	}
	
	private void addTestData() throws SQLException {
		User userTecBeast = new User();
		userTecBeast.setName("TecBeast");
		userTecBeast.setEmail(userTecBeast.getName().toLowerCase().replace(' ', '@') + ".de");
		fDbManager.getUserDataAccess().create(userTecBeast);
	}

}