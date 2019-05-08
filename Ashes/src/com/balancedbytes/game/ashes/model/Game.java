package com.balancedbytes.game.ashes.model;

import com.balancedbytes.game.ashes.command.CommandList;

/**
 * Control class for each individual game.
 * Management on a turn by turn basis.
 */
public class Game {

	public final static int NR_PLAYERS = 8;
  	public final static int NR_PLANETS = 40;

  	// planetary distances (in D+x)
  	private final static int[][] DISTANCES = {
	  	{-1,1,3,3,3,3,3,2,0,2,3,3,3,3,3,2,1,1,1,1,1,1,1,1,2,2,1,2,2,2,2,2,3,3,3,3,2,2,1,2},  // 01
  		{1,-1,2,2,3,3,3,3,2,0,1,2,3,3,3,3,3,0,2,1,0,3,2,1,3,2,2,2,1,2,3,2,2,3,2,3,3,2,2,1},  // 02
  		{3,2,-1,2,3,3,3,3,3,1,0,1,3,3,3,3,3,2,3,3,2,3,3,2,3,3,3,2,2,2,3,3,2,3,2,3,3,3,3,1},  // 03
  		{3,2,2,-1,1,3,3,3,3,2,1,0,2,3,3,3,3,3,3,2,2,3,3,2,3,2,2,1,1,1,2,1,0,1,1,3,3,2,2,1},  // 04
  		{3,3,3,1,-1,2,3,3,3,3,3,2,0,2,3,3,3,3,3,2,3,2,2,2,2,1,2,1,2,1,1,1,1,0,1,1,3,2,2,2},  // 05
  		{3,3,3,3,2,-1,2,2,3,3,3,3,1,0,1,2,3,3,2,3,3,2,2,3,1,1,2,2,3,2,1,2,3,2,2,0,1,2,2,3},  // 06
  		{3,3,3,3,3,2,-1,2,3,3,3,3,3,1,0,1,2,3,2,3,3,2,2,3,2,3,3,3,3,3,2,3,3,3,3,2,1,2,3,3},  // 07
  		{2,3,3,3,3,2,2,-1,2,3,3,3,3,2,1,0,0,3,1,2,3,1,1,3,1,2,2,3,3,3,2,3,3,3,3,3,1,2,2,3},  // 08
  		{0,2,3,3,3,3,3,2,-1,2,3,3,3,3,3,3,2,1,1,1,2,2,2,2,3,3,2,2,3,3,3,3,3,3,3,3,3,2,2,3},  // 09
  		{2,0,1,2,3,3,3,3,2,-1,1,2,3,3,3,3,3,1,3,2,0,3,2,1,3,3,2,2,1,2,3,2,2,3,2,3,3,3,2,1},  // 10
  		{3,1,0,1,3,3,3,3,3,1,-1,1,3,3,3,3,3,2,3,2,1,3,3,2,3,3,2,2,1,2,3,2,1,3,2,3,3,3,2,0},  // 11
  		{3,2,1,0,2,3,3,3,3,2,1,-1,2,3,3,3,3,3,3,3,2,3,3,2,3,2,2,1,1,1,3,2,0,2,1,3,3,3,2,1},  // 12
  		{3,3,3,2,0,1,3,3,3,3,3,2,-1,2,3,3,3,3,3,3,3,2,2,2,2,1,2,2,2,2,1,1,2,0,1,0,2,2,2,3},  // 13
  		{3,3,3,3,2,0,1,2,3,3,3,3,2,-1,1,1,2,3,2,3,3,1,2,3,1,2,2,3,3,3,1,2,3,2,3,1,0,2,2,3},  // 14
  		{3,3,3,3,3,1,0,1,3,3,3,3,3,1,-1,1,2,3,2,3,3,2,2,3,1,2,3,3,3,3,2,3,3,3,3,2,0,2,3,3},  // 15
  		{2,3,3,3,3,2,1,0,3,3,3,3,3,1,1,-1,1,3,1,2,3,1,1,3,1,2,2,3,3,3,2,2,3,3,3,2,0,1,2,3},  // 16
  		{1,3,3,3,3,3,2,0,2,3,3,3,3,2,2,1,-1,2,0,1,3,1,1,2,1,2,2,2,3,3,2,2,3,3,3,3,1,1,2,3},  // 17
  		{1,0,2,3,3,3,3,3,1,1,2,3,3,3,3,3,2,-1,1,0,0,2,1,0,2,2,1,1,1,2,2,2,2,2,2,3,3,2,1,1},  // 18
  		{1,2,3,3,3,2,2,1,1,3,3,3,3,2,2,1,0,1,-1,1,2,0,0,2,1,1,1,2,2,2,2,2,3,2,3,2,1,1,1,3},  // 19
  		{1,1,3,2,2,3,3,2,1,2,2,3,3,3,3,2,1,0,1,-1,1,1,0,0,1,1,0,1,1,1,2,1,2,2,2,3,2,1,0,2},  // 20
  		{1,0,2,2,3,3,3,3,2,0,1,2,3,3,3,3,3,0,2,1,-1,2,2,0,3,2,1,1,0,1,2,2,1,2,2,3,3,2,1,0},  // 21
  		{1,3,3,3,2,2,2,1,2,3,3,3,2,1,2,1,1,2,0,1,2,-1,0,2,0,0,1,1,2,2,1,1,2,2,2,1,1,0,1,3},  // 22
  		{1,2,3,3,2,2,2,1,2,2,3,3,2,2,2,1,1,1,0,0,2,0,-1,1,1,0,0,1,2,1,1,1,2,2,2,2,1,0,1,2},  // 23
  		{1,1,2,2,2,3,3,3,2,1,2,2,2,3,3,3,2,0,2,0,0,2,1,-1,2,1,0,0,0,1,2,1,1,2,1,3,3,1,0,1},  // 24
  		{2,3,3,3,2,1,2,1,3,3,3,3,2,1,1,1,1,2,1,1,3,0,1,2,-1,0,1,2,2,2,0,1,2,2,2,1,0,0,1,2},  // 25
  		{2,2,3,2,1,1,3,2,3,3,3,2,1,2,2,2,2,2,1,1,2,0,0,1,0,-1,0,1,1,1,0,0,1,1,1,1,1,0,0,2},  // 26
  		{1,2,3,2,2,2,3,2,2,2,2,2,2,2,3,2,2,1,1,0,1,1,0,0,1,0,-1,0,1,0,1,0,1,1,1,3,3,0,0,3},  // 27
  		{2,2,2,1,1,2,3,3,2,2,2,1,2,3,3,3,2,1,2,1,1,1,1,0,2,1,0,-1,0,0,1,0,1,1,1,2,2,1,0,1},  // 28
  		{2,1,2,1,2,3,3,3,3,1,1,1,2,3,3,3,3,1,2,1,0,2,2,0,2,1,1,0,-1,0,2,1,0,1,1,3,3,1,0,0},  // 29
  		{2,2,2,1,1,2,3,3,3,2,2,1,2,3,3,3,3,2,2,1,1,2,1,1,2,1,0,0,0,-1,1,0,0,1,0,2,3,1,0,1},  // 30
  		{2,3,3,2,1,1,2,2,3,3,3,3,1,1,2,2,2,2,2,2,2,1,1,2,0,0,1,1,2,1,-1,1,2,1,1,0,1,0,1,3},  // 31
  		{2,2,3,1,1,2,3,3,3,2,2,2,1,2,3,2,2,2,2,1,2,1,1,1,1,0,0,0,1,0,1,-1,1,0,0,1,2,1,0,2},  // 32
  		{3,2,2,0,1,3,3,3,3,2,1,0,2,3,3,3,3,2,3,2,1,2,2,1,2,1,1,1,0,0,2,1,-1,1,0,2,3,2,1,1},  // 33
  		{3,3,3,1,0,2,3,3,3,3,3,2,0,2,3,3,3,2,2,2,2,2,2,2,2,1,1,1,1,1,1,0,1,-1,0,1,2,1,1,2},  // 34
  		{3,2,2,1,1,2,3,3,3,2,2,1,1,3,3,3,3,2,3,2,2,2,2,1,2,1,1,1,1,0,1,0,0,0,-1,2,3,1,1,1},  // 35
  		{3,3,3,3,1,0,2,3,3,3,3,3,0,1,2,2,3,3,2,3,3,1,2,3,1,1,3,2,3,2,0,1,2,1,2,-1,1,1,2,3},  // 36
  		{2,3,3,3,3,1,1,1,3,3,3,3,2,0,0,0,1,3,1,2,3,1,1,3,0,1,3,2,3,3,1,2,3,2,3,1,-1,1,2,3},  // 37
  		{2,2,3,2,2,2,2,2,2,3,3,3,2,2,2,1,1,2,1,1,2,0,0,1,0,0,0,1,1,1,0,1,2,1,1,1,1,-1,0,2},  // 38
  		{1,2,3,2,2,2,3,2,2,2,2,2,2,2,3,2,2,1,1,0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,2,2,0,-1,1},  // 39
  		{2,1,1,1,2,3,3,3,3,1,0,1,3,3,3,3,3,1,3,2,0,3,2,1,2,2,3,1,0,1,3,2,1,2,1,3,3,2,1,-1}   // 40
  	};
  
