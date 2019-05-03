package com.balancedbytes.game.ashes.model;

import com.balancedbytes.game.ashes.Report;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.parser.ParserToken;

/**
 *
 */
public class Player {

  private transient Game fGame;
  
  private String fUser;
  private String fName;
  private int fNr;
  private int fHomePlanetNr;
  private int fPp;
  private float fFm;
  private float fTm;
  private ParserToken[] fPt;

  transient private Report report = null;

  /**
   *
   */
  public Player(Game game, String user, int nr) {
  
  	fGame = game;
  	fUser = user;
    fNr = nr;
    fHomePlanetNr = nr;
    fName = "player" + fNr;
  
  	fPp = 0;
  	fFm = 1.00f;
  	fTm = 1.00f;
  
  	fPt = new ParserToken[9];
    fPt[0] = ParserToken.WAR;
    for (int i = 1; i < fPt.length; i++) {
  		if (i == fNr) { fPt[i] = ParserToken.PEACE; } else { fPt[i] = ParserToken.NEUTRAL; }
    }
  
  }
  
  public int getHomePlanetNr() {
	  return fHomePlanetNr;
  }
  
  public void setHomePlanetNr(int nr) {
	  fHomePlanetNr = nr;
  }
  
  /**
   *
   */
  public float getFighterMorale() {
  	return fFm;
  }

  /**
   *
   */
  public String getName() {
  	return fName;
  }

  /**
   *
   */
  public int getNumber() {
  	return fNr;
  }

  /**
   *
   */
  public int getPoliticalPoints() {
  	return fPp;
  }

  /**
   *
   */
  public ParserToken getPoliticalTerms(int otherPlayer) {
  	if ((otherPlayer < 0) || (otherPlayer > fPt.length)) { return null; }
  	return fPt[otherPlayer];
  }

  /**
   *
   */
  public Report getReport() {
  	if (report == null) { report = new Report(); }
  	return report;
  }

  /**
   *
   */
  public float getTransporterMorale() {
  	return fTm;
  }

  /**
   *
   */
  public String getUser() {
  	return fUser;
  }

  /**
   *  Play a full turn for this player with the given commands.
   */
  public void phase(int phaseNr, CommandList cmdList) {
	  
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

  /**
   *
   */
  public void setFighterMorale(float fm) {
  	fFm = fm;
  	if (fFm < 0.5f) { fFm = 0.5f; }
  	if (fFm > 1.5f) { fFm = 1.5f; }
  }

  /**
   *
   */
  public void setName(String name) {
  	fName = name;
  }

  /**
   *
   */
  public void setTransporterMorale(float tm) {
  	fTm = tm;
  	if (fTm < 0.5f) { fTm = 0.5f; }
  	if (fTm > 1.5f) { fTm = 1.5f; }
  }
  
}