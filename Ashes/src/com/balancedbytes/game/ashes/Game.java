package com.balancedbytes.game.ashes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;

import de.seipler.util.log.Log;
import de.seipler.util.log.LogFactory;

/**
 * Control class for each individual game. Management on a turn by turn basis.
 */
public class Game implements Serializable {

  public final static String MODULE = "Game";
  public final static int NR_PLAYERS = 9;
  public final static int NR_PLANETS = 41;

  // planetary distances (in D+x)
  private final static int[][] distance = {
  	null,
  	{-1,-1,1,3,3,3,3,3,2,0,2,3,3,3,3,3,2,1,1,1,1,1,1,1,1,2,2,1,2,2,2,2,2,3,3,3,3,2,2,1,2},  // 01
  	{-1,1,-1,2,2,3,3,3,3,2,0,1,2,3,3,3,3,3,0,2,1,0,3,2,1,3,2,2,2,1,2,3,2,2,3,2,3,3,2,2,1},  // 02
  	{-1,3,2,-1,2,3,3,3,3,3,1,0,1,3,3,3,3,3,2,3,3,2,3,3,2,3,3,3,2,2,2,3,3,2,3,2,3,3,3,3,1},  // 03
  	{-1,3,2,2,-1,1,3,3,3,3,2,1,0,2,3,3,3,3,3,3,2,2,3,3,2,3,2,2,1,1,1,2,1,0,1,1,3,3,2,2,1},  // 04
  	{-1,3,3,3,1,-1,2,3,3,3,3,3,2,0,2,3,3,3,3,3,2,3,2,2,2,2,1,2,1,2,1,1,1,1,0,1,1,3,2,2,2},  // 05
  	{-1,3,3,3,3,2,-1,2,2,3,3,3,3,1,0,1,2,3,3,2,3,3,2,2,3,1,1,2,2,3,2,1,2,3,2,2,0,1,2,2,3},  // 06
  	{-1,3,3,3,3,3,2,-1,2,3,3,3,3,3,1,0,1,2,3,2,3,3,2,2,3,2,3,3,3,3,3,2,3,3,3,3,2,1,2,3,3},  // 07
  	{-1,2,3,3,3,3,2,2,-1,2,3,3,3,3,2,1,0,0,3,1,2,3,1,1,3,1,2,2,3,3,3,2,3,3,3,3,3,1,2,2,3},  // 08
  	{-1,0,2,3,3,3,3,3,2,-1,2,3,3,3,3,3,3,2,1,1,1,2,2,2,2,3,3,2,2,3,3,3,3,3,3,3,3,3,2,2,3},  // 09
  	{-1,2,0,1,2,3,3,3,3,2,-1,1,2,3,3,3,3,3,1,3,2,0,3,2,1,3,3,2,2,1,2,3,2,2,3,2,3,3,3,2,1},  // 10
  	{-1,3,1,0,1,3,3,3,3,3,1,-1,1,3,3,3,3,3,2,3,2,1,3,3,2,3,3,2,2,1,2,3,2,1,3,2,3,3,3,2,0},  // 11
  	{-1,3,2,1,0,2,3,3,3,3,2,1,-1,2,3,3,3,3,3,3,3,2,3,3,2,3,2,2,1,1,1,3,2,0,2,1,3,3,3,2,1},  // 12
  	{-1,3,3,3,2,0,1,3,3,3,3,3,2,-1,2,3,3,3,3,3,3,3,2,2,2,2,1,2,2,2,2,1,1,2,0,1,0,2,2,2,3},  // 13
  	{-1,3,3,3,3,2,0,1,2,3,3,3,3,2,-1,1,1,2,3,2,3,3,1,2,3,1,2,2,3,3,3,1,2,3,2,3,1,0,2,2,3},  // 14
  	{-1,3,3,3,3,3,1,0,1,3,3,3,3,3,1,-1,1,2,3,2,3,3,2,2,3,1,2,3,3,3,3,2,3,3,3,3,2,0,2,3,3},  // 15
  	{-1,2,3,3,3,3,2,1,0,3,3,3,3,3,1,1,-1,1,3,1,2,3,1,1,3,1,2,2,3,3,3,2,2,3,3,3,2,0,1,2,3},  // 16
  	{-1,1,3,3,3,3,3,2,0,2,3,3,3,3,2,2,1,-1,2,0,1,3,1,1,2,1,2,2,2,3,3,2,2,3,3,3,3,1,1,2,3},  // 17
  	{-1,1,0,2,3,3,3,3,3,1,1,2,3,3,3,3,3,2,-1,1,0,0,2,1,0,2,2,1,1,1,2,2,2,2,2,2,3,3,2,1,1},  // 18
  	{-1,1,2,3,3,3,2,2,1,1,3,3,3,3,2,2,1,0,1,-1,1,2,0,0,2,1,1,1,2,2,2,2,2,3,2,3,2,1,1,1,3},  // 19
  	{-1,1,1,3,2,2,3,3,2,1,2,2,3,3,3,3,2,1,0,1,-1,1,1,0,0,1,1,0,1,1,1,2,1,2,2,2,3,2,1,0,2},  // 20
  	{-1,1,0,2,2,3,3,3,3,2,0,1,2,3,3,3,3,3,0,2,1,-1,2,2,0,3,2,1,1,0,1,2,2,1,2,2,3,3,2,1,0},  // 21
  	{-1,1,3,3,3,2,2,2,1,2,3,3,3,2,1,2,1,1,2,0,1,2,-1,0,2,0,0,1,1,2,2,1,1,2,2,2,1,1,0,1,3},  // 22
  	{-1,1,2,3,3,2,2,2,1,2,2,3,3,2,2,2,1,1,1,0,0,2,0,-1,1,1,0,0,1,2,1,1,1,2,2,2,2,1,0,1,2},  // 23
  	{-1,1,1,2,2,2,3,3,3,2,1,2,2,2,3,3,3,2,0,2,0,0,2,1,-1,2,1,0,0,0,1,2,1,1,2,1,3,3,1,0,1},  // 24
  	{-1,2,3,3,3,2,1,2,1,3,3,3,3,2,1,1,1,1,2,1,1,3,0,1,2,-1,0,1,2,2,2,0,1,2,2,2,1,0,0,1,2},  // 25
  	{-1,2,2,3,2,1,1,3,2,3,3,3,2,1,2,2,2,2,2,1,1,2,0,0,1,0,-1,0,1,1,1,0,0,1,1,1,1,1,0,0,2},  // 26
  	{-1,1,2,3,2,2,2,3,2,2,2,2,2,2,2,3,2,2,1,1,0,1,1,0,0,1,0,-1,0,1,0,1,0,1,1,1,3,3,0,0,3},  // 27
  	{-1,2,2,2,1,1,2,3,3,2,2,2,1,2,3,3,3,2,1,2,1,1,1,1,0,2,1,0,-1,0,0,1,0,1,1,1,2,2,1,0,1},  // 28
  	{-1,2,1,2,1,2,3,3,3,3,1,1,1,2,3,3,3,3,1,2,1,0,2,2,0,2,1,1,0,-1,0,2,1,0,1,1,3,3,1,0,0},  // 29
  	{-1,2,2,2,1,1,2,3,3,3,2,2,1,2,3,3,3,3,2,2,1,1,2,1,1,2,1,0,0,0,-1,1,0,0,1,0,2,3,1,0,1},  // 30
  	{-1,2,3,3,2,1,1,2,2,3,3,3,3,1,1,2,2,2,2,2,2,2,1,1,2,0,0,1,1,2,1,-1,1,2,1,1,0,1,0,1,3},  // 31
  	{-1,2,2,3,1,1,2,3,3,3,2,2,2,1,2,3,2,2,2,2,1,2,1,1,1,1,0,0,0,1,0,1,-1,1,0,0,1,2,1,0,2},  // 32
  	{-1,3,2,2,0,1,3,3,3,3,2,1,0,2,3,3,3,3,2,3,2,1,2,2,1,2,1,1,1,0,0,2,1,-1,1,0,2,3,2,1,1},  // 33
  	{-1,3,3,3,1,0,2,3,3,3,3,3,2,0,2,3,3,3,2,2,2,2,2,2,2,2,1,1,1,1,1,1,0,1,-1,0,1,2,1,1,2},  // 34
  	{-1,3,2,2,1,1,2,3,3,3,2,2,1,1,3,3,3,3,2,3,2,2,2,2,1,2,1,1,1,1,0,1,0,0,0,-1,2,3,1,1,1},  // 35
  	{-1,3,3,3,3,1,0,2,3,3,3,3,3,0,1,2,2,3,3,2,3,3,1,2,3,1,1,3,2,3,2,0,1,2,1,2,-1,1,1,2,3},  // 36
  	{-1,2,3,3,3,3,1,1,1,3,3,3,3,2,0,0,0,1,3,1,2,3,1,1,3,0,1,3,2,3,3,1,2,3,2,3,1,-1,1,2,3},  // 37
  	{-1,2,2,3,2,2,2,2,2,2,3,3,3,2,2,2,1,1,2,1,1,2,0,0,1,0,0,0,1,1,1,0,1,2,1,1,1,1,-1,0,2},  // 38
  	{-1,1,2,3,2,2,2,3,2,2,2,2,2,2,2,3,2,2,1,1,0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,2,2,0,-1,1},  // 39
  	{-1,2,1,1,1,2,3,3,3,3,1,0,1,3,3,3,3,3,1,3,2,0,3,2,1,2,2,3,1,0,1,3,2,1,2,1,3,3,2,1,-1}   // 40
  };