  	private int fNumber;
  	private int fTurn;
  	private Planet[] fPlanets;
  	private Player[] fPlayers;

	/**
	 * Start a new Game with given planets.
	 */
	public Game(int number, String[] users) {

		fNumber = number;
		fPlayers = new Player[NR_PLAYERS];
		fPlayers[0] = new Player(null, 0);
		for (int i = 0; i < users.length; i++) {
			fPlayers[i] = new Player(users[i], i + 1);
		}
  
	  	fTurn = 1;
	  	fPlanets = new Planet[NR_PLANETS];
  
	  	// planet name, planet number, player, WF, HD, PR, FI, TR, PDU
  		fPlanets[0]  = new Planet("Earth",            1, 1, 240, 6,  683, 15,  4,  6);
  		fPlanets[1]  = new Planet("Crab",             2, 2, 240, 6,  666, 15,  3,  6);
  		fPlanets[2]  = new Planet("Eastside",         3, 3, 240, 6,  833, 15, 10,  6);
  		fPlanets[3]  = new Planet("Nameless",         4, 4, 240, 6,  650, 15,  3,  6);
  		fPlanets[4]  = new Planet("Lenin",            5, 5, 240, 6,  616, 15,  1,  6);
  		fPlanets[5]  = new Planet("Shadow",           6, 6, 240, 6,  700, 15,  5,  6);
  		fPlanets[6]  = new Planet("Sombrero",         7, 7, 240, 6,  816, 15, 10,  6);
  		fPlanets[7]  = new Planet("Lone Star",        8, 8, 240, 6,  716, 15,  5,  6);
  		fPlanets[8]  = new Planet("Barnard's Arrow",  9, 1, 240, 6,  500, 10,  9,  7);
  		fPlanets[9] =  new Planet("Outpost",         10, 2, 110, 3, 1083, 10,  6, 10);
  		fPlanets[10] = new Planet("Desert Rock",     11, 3, 180, 5,  666, 10,  7,  6);
  		fPlanets[11] = new Planet("Mechanica",       12, 4, 180, 2,  500, 10,  5, 11);
  		fPlanets[12] = new Planet("Last Hope",       13, 5, 205, 6,  583, 10,  1, 12);
  		fPlanets[13] = new Planet("Wilderness",      14, 6,  90, 3,  333, 10,  6,  8);
  		fPlanets[14] = new Planet("Tramp",           15, 7, 160, 4,  750, 10,  8,  6);
  		fPlanets[15] = new Planet("New Nome",        16, 8, 120, 3, 1000, 10,  5,  9);
  		fPlanets[16] = new Planet("Kalgourlie",      17, 0, 160, 4, 1000,  6,  3,  4);
  		fPlanets[17] = new Planet("Draken",          18, 0, 180, 5,  833,  6,  3,  5);
  		fPlanets[18] = new Planet("Rivet",           19, 0, 220, 6,  666,  6,  3,  6);
  		fPlanets[19] = new Planet("Crossland",       20, 0, 230, 6, 1000,  6,  3,  6);
  		fPlanets[20] = new Planet("Beyond",          21, 0,  80, 2,  666,  6,  1,  2);
  		fPlanets[21] = new Planet("New Earth",       22, 0, 110, 3, 1000,  6,  2,  3);
  		fPlanets[22] = new Planet("Scott's Home",    23, 0, 150, 4,  833,  6,  3,  4);
  		fPlanets[23] = new Planet("Newton",          24, 0, 120, 3, 1166,  6,  3,  3);
  		fPlanets[24] = new Planet("Murphy",          25, 0,  85, 3, 1333,  6,  1,  3);
  		fPlanets[25] = new Planet("Aitchison",       26, 0, 100, 3,  583,  6,  2,  3);
  		fPlanets[26] = new Planet("Landfall",        27, 0,  90, 3, 1083,  6,  1,  3);
  		fPlanets[27] = new Planet("Atlas",           28, 0,  80, 2,  833,  6,  1,  2);
  		fPlanets[28] = new Planet("New Mecca",       29, 0,  80, 2, 1000,  6,  1,  2);
  		fPlanets[29] = new Planet("Evergreen",       30, 0, 110, 3, 1166,  6,  2,  3);
  		fPlanets[30] = new Planet("New Jerusalem",   31, 0, 100, 3,  916,  6,  2,  3);
  		fPlanets[31] = new Planet("Lesser Evil",     32, 0, 160, 4, 1083,  6,  3,  4);
  		fPlanets[32] = new Planet("Lermontov",       33, 0,  90, 3, 1250,  6,  1,  3);
  		fPlanets[33] = new Planet("Einstein",        34, 0, 220, 6, 1250,  6,  3,  6);
  		fPlanets[34] = new Planet("Dunroamin",       35, 0,  80, 5,  750,  6,  3,  5);
  		fPlanets[35] = new Planet("Strife",          36, 0, 140, 4, 1166,  6,  3,  4);
  		fPlanets[36] = new Planet("Potter's Bar",    37, 0,  20, 1,  500,  2,  0,  1);
  		fPlanets[37] = new Planet("Kaironow",        38, 0,  20, 1,  500,  2,  0,  1);
  		fPlanets[38] = new Planet("Stormbringer",    39, 0,  20, 1,  500,  2,  0,  1);
  		fPlanets[39] = new Planet("Mike's Dream",    40, 0,  20, 1,  500,  2,  0,  1);
  		
  	}
	
