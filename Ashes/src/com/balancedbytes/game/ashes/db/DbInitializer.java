package com.balancedbytes.game.ashes.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.model.User;

/**
 * Create Tables in H2 database and fill with initial test data.
 */
public class DbInitializer {
	
	private static final Log LOG = LogFactory.getLog(DbInitializer.class);

	private DbManager fDbManager;
	
	protected DbInitializer(DbManager dbManager) {
		fDbManager = dbManager;
	}
	
	private int dropTable(Statement statement, String table) throws SQLException {
		LOG.trace("dropTable(" + table + ")");
		StringBuilder sql = new StringBuilder();
		sql.append("DROP TABLE IF EXISTS ").append(table).append(";");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableUsers(Statement statement) throws SQLException {
		LOG.trace("createTableUsers()");
		String sql = new StringBuilder()
			.append("CREATE TABLE users (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" user_name VARCHAR(32) NOT NULL,")
			.append(" real_name VARCHAR(80),")
			.append(" email VARCHAR(100) NOT NULL,")
			.append(" secret VARCHAR(32),")
			.append(" registered DATETIME,")
			.append(" last_processed DATETIME,")
			.append(" games_joined INTEGER NOT NULL,")
			.append(" games_finished INTEGER NOT NULL,")
			.append(" games_won INTEGER NOT NULL")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}

	private int createTableJoins(Statement statement) throws SQLException {
		LOG.trace("createTableJoins()");
		String sql = new StringBuilder()
			.append("CREATE TABLE joins (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" user_name VARCHAR(32) NOT NULL,")
			.append(" game_name VARCHAR(80),")
			.append(" home_planets VARCHAR(16),")  // comma-separated list
			.append(" joined DATETIME")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}

	private int createTableMoves(Statement statement) throws SQLException {
		LOG.trace("createTableMoves()");
		String sql = new StringBuilder()
			.append("CREATE TABLE moves (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" game_nr INTEGER NOT NULL,")
			.append(" player_nr INTEGER NOT NULL,")
			.append(" turn INTEGER NOT NULL,")
			.append(" deadline DATETIME,")
			.append(" received DATETIME,")
			.append(" user_name VARCHAR(32),")
			.append(" turn_secret VARCHAR(32),")
			.append(" command_list BLOB")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}
	
	private int createTableGames(Statement statement) throws SQLException {
		LOG.trace("createTableGames()");
		String sql = new StringBuilder()
			.append("CREATE TABLE games (")
			.append(" id IDENTITY NOT NULL PRIMARY KEY,")  // identity = auto-incrementing long integer
			.append(" game_nr INTEGER NOT NULL,")
			.append(" turn INTEGER NOT NULL,")
			.append(" last_update DATETIME,")
			.append(" player_list BLOB,")
			.append(" planet_list BLOB")
			.append(");")
			.toString();
		return statement.executeUpdate(sql);
	}

	public void init(boolean testData) throws SQLException {

		Connection connection = fDbManager.getConnection();
		Statement statement = connection.createStatement();

		dropTable(statement, "games");
		dropTable(statement, "moves");
		dropTable(statement, "joins");
		dropTable(statement, "users");
		connection.commit();

		createTableUsers(statement);
		createTableMoves(statement);
		createTableJoins(statement);
		createTableGames(statement);
		connection.commit();
		
		if (testData) {
			addTestData();
			connection.commit();
		}
		
	}
	
	private void addTestData() throws SQLException {
		LOG.trace("addTestData()");
		User userTecBeast = new User();
		userTecBeast.setUserName("TecBeast");
		userTecBeast.setRealName("Georg Seipler");
		userTecBeast.setEmail(userTecBeast.getRealName().toLowerCase().replace(' ', '@') + ".de");  // spam protection
		userTecBeast.setLastProcessed(new Date());
		userTecBeast.setRegistered(userTecBeast.getLastProcessed());
		fDbManager.getUserDataAccess().create(userTecBeast);
	}

}