package com.balancedbytes.mystuff;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionHelper {
	
	private static DataSource dataSource = null;

	public static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				if (envCtx != null) {
					dataSource = (DataSource) envCtx.lookup("jdbc/mystuff");
				}
			} catch (NamingException e) {
				throw new SQLException("Unable to find named resource", e);
			}
		}
		return (dataSource != null) ? dataSource.getConnection() : null;
	}

}