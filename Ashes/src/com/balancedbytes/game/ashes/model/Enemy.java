package com.balancedbytes.game.ashes.model;

/**
 *
 */
public class Enemy {

  /*
  // probabilities for neutral player
  private final static int[] SENDPROB = { 60, 30, 0 };

  private final static String[] STRATEGYNAME = {
    "agressive", "moderate", "passive"
  };

  // pdu, fp, op, rp, fy, ty
  private final static int[][] BUILDSUMPROB = {
    { 8, 13, 18, 68, 93, 100 },
    { 30, 45, 60, 90, 95, 100 },
    { 25, 33, 40, 90, 100, 100 }
  };
  */

  // private int strategy = 0; // strategy for neutral player

  /*
  // space travel and cargo ships
  
  if ((int)(Math.random() * 100 + 1) <= SENDPROB[strategy]) {
    int target = planetsInD[(int)(Math.random() * planetsInD.length)];
    if (DEBUG) { System.err.println(" sending Fleet to Planet " + target); }
  		send(flightQueue[0].getFleet(playerNr), target);
  }
  
  // building and resources
  
  if (pdu < hd) { build(Parser.PDU, hd - pdu); }
  build(Parser.FI, rare / 3 - ty);
  build(Parser.TR, ty);
  while (currentWf > 3) {
    Command cmd = new Command(Parser.BUILD);
    int buildNow = (int)(Math.random() * 100 + 1);
    if (buildNow <= BUILDSUMPROB[strategy][0]) { build(Parser.PDU, 1); }
    else if (buildNow <= BUILDSUMPROB[strategy][1]) { build(Parser.FP, 1); }
    else if (buildNow <= BUILDSUMPROB[strategy][2]) { build(Parser.OP, 1); }
    else if (buildNow <= BUILDSUMPROB[strategy][3]) { build(Parser.RP, 1); }
    else if (buildNow <= BUILDSUMPROB[strategy][4]) { build(Parser.FY, 1); }
    else { build(Parser.TY, 1); }
    cmdList.add(cmd);
  }
  	  
  return cmdList;	
  */

  /**
   *
   */
  public Enemy() {
	  /*
    super(game, "neutral", (int) (Math.random() * 25 + 16));

    this.number = 0;
    this.name = "neutral";

    pt[0] = Parser.PEACE;
    for (int i = 1; i < pt.length; i++) {
      pt[i] = Parser.WAR;
    }

    this.strategy = (int) (Math.random() * 3);
    */
  }
  
}