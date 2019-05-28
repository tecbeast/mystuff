package com.balancedbytes.game.ashes.model;

import java.util.Date;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.db.ICsvExportable;
import com.balancedbytes.game.ashes.db.IDataObject;

public class Join implements IDataObject, ICsvExportable {
	
	public static final String CSV_HEADER = "UserName;GameName;HomePlanets;Joined";

	private long fId;
	private boolean fModified;
	
	private String fUserName;
	private String fGameName;
	private List<Integer> fHomePlanets;
	private Date fJoined;
	
	@Override
	public long getId() {
		return fId;
	}
	
	public void setId(long id) {
		fId = id;
	}
	
	@Override
	public boolean isModified() {
		return fModified;
	}
	
	public void setModified(boolean modified) {
		fModified = modified;
	}
	
	public String getUserName() {
		return fUserName;
	}
	
	public void setUserName(String userName) {
		fUserName = userName;
	}
	
	public String getGameName() {
		return fGameName;
	}
	
	public void setGameName(String gameName) {
		fGameName = gameName;
	}
	
	public List<Integer> getHomePlanets() {
		return fHomePlanets;
	}
	
	public void setHomePlanets(List<Integer> homePlanets) {
		fHomePlanets = homePlanets;
	}
	
	public Date getJoined() {
		return fJoined;
	}
	
	public void setJoined(Date joined) {
		fJoined = joined;
	}
	
	public String toCsv() {
		StringBuilder result = new StringBuilder();
		result.append(fUserName);
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.print(fGameName));
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.join(fHomePlanets, ","));
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.asTimestamp(fJoined));
		return result.toString();
	}
	
}
