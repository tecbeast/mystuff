package com.balancedbytes.game.ashes;

import java.util.Iterator;
// import javax.mail.*;
// import javax.mail.internet.*;

import com.balancedbytes.game.ashes.parser.Parser;

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
  private int[] fPt;

  transient private Report report = null;

  /**
   *
   */
  public Player(Game game, String user, int nr) {
  
  	this.fGame = game;
  	this.fUser = user;
    this.fNr = nr;
    this.fHomePlanetNr = nr;
    this.fName = "player" + this.fNr;
  
  	fPp = 0;
  	fFm = 1.00f;
  	fTm = 1.00f;
  
  	fPt = new int[9];
    fPt[0] = Parser.WAR;
    for (int i = 1; i < fPt.length; i++) {
  		if (i == fNr) { fPt[i] = Parser.PEACE; } else { fPt[i] = Parser.NEUTRAL; }
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
  public int getPoliticalTerms(int otherPlayer) {
  	if ((otherPlayer < 0) || (otherPlayer > fPt.length)) { return -1; }
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
  	StringBuffer buffer = new StringBuffer();
  	Iterator<Command> iterator = null;
  	Command cmd = null;
  
  	if (phaseNr > 0) {
  
  	} else {
  
  	  iterator = cmdList.iterator();
  	  while (iterator.hasNext()) {
  			cmd = (Command)iterator.next();
  			if (cmd.getPlayer() == fNr) {
  			  switch (cmd.getToken()) {
  					case Parser.DECLARE:
  			 			fPt[cmd.getDestination()] = cmd.getType();
  				  	break;
  					case Parser.PLAYERNAME:
  				 		fName = cmd.getText();
  					  break;
  				}
  			}
  	  }
  
  	  if (this.fFm < 0.96f) { this.fFm += 0.05; }
  	  if (this.fTm < 0.96f) { this.fTm += 0.05; }
  
  	  buffer.append("\nplayer1  player2  player3  player4  player5  player6  player7  player8\n");
  	  for (int i = 1; i < fPt.length; i++) {
  			switch (fPt[i]) {
  			  case     Parser.WAR: buffer.append("  war  "); break;
  			  case   Parser.PEACE: buffer.append(" peace "); break;
  		 		case Parser.NEUTRAL: buffer.append("neutral"); break;
  			}
  		if (i < fPt.length - 1) { buffer.append("  "); }
  	  }
  	  buffer.append('\n');
  
  	  getReport().add(Report.POLITICS, buffer);
  
  	}
  }

  /**
   *
   */
  public void setFighterMorale(float fm) {
  	this.fFm = fm;
  	if (this.fFm < 0.5f) { this.fFm = 0.5f; }
  	if (this.fFm > 1.5f) { this.fFm = 1.5f; }
  }

  /**
   *
   */
  public void setName(String name) {
  	this.fName = name;
  }

  /**
   *
   */
  public void setTransporterMorale(float tm) {
  	this.fTm = tm;
  	if (this.fTm < 0.5f) { this.fTm = 0.5f; }
  	if (this.fTm > 1.5f) { this.fTm = 1.5f; }
  }
  
}