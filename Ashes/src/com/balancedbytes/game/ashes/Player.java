package com.balancedbytes.game.ashes;

import java.io.Serializable;
import java.util.Iterator;
// import javax.mail.*;
// import javax.mail.internet.*;

/**
 *
 */
public class Player implements Serializable {

  protected   Game game       = null;
  protected String user       = null;
  protected String name       = null;
  protected    int number     = 0;
  protected Planet homePlanet = null;
  protected    int pp         = 0;
  protected  float fm         = 0.0f;
  protected  float tm         = 0.0f;
  protected  int[] pt         = null;

  transient protected Report report = null;

  /**
   *
   */
  public Player(Game game, String user, int homePlanetNr) {
  
  	this.game = game;
  	this.user = user;
  	this.homePlanet = game.getPlanet(homePlanetNr);
    this.number = homePlanetNr;
    this.name = "player" + this.number;
  
  	pp = 0;
  	fm = 1.00f;
  	tm = 1.00f;
  
  	pt = new int[9];
    pt[0] = Parser.WAR;
    for (int i = 1; i < pt.length; i++) {
  		if (i == number) { pt[i] = Parser.PEACE; } else { pt[i] = Parser.NEUTRAL; }
    }
  
  }

  /**
   *
   */
  public float getFighterMorale() {
  	return fm;
  }

  /**
   *
   */
  public Planet getHomePlanet() {
  	return homePlanet;
  }

  /**
   *
   */
  public String getName() {
  	return name;
  }

  /**
   *
   */
  public int getNumber() {
  	return number;
  }

  /**
   *
   */
  public int getPoliticalPoints() {
  	return pp;
  }

  /**
   *
   */
  public int getPoliticalTerms(int otherPlayer) {
  	if ((otherPlayer < 0) || (otherPlayer > pt.length)) { return -1; }
  	return pt[otherPlayer];
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
  	return tm;
  }

  /**
   *
   */
  public String getUser() {
  	return user;
  }

  /**
   *  Play a full turn for this player with the given commands.
   */
  public void phase(int phaseNr, CommandList cmdList) {
  	StringBuffer buffer = new StringBuffer();
  	Iterator iterator = null;
  	Command cmd = null;
  
  	if (phaseNr > 0) {
  
  	} else {
  
  	  iterator = cmdList.iterator();
  	  while (iterator.hasNext()) {
  			cmd = (Command)iterator.next();
  			if (cmd.getPlayer() == number) {
  			  switch (cmd.getToken()) {
  					case Parser.DECLARE:
  			 			pt[cmd.getDestination()] = cmd.getType();
  				  	break;
  					case Parser.PLAYERNAME:
  				 		name = cmd.getText();
  					  break;
  				}
  			}
  	  }
  
  	  if (this.fm < 0.96f) { this.fm += 0.05; }
  	  if (this.tm < 0.96f) { this.tm += 0.05; }
  
  	  buffer.append("\nplayer1  player2  player3  player4  player5  player6  player7  player8\n");
  	  for (int i = 1; i < pt.length; i++) {
  			switch (pt[i]) {
  			  case     Parser.WAR: buffer.append("  war  "); break;
  			  case   Parser.PEACE: buffer.append(" peace "); break;
  		 		case Parser.NEUTRAL: buffer.append("neutral"); break;
  			}
  		if (i < pt.length - 1) { buffer.append("  "); }
  	  }
  	  buffer.append('\n');
  
  	  getReport().add(Report.POLITICS, buffer);
  
  	}
  }

  /**
   *
   */
  public void setFighterMorale(float fm) {
  	this.fm = fm;
  	if (this.fm < 0.5f) { this.fm = 0.5f; }
  	if (this.fm > 1.5f) { this.fm = 1.5f; }
  }

  /**
   *
   */
  public void setHomePlanet(Planet homePlanet) {
    this.homePlanet = homePlanet;
  }

  /**
   *
   */
  public void setName(String name) {
  	this.name = name;
  }

  /**
   *
   */
  public void setTransporterMorale(float tm) {
  	this.tm = tm;
  	if (this.tm < 0.5f) { this.tm = 0.5f; }
  	if (this.tm > 1.5f) { this.tm = 1.5f; }
  }
  
}