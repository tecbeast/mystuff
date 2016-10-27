package com.balancedbytes.mystuff;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RestDataAccess<T extends RestData> {

	protected T processResultSet(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		T data = null;
        while (rs.next()) {
        	data = processRow(rs);
        }
        return data;
	}

	protected void processResultSet(ResultSet rs, RestDataCollection<T> dataCollection) throws SQLException {
		if ((rs == null) || (dataCollection == null)) {
			return;
		}
        while (rs.next()) {
            dataCollection.add(processRow(rs));
        }
	}
	
	protected abstract T processRow(ResultSet rs) throws SQLException;

}