	public static int getDistance(int fromPlanetNr, int toPlanetNr) {
		if ((fromPlanetNr < 1) || (fromPlanetNr > 40) || (toPlanetNr < 1) || (toPlanetNr > 40)) {
			return -1;
		}
		return DISTANCES[fromPlanetNr - 1][toPlanetNr - 1];
	}	

	/**
	 * Game number.
	 */
	public int getNumber() {
		return fNumber;
	}

	/**
	 * Planet with given number.
	 */
	public Planet getPlanet(int nr) {
		if ((nr <= 0) || (nr > fPlanets.length)) {
			return null;
		} else {
			return fPlanets[nr - 1];
		}
	}

	/**
	 * Player with given number
	 */
	public Player getPlayer(int nr) {
		if ((nr <= 0) || (nr > fPlayers.length)) {
			return null;
		} else {
			return fPlayers[nr - 1];
		}
	}

	/**
	 * Number of player with given username or 0 if no player with that name.
	 */
	public int getPlayerNrOfUser(String userName) {
		for (int i = 0; i < fPlayers.length; i++) {
			if (fPlayers[i].getUser().equals(userName)) {
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * Turn number.
	 */
	public int getTurn() {
		return fTurn;
	}
	
	/**
	 * 
	 */
	public PoliticalTerm getPoliticalTerm(int playerNrA, int playerNrB) {
		if (playerNrA == playerNrB) {
			return PoliticalTerm.PEACE;
		}
		PoliticalTerm ptAB = getPlayer(playerNrA).getPoliticalTerm(playerNrB);
		PoliticalTerm ptBA = getPlayer(playerNrB).getPoliticalTerm(playerNrA);
		if ((ptAB == PoliticalTerm.PEACE) && (ptBA == PoliticalTerm.PEACE)) {
			return PoliticalTerm.PEACE;
		} else if ((ptAB == PoliticalTerm.WAR) || (ptBA == PoliticalTerm.WAR)) {
			return PoliticalTerm.WAR;
		} else { 
			return PoliticalTerm.NEUTRAL;
		}
	}
	
	/**
	 * 
	 */
	public void addReportToAllPlayers(Report report) {
		for (Player player : fPlayers) {
			player.getReporting().add(report);
		}
	}

	/**
	 * Play a full turn for this game with the given commands.
	 */
	public void nextTurn(CommandList cmdList) {

		/*

		for (int i = 0; i < NR_PLAYERS; i++) {
			getPlayer(i).phase(0, cmdList);
		}

		Iterator<OldCommand> iterator = cmdList.iterator();
		while (iterator.hasNext()) {
			cmd = (OldCommand) iterator.next();
			if (cmd.getToken() == Parser.MESSAGE) {
				String header = "\n" + getPlayer(cmd.getPlayer()).getName() + " sends:\n";
				int dest = cmd.getDestination();
				if (dest > 0) {
					getPlayer(dest).getReport().add(Report.MESSAGES, header + cmd.getText());
				} else {
					Report.addAll(Report.MESSAGES, header + cmd.getText());
				}
			}
		}

			*/

		/*
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j < NR_PLANETS; j++) {
				getPlanet(j).phase(i, cmdList);
			}
		}
		*/
  
	  	/*
	  	enum = cset.elements();
	  	while (enum.hasMoreElements()) {
	  	  cmd = (Command)enum.nextElement();
	  	  switch (cmd.getToken()) {
	  		case Parser.SPY:
	  		  int spyLevel = 3;
	  		  boolean detected = false;
	  		  Planet planet = getPlanet(cmd.getSource());
	  		  Player player = getPlayer(cmd.getPlayer());
	  		  if ((Math.random() * 10) < (planet.getPlanetaryMorale() * 2)) {
	  			detected = true; spyLevel = 0;
	  		  } else {
	  			int spyProb = (int)(Math.random() * 100 + 1);
	  			if (spyProb > 20) { spyLevel = 2; }
	  			if (spyProb > 40) { spyLevel = 1; }
	  			if (spyProb > 60) { spyLevel = 0; }
	  		  }
	  
	  		  log.debug(DEBUG_MODULE, DEBUG_LEVEL, "Player " + cmd.getPlayer() + " uses level " + spyLevel + " spy - detected: " + detected);
	  
	  		  player.getReport().add(Report.INTELLIGENCE, "\n" + planet.spy(spyLevel));
	  		  if (detected) {
	  			player.getReport().add(Report.INTELLIGENCE, "Spy has been detected.\n");
	  			Player victim = getPlayer(planet.getPlayerNr());
	  			victim.getReport().add(Report.INTELLIGENCE, "A spy from " + player.getName() + " dected on " + planet.getName() + " ("+ planet.getNr() + ")\n");
	  		  }
	  		  break;
	  	  }
	  	}
	  
	  	Session session = Session.getDefaultInstance(AshesOfEmpire.getProperties(), null);
	  	for (int i = 0; i < NR_PLAYERS; i++) {
	  	  Report report = players[i].getReport();
	  	  String subject =  "Ashes Of Empire Game " + nr + " Turn " + turn + " Player " +  i;
	  	  getPlayer(i).mailTo(session, subject, report.toString());
	  	}
	  	*/
  
		// increase turn
		fTurn++;
	}
  
}