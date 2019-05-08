package com.balancedbytes.game.ashes.model;

import java.util.List;

import com.balancedbytes.game.ashes.command.Command;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.ICommandFilter;
import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class Player implements IJsonSerializable {
	
	private static final String USER = "user";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private static final String HOME_PLANET_NR = "homePlanetNr";
	private static final String POLITICAL_POINTS = "politicalPoints";
	private static final String FIGHTER_MORALE = "fighterMorale";
	private static final String TRANSPORTER_MORALE = "transporterMorale";
	private static final String POLITICAL_TERMS = "politicalTerms";

	private String fUser;
	private String fName;
	private int fNumber;
	private int fHomePlanetNr;
	private int fPoliticalPoints;
	private int fFighterMorale;      // percent (min = 50%, max = 150%)
	private int fTransporterMorale;  // percent (min = 50%, max = 150%) 
	private PoliticalTerm[] fPoliticalTerms;
	private Reporting fReporting;

	/**
	 *
	 */
	public Player(String user, int number) {

		fNumber = number;

		setUser(user);
		setHomePlanetNr(fNumber);
		setName("player" + fNumber);
		
		setPoliticalPoints(0);
		setFighterMorale(100);
		setTransporterMorale(100);
		fReporting = new Reporting();
		
		// you start at WAR with neutral,
		// at PEACE with yourself
		// and at NEUTRAL with anyone else
		fPoliticalTerms = new PoliticalTerm[9];
		for (int i = 0; i < fPoliticalTerms.length; i++) {
			if (i == 0) {
				setPoliticalTerm(i, PoliticalTerm.WAR);
			} else if (i == fNumber) {
				setPoliticalTerm(i, PoliticalTerm.PEACE);
			} else {
				setPoliticalTerm(i, PoliticalTerm.NEUTRAL);
			}
		}
		
	}  
  
	public String getUser() {
		return fUser;
	}
	
	public void setUser(String user) {
		fUser = user;
	}
	
	public int getHomePlanetNr() {
		return fHomePlanetNr;
	}
		
	public void setHomePlanetNr(int planetNr) {
		if ((planetNr >= 1) && (planetNr <= 40)) {
			fHomePlanetNr = planetNr;
		}
	}

	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}

	public int getNumber() {
		return fNumber;
	}
	
	public int getPoliticalPoints() {
		return fPoliticalPoints;
	}
	
	public void setPoliticalPoints(int politicalPoints) {
		fPoliticalPoints = politicalPoints;
	}
	
	public int getFighterMorale() {
		return fFighterMorale;
	}

	public void setFighterMorale(int percent) {
		if (percent < 50) {
			fFighterMorale = 50;
		} else if (percent > 150) {
			fFighterMorale = 150;
		} else {
			fFighterMorale = percent;
		}
	}

	public int getTransporterMorale() {
		return fTransporterMorale;
	}
	
	public void setTransporterMorale(int percent) {
		if (percent < 50) {
			fTransporterMorale = 50;
		} else if (percent > 150) {
			fTransporterMorale = 150;
		} else {
			fTransporterMorale = percent;
		}
	}
	
	public PoliticalTerm getPoliticalTerm(int otherPlayer) {
		if ((otherPlayer < 0) || (otherPlayer > fPoliticalTerms.length)) {
			return null;
		}
		return fPoliticalTerms[otherPlayer];
	}
	
	public void setPoliticalTerm(int otherPlayer, PoliticalTerm politicalTerm) {
		if ((otherPlayer >= 0) && (otherPlayer < fPoliticalTerms.length)) {
			fPoliticalTerms[otherPlayer] = politicalTerm;
		}
	}
	
	public Reporting getReporting() {
		return fReporting;
	}
	
	/**
	 *  Play a full turn for this player with the given commands.
	 */
	public void turn(Game game, CommandList cmdList) {
		
		for (Command cmd : getPlayerCommands(cmdList)) {
			switch (cmd.getType()) {
				case DECLARE:
					break;
				case HOMEPLANET:
					break;
				case PLANETNAME:
					break;
				case PLAYERNAME:
					break;
				default:
					break;
			}
		}
			  
	/*
	StringBuffer buffer = new StringBuffer();
  	Iterator<Command> iterator = null;
  
  	if (phaseNr > 0) {
  
  	} else {
  
  	  while (Command cmd : cmdList.getCommands()) {
  			if (cmd.getPlayerNr() == fNr) {
  			  switch (cmd.getToken()) {
  					case DECLARE:
  			 			fPt[cmd.getDestination()] = cmd.getType();
  				  	break;
  					case PLAYERNAME:
  				 		fName = cmd.getText();
  					  break;
					default:
						break;
  				}
  			}
  	  }
  
  	  if (fFm < 0.96f) { fFm += 0.05; }
  	  if (fTm < 0.96f) { fTm += 0.05; }
  
  	  buffer.append("\nplayer1  player2  player3  player4  player5  player6  player7  player8\n");
  	  for (int i = 1; i < fPt.length; i++) {
  			switch (fPt[i]) {
  			  case     WAR: buffer.append("  war  "); break;
  			  case   PEACE: buffer.append(" peace "); break;
  		 		case NEUTRAL: buffer.append("neutral"); break;
				default:
					break;

  			}
  		if (i < fPt.length - 1) { buffer.append("  "); }
  	  }
  	  buffer.append('\n');
  
  	  getReport().add(Report.POLITICS, buffer);
  
  	}
  	
  	*/
	  
	}
	
	private List<Command> getPlayerCommands(CommandList cmdList) {
		return cmdList.filter(new ICommandFilter() {
			@Override
			public boolean filter(Command cmd) {
				return (cmd.getPlayerNr() == getNumber());
			}
		}).toList();
	}
	
	@Override
	public JsonValue toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(new JsonObject());
		json.add(NUMBER, getNumber());
		json.add(USER, getUser());
		json.add(NAME, getName());
		json.add(HOME_PLANET_NR, getHomePlanetNr());
		json.add(POLITICAL_POINTS, getPoliticalPoints());
		json.add(FIGHTER_MORALE, getFighterMorale());
		json.add(TRANSPORTER_MORALE, getTransporterMorale());
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < fPoliticalTerms.length; i++) {
			jsonArray.add(fPoliticalTerms[i].toString());
		}
		json.add(POLITICAL_TERMS, jsonArray);
		return json.toJsonObject();
	}
	
	@Override
	public Player fromJson(JsonValue jsonValue) {
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		fNumber = json.getInt(NUMBER);
		setUser(json.getString(USER));
		setName(json.getString(NAME));
		setHomePlanetNr(json.getInt(HOME_PLANET_NR));
		setPoliticalPoints(json.getInt(POLITICAL_POINTS));
		setFighterMorale(json.getInt(FIGHTER_MORALE));
		setTransporterMorale(json.getInt(TRANSPORTER_MORALE));
		JsonArray jsonArray = json.getArray(POLITICAL_TERMS);
		for (int i = 0; i < jsonArray.size(); i++) {
			setPoliticalTerm(i, PoliticalTerm.valueOf(jsonArray.get(i).asString()));
		}
		return this;
	}
  
}