  private int nr;
  private int turn;
  private Planet[] planets;
  private Player[] players;

  /**
   *  Start a new Game with given planets.
   */
  public Game(int nr, String[] users) {
  	this.nr = nr;
  	players = new Player[NR_PLAYERS];
  	players[0] = new Player(this, null, 0);
  	for (int i = 0; i < users.length; i++) {
  	  players[i+1] = new Player(this, users[i], i+1);
  	}
  
  	turn = 1;
  	planets = new Planet[41];
  
  	// game, planet name, planet number, player, A, HD, PR, FI, TR, PDU, distances
  	planets[0]  = null;
  	planets[1]  = new Planet(this, "Earth",            1, 1, 240, 6, 0.683f, 15,  4,  6,  distance[1]);
  	planets[2]  = new Planet(this, "Crab",             2, 2, 240, 6, 0.666f, 15,  3,  6,  distance[2]);
  	planets[3]  = new Planet(this, "Eastside",         3, 3, 240, 6, 0.833f, 15, 10,  6,  distance[3]);
  	planets[4]  = new Planet(this, "Nameless",         4, 4, 240, 6, 0.650f, 15,  3,  6,  distance[4]);
  	planets[5]  = new Planet(this, "Lenin",            5, 5, 240, 6, 0.616f, 15,  1,  6,  distance[5]);
  	planets[6]  = new Planet(this, "Shadow",           6, 6, 240, 6, 0.700f, 15,  5,  6,  distance[6]);
  	planets[7]  = new Planet(this, "Sombrero",         7, 7, 240, 6, 0.816f, 15, 10,  6,  distance[7]);
  	planets[8]  = new Planet(this, "Lone Star",        8, 8, 240, 6, 0.716f, 15,  5,  6,  distance[8]);
  	planets[9]  = new Planet(this, "Barnard's Arrow",  9, 1, 240, 6, 0.500f, 10,  9,  7,  distance[9]);
  	planets[10] = new Planet(this, "Outpost",         10, 2, 110, 3, 1.083f, 10,  6, 10, distance[10]);
  	planets[11] = new Planet(this, "Desert Rock",     11, 3, 180, 5, 0.666f, 10,  7,  6, distance[11]);
  	planets[12] = new Planet(this, "Mechanica",       12, 4, 180, 2, 1.500f, 10,  5, 11, distance[12]);
  	planets[13] = new Planet(this, "Last Hope",       13, 5, 205, 6, 0.583f, 10,  1, 12, distance[13]);
  	planets[14] = new Planet(this, "Wilderness",      14, 6,  90, 3, 1.333f, 10,  6,  8, distance[14]);
  	planets[15] = new Planet(this, "Tramp",           15, 7, 160, 4, 0.750f, 10,  8,  6, distance[15]);
  	planets[16] = new Planet(this, "New Nome",        16, 8, 120, 3, 1.000f, 10,  5,  9, distance[16]);
  	planets[17] = new Planet(this, "Kalgourlie",      17, 0, 160, 4, 1.000f,  6,  3,  4, distance[17]);
  	planets[18] = new Planet(this, "Draken",          18, 0, 180, 5, 0.833f,  6,  3,  5, distance[18]);
  	planets[19] = new Planet(this, "Rivet",           19, 0, 220, 6, 0.666f,  6,  3,  6, distance[19]);
  	planets[20] = new Planet(this, "Crossland",       20, 0, 230, 6, 1.000f,  6,  3,  6, distance[20]);
  	planets[21] = new Planet(this, "Beyond",          21, 0,  80, 2, 0.666f,  6,  1,  2, distance[21]);
  	planets[22] = new Planet(this, "New Earth",       22, 0, 110, 3, 1.000f,  6,  2,  3, distance[22]);
  	planets[23] = new Planet(this, "Scott's Home",    23, 0, 150, 4, 0.833f,  6,  3,  4, distance[23]);
  	planets[24] = new Planet(this, "Newton",          24, 0, 120, 3, 1.166f,  6,  3,  3, distance[24]);
  	planets[25] = new Planet(this, "Murphy",          25, 0,  85, 3, 1.333f,  6,  1,  3, distance[25]);
  	planets[26] = new Planet(this, "Aitchison",       26, 0, 100, 3, 0.583f,  6,  2,  3, distance[26]);
  	planets[27] = new Planet(this, "Landfall",        27, 0,  90, 3, 1.083f,  6,  1,  3, distance[27]);
  	planets[28] = new Planet(this, "Atlas",           28, 0,  80, 2, 0.833f,  6,  1,  2, distance[28]);
  	planets[29] = new Planet(this, "New Mecca",       29, 0,  80, 2, 1.000f,  6,  1,  2, distance[29]);
  	planets[30] = new Planet(this, "Evergreen",       30, 0, 110, 3, 1.166f,  6,  2,  3, distance[30]);
  	planets[31] = new Planet(this, "New Jerusalem",   31, 0, 100, 3, 0.916f,  6,  2,  3, distance[31]);
  	planets[32] = new Planet(this, "Lesser Evil",     32, 0, 160, 4, 1.083f,  6,  3,  4, distance[32]);
  	planets[33] = new Planet(this, "Lermontov",       33, 0,  90, 3, 1.250f,  6,  1,  3, distance[33]);
  	planets[34] = new Planet(this, "Einstein",        34, 0, 220, 6, 1.250f,  6,  3,  6, distance[34]);
  	planets[35] = new Planet(this, "Dunroamin",       35, 0,  80, 5, 0.750f,  6,  3,  5, distance[35]);
  	planets[36] = new Planet(this, "Strife",          36, 0, 140, 4, 1.166f,  6,  3,  4, distance[36]);
  	planets[37] = new Planet(this, "Potter's Bar",    37, 0,  20, 1, 0.500f,  2,  0,  1, distance[37]);
  	planets[38] = new Planet(this, "Kaironow",        38, 0,  20, 1, 0.500f,  2,  0,  1, distance[38]);
  	planets[39] = new Planet(this, "Stormbringer",    39, 0,  20, 1, 0.500f,  2,  0,  1, distance[39]);
  	planets[40] = new Planet(this, "Mike's Dream",    40, 0,  20, 1, 0.500f,  2,  0,  1, distance[40]);
  }

