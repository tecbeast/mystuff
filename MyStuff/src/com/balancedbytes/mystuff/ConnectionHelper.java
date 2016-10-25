package com.balancedbytes.mystuff;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionHelper {
	
	private static final ConnectionHelper _INSTANCE = new ConnectionHelper();
	
	private DataSource dataSource = null;

	private ConnectionHelper() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			dataSource = (DataSource) envCtx.lookup("jdbc/mystuff");
		} catch (NamingException e) {
			throw new MyStuffException(e);
		}
	}
	
	private Connection getConnectionInternal() throws SQLException {
		if (dataSource == null) {
			return null;
		}
		return dataSource.getConnection();
	}

	public static Connection getConnection() throws SQLException {
		return _INSTANCE.getConnectionInternal();
	}

}