package com.balancedbytes.game.ashes;

import java.util.Iterator;

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
  
  	private int fNr;
  	private int fTurn;
  	private Planet[] fPlanets;
  	private Player[] fPlayers;

	/**
	 * Start a new Game with given planets.
	 */
	public Game(int nr, String[] users) {

		fNr = nr;
		fPlayers = new Player[NR_PLAYERS];
		fPlayers[0] = new Player(this, null, 0);
		for (int i = 0; i < users.length; i++) {
			fPlayers[i] = new Player(this, users[i], i + 1);
		}
  
	  	fTurn = 1;
	  	fPlanets = new Planet[NR_PLANETS];
  
	  	// game, planet name, planet number, player, A, HD, PR*1000, FI, TR, PDU, distances
  		fPlanets[0]  = new Planet(this, "Earth",            1, 1, 240, 6,  683, 15,  4,  6, DISTANCES[0]);
  		fPlanets[1]  = new Planet(this, "Crab",             2, 2, 240, 6,  666, 15,  3,  6, DISTANCES[1]);
  		fPlanets[2]  = new Planet(this, "Eastside",         3, 3, 240, 6,  833, 15, 10,  6, DISTANCES[2]);
  		fPlanets[3]  = new Planet(this, "Nameless",         4, 4, 240, 6,  650, 15,  3,  6, DISTANCES[3]);
  		fPlanets[4]  = new Planet(this, "Lenin",            5, 5, 240, 6,  616, 15,  1,  6, DISTANCES[4]);
  		fPlanets[5]  = new Planet(this, "Shadow",           6, 6, 240, 6,  700, 15,  5,  6, DISTANCES[5]);
  		fPlanets[6]  = new Planet(this, "Sombrero",         7, 7, 240, 6,  816, 15, 10,  6, DISTANCES[6]);
  		fPlanets[7]  = new Planet(this, "Lone Star",        8, 8, 240, 6,  716, 15,  5,  6, DISTANCES[7]);
  		fPlanets[8]  = new Planet(this, "Barnard's Arrow",  9, 1, 240, 6,  500, 10,  9,  7, DISTANCES[8]);
  		fPlanets[9] =  new Planet(this, "Outpost",         10, 2, 110, 3, 1083, 10,  6, 10, DISTANCES[9]);
  		fPlanets[10] = new Planet(this, "Desert Rock",     11, 3, 180, 5,  666, 10,  7,  6, DISTANCES[10]);
  		fPlanets[11] = new Planet(this, "Mechanica",       12, 4, 180, 2,  500, 10,  5, 11, DISTANCES[11]);
  		fPlanets[12] = new Planet(this, "Last Hope",       13, 5, 205, 6,  583, 10,  1, 12, DISTANCES[12]);
  		fPlanets[13] = new Planet(this, "Wilderness",      14, 6,  90, 3,  333, 10,  6,  8, DISTANCES[13]);
  		fPlanets[14] = new Planet(this, "Tramp",           15, 7, 160, 4,  750, 10,  8,  6, DISTANCES[14]);
  		fPlanets[15] = new Planet(this, "New Nome",        16, 8, 120, 3, 1000, 10,  5,  9, DISTANCES[15]);
  		fPlanets[16] = new Planet(this, "Kalgourlie",      17, 0, 160, 4, 1000,  6,  3,  4, DISTANCES[16]);
  		fPlanets[17] = new Planet(this, "Draken",          18, 0, 180, 5,  833,  6,  3,  5, DISTANCES[17]);
  		fPlanets[18] = new Planet(this, "Rivet",           19, 0, 220, 6,  666,  6,  3,  6, DISTANCES[18]);
  		fPlanets[19] = new Planet(this, "Crossland",       20, 0, 230, 6, 1000,  6,  3,  6, DISTANCES[19]);
  		fPlanets[20] = new Planet(this, "Beyond",          21, 0,  80, 2,  666,  6,  1,  2, DISTANCES[20]);
  		fPlanets[21] = new Planet(this, "New Earth",       22, 0, 110, 3, 1000,  6,  2,  3, DISTANCES[21]);
  		fPlanets[22] = new Planet(this, "Scott's Home",    23, 0, 150, 4,  833,  6,  3,  4, DISTANCES[22]);
  		fPlanets[23] = new Planet(this, "Newton",          24, 0, 120, 3, 1166,  6,  3,  3, DISTANCES[23]);
  		fPlanets[24] = new Planet(this, "Murphy",          25, 0,  85, 3, 1333,  6,  1,  3, DISTANCES[24]);
  		fPlanets[25] = new Planet(this, "Aitchison",       26, 0, 100, 3,  583,  6,  2,  3, DISTANCES[25]);
  		fPlanets[26] = new Planet(this, "Landfall",        27, 0,  90, 3, 1083,  6,  1,  3, DISTANCES[26]);
  		fPlanets[27] = new Planet(this, "Atlas",           28, 0,  80, 2,  833,  6,  1,  2, DISTANCES[27]);
  		fPlanets[28] = new Planet(this, "New Mecca",       29, 0,  80, 2, 1000,  6,  1,  2, DISTANCES[28]);
  		fPlanets[29] = new Planet(this, "Evergreen",       30, 0, 110, 3, 1166,  6,  2,  3, DISTANCES[29]);
  		fPlanets[30] = new Planet(this, "New Jerusalem",   31, 0, 100, 3,  916,  6,  2,  3, DISTANCES[30]);
  		fPlanets[31] = new Planet(this, "Lesser Evil",     32, 0, 160, 4, 1083,  6,  3,  4, DISTANCES[31]);
  		fPlanets[32] = new Planet(this, "Lermontov",       33, 0,  90, 3, 1250,  6,  1,  3, DISTANCES[32]);
  		fPlanets[33] = new Planet(this, "Einstein",        34, 0, 220, 6, 1250,  6,  3,  6, DISTANCES[33]);
  		fPlanets[34] = new Planet(this, "Dunroamin",       35, 0,  80, 5,  750,  6,  3,  5, DISTANCES[34]);
  		fPlanets[35] = new Planet(this, "Strife",          36, 0, 140, 4, 1166,  6,  3,  4, DISTANCES[35]);
  		fPlanets[36] = new Planet(this, "Potter's Bar",    37, 0,  20, 1,  500,  2,  0,  1, DISTANCES[36]);
  		fPlanets[37] = new Planet(this, "Kaironow",        38, 0,  20, 1,  500,  2,  0,  1, DISTANCES[37]);
  		fPlanets[38] = new Planet(this, "Stormbringer",    39, 0,  20, 1,  500,  2,  0,  1, DISTANCES[38]);
  		fPlanets[39] = new Planet(this, "Mike's Dream",    40, 0,  20, 1,  500,  2,  0,  1, DISTANCES[39]);
  	}

	/**
	 * Game number.
	 */
	public int getNr() {
		return fNr;
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
	 * Number of planet with given name or 0 if no planet with that name.
	 */
	public int getPlanetNr(String planetName) {
		for (int i = 0; i < fPlanets.length; i++) {
			if (fPlanets[i].getName().equals(planetName)) {
				return i + 1;
			}
		}
		return 0;
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
	 * Number of player with given name or 0 if no player with that name.
	 */
	public int getPlayerNr(String playerName) {
		for (int i = 0; i < fPlayers.length; i++) {
			if (fPlayers[i].getName().equals(playerName)) {
				return i + 1;
			}
		}
		return 0;
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
	 * Play a full turn for this game with the given commands.
	 */
	public void nextTurn(CommandList cmdList) {
		Command cmd = null;

		for (int i = 0; i < NR_PLAYERS; i++) {
			getPlayer(i).phase(0, cmdList);
		}

		Iterator<Command> iterator = cmdList.iterator();
		while (iterator.hasNext()) {
			cmd = (Command) iterator.next();
			/*
			if (cmd.getToken() == Parser.MESSAGE) {
				String header = "\n" + getPlayer(cmd.getPlayer()).getName() + " sends:\n";
				int dest = cmd.getDestination();
				if (dest > 0) {
					getPlayer(dest).getReport().add(Report.MESSAGES, header + cmd.getText());
				} else {
					Report.addAll(Report.MESSAGES, header + cmd.getText());
				}
			}
			*/
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 1; j < NR_PLANETS; j++) {
				getPlanet(j).phase(i, cmdList);
			}
		}
  
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