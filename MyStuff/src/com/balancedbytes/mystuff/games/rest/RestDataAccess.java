package com.balancedbytes.mystuff.games.rest;

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

	protected void processResultSet(ResultSet rs, RestDataCollection<T> dataCollection, RestDataPaging paging) throws SQLException {
		if ((rs == null) || (dataCollection == null)) {
			return;
		}
        while (rs.next()) {
        	T data = processRow(rs, paging);
        	if (data != null) {
        		dataCollection.addElement(data);
        	}
        }
	}

	protected T processRow(ResultSet rs, RestDataPaging paging) throws SQLException {
		if (paging == null) {
			return processRow(rs);
		}
		paging.incCount();
		if (paging.isOnPage()) {
			return processRow(rs);
		}
		return null;
	}
	
	protected abstract T processRow(ResultSet rs) throws SQLException;

}
