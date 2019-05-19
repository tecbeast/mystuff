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
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE users (");
		sql.append("  id VARCHAR(20) NOT NULL PRIMARY KEY,");
		sql.append("  name VARCHAR(80) NOT NULL,");
		sql.append("  email VARCHAR(100) NOT NULL,");
		sql.append("  registered TIMESTAMP NOT NULL,");
		sql.append("  last_processed TIMESTAMP NOT NULL,");
		sql.append("  games_joined INTEGER NOT NULL,");
		sql.append("  games_finished INTEGER NOT NULL,");
		sql.append("  games_won INTEGER NOT NULL");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}
	
	private int createTablePlayerTurns(Statement statement) throws SQLException {
		LOG.info("create table player_turns");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE player_turns (");
		sql.append("  game_number INTEGER NOT NULL,");
		sql.append("  turn INTEGER NOT NULL,");
		sql.append("  player_number INTEGER NOT NULL,");
		sql.append("  deadline TIMESTAMP,");
		sql.append("  turn_secret VARCHAR(40),");
		sql.append("  turn_commands CLOB,");
		sql.append("  PRIMARY KEY(game_number, turn, player_number)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	public void init(boolean testData) throws SQLException {

		Connection connection = fDbManager.getConnection();
		Statement statement = connection.createStatement();

		dropTable(statement, "users");
		dropTable(statement, "player_turns");
		connection.commit();

		createTableUsers(statement);
		createTablePlayerTurns(statement);
		connection.commit();
		
		if (testData) {
			addTestData();
		}
		
	}
	
	private void addTestData() throws SQLException {

		User userTecBeast = new User();
		userTecBeast.setId("TecBeast");
		userTecBeast.setName("Georg Seipler");
		userTecBeast.setEmail(userTecBeast.getName().toLowerCase().replace(' ', '@') + ".de");
		fDbManager.getUserDataAccess().createUser(userTecBeast);
		
	}

}