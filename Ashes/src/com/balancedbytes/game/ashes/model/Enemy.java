package com.balancedbytes.game.ashes.model;

/**
 *
 */
public class Enemy extends Player {

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
		
		super();

		setPlayerNr(0);
		setUserName("");
		setPlayerName("Neutral");
		setPoliticalPoints(0);
		setFighterMorale(100);
		setTransporterMorale(100);

		for (int i = 0; i < getPoliticalTerms().length; i++) {
			setPoliticalTerm(i, (i == 0) ? PoliticalTerm.PEACE : PoliticalTerm.WAR);
		}

	}
	
	/**
	 * 
	 */
	public void setNewHomePlanet(Game game) {
		if (game == null) {
			return;
		}
		PlanetList planets = new PlanetList();
		for (Planet planet : game.getPlanets()) {
			if (planet.getPlayerNr() == 0) {
				planets.add(planet);
			}
		}
		if (planets.size() == 0) {
			return;
		}
		Planet homePlanet = planets.get((int) (planets.size() * game.randomDouble()));
		setHomePlanetNr(homePlanet.getPlanetNr());
	}

}