  /**
   * Game number.
   */
  public int getNr() {
  	return nr;
  }

  /**
   * Planet with given number.
   */
  public Planet getPlanet(int nr) {
  	if ((nr < 0) || (nr > planets.length)) {
  	  return null;
  	} else {
  	  return planets[nr];
  	}
  }

  /**
   * Number of planet with given name
   * or -1 if no planet with that name.
   */
  public int getPlanetNr(String planetName) {
  	for (int i = 1; i < planets.length; i++) {
  	  if (planets[i].getName().equals(planetName)) { return i; }
  	}
  	return -1;
  }

  /**
   * Player with given number
   */
  public Player getPlayer(int nr) {
  	if ((nr < 0) || (nr > players.length)) {
  	  return null;
  	} else {
  	  return players[nr];
  	}
  }

  /**
   * Number of player with given name
   * or -1 if no player with that name.
   */
  public int getPlayerNr(String playerName) {
  	for (int i = 1; i < players.length; i++) {
  	  if (players[i].getName().equals(playerName)) { return i; }
  	}
  	return -1;
  }

  /**
   * Number of player with given username
   * or -1 if no player with that name.
   */
  public int getPlayerNrOfUser(String userName) {
  	for (int i = 1; i < players.length; i++) {
  	  if (players[i].getUser().equals(userName)) { return i; }
  	}
  	return -1;
  }

  /**
   * Turn number.
   */
  public int getTurn() {
  	return turn;
  }

  /**
   * Loads (deserializes) game with the given number.
   * @return the loaded game object or null in case of any errors
   */
  public static Game load(String directory, int nr) {
  	Log log = LogFactory.getLog(Game.class);
    ObjectInputStream in = null;
  
  	try {
  	  log.info("loading " + directory + "/game" + nr + ".obj");
  	  in = new ObjectInputStream(new FileInputStream(directory + "/game" + nr + ".obj"));
  	  Game result = (Game)in.readObject();
  	  log.info("loading successful");
  	  return result;
  	} catch(Exception e) {
      log.error("unable to load game object", e);
  	  return null;
  	} finally {
  	  if (in != null) {
  		  try { in.close(); } catch (IOException ie) { }
  		}
  	}
  
  }

  /**
   * Play a full turn for this game with the given commands.
   */
  public void nextTurn(CommandList cmdList) {
  	Command cmd = null;
  
  	for (int i = 0; i < NR_PLAYERS; i++) {
  	  getPlayer(i).phase(0, cmdList);
  	}
  
  	Iterator iterator = cmdList.iterator();
  	while (iterator.hasNext()) {
  	  cmd = (Command)iterator.next();
      if (cmd.getToken() == Parser.MESSAGE) {
			  String header = "\n" + getPlayer(cmd.getPlayer()).getName() + " sends:\n";
		 		int dest = cmd.getDestination();
		  	if (dest > 0)  {
					getPlayer(dest).getReport().add(Report.MESSAGES, header + cmd.getText());
			  } else {
					Report.addAll(Report.MESSAGES, header + cmd.getText());
			  }
  		}
  	}
  
  	for (int i = 0; i < 2; i++) {
  	  for (int j = 1; j < NR_PLANETS; j++) { getPlanet(j).phase(i, cmdList); }
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
  	turn++;
  }
  
}