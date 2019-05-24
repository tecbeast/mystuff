package com.balancedbytes.game.ashes.model;

import java.util.Date;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.ValidationResult;
import com.balancedbytes.game.ashes.db.ICsvExportable;
import com.balancedbytes.game.ashes.db.IDataObject;

public class PlayerMove implements IDataObject, ICsvExportable {

	public static final String CSV_HEADER = "GameNr;Turn;PlayerNr;Deadline;Received;UserName;TurnSecret;NrOfCommands";

	private long fId;
	private boolean fModified;
	
	private int fGameNr;
	private int fTurn;
	private int fPlayerNr;
	private Date fDeadline;
	private Date fReceived;
	private String fUserName;
	private String fTurnSecret;
	private CommandList fCommands;
	
	private transient User fUser;
	private transient ValidationResult fValidationResult;
	
	public PlayerMove() {
		super();
	}
	
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
	
	public int getGameNr() {
		return fGameNr;
	}
	
	public void setGameNr(int gameNr) {
		fGameNr = gameNr;
	}
	
	public int getTurn() {
		return fTurn;
	}
	
	public void setTurn(int turn) {
		fTurn = turn;
	}
	
	public int getPlayerNr() {
		return fPlayerNr;
	}
	
	public void setPlayerNr(int playerNr) {
		fPlayerNr = playerNr;
	}
	
	public Date getDeadline() {
		return fDeadline;
	}
	
	public void setDeadline(Date deadline) {
		fDeadline = deadline;
	}
	
	public Date getReceived() {
		return fReceived;
	}
	
	public void setReceived(Date received) {
		fReceived = received;
	}
	
	public String getUserName() {
		return fUserName;
	}
	
	public void setUserName(String userName) {
		fUserName = userName;
	}
	
	public String getTurnSecret() {
		return fTurnSecret;
	}
	
	public void setTurnSecret(String turnSecret) {
		fTurnSecret = turnSecret;
	}
	
	public CommandList getCommands() {
		return fCommands;
	}
	
	public void setCommands(CommandList commands) {
		fCommands = commands;
	}
	
	public User getUser() {
		return fUser;
	}
	
	public void setUser(User user) {
		fUser = user;
	}
	
	public ValidationResult getValidationResult() {
		return fValidationResult;
	}
	
	public void setValidationResult(ValidationResult validationResult) {
		fValidationResult = validationResult;
	}
	
	public String toCsv() {
		StringBuilder result = new StringBuilder();
		result.append(fGameNr);
		result.append(CSV_SEPARATOR);
		result.append(fTurn);
		result.append(CSV_SEPARATOR);
		result.append(fPlayerNr);
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.asTimestamp(fDeadline));
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.asTimestamp(fReceived));
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.print(fUserName));
		result.append(CSV_SEPARATOR);
		result.append(AshesUtil.print(fTurnSecret));
		result.append(CSV_SEPARATOR);
		int commandSize = (fCommands != null) ? fCommands.size() : 0;
		result.append(commandSize);
		return result.toString();
	}
		
}
