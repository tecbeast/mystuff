package com.balancedbytes.game.ashes.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.TurnSecretGenerator;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.db.IDataObject;

/**
 * Control class for each individual game.
 * Management on a turn by turn basis.
 */
public class Game implements IDataObject {
	
	public static final int NR_OF_PLAYERS = 8;
	public static final int NR_OF_PLANETS = 40;

  	private static final Random RANDOM = new SecureRandom();

  	// planetary distances (in D+x)
  	private static final int[][] DISTANCES = {
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
  	
  	private long fId;
  	private boolean fModified;
  	
  	private int fGameNr;
  	private int fTurn;
  	private Date fLastUpdate;
  	private PlanetList fPlanets;
  	private PlayerList fPlayers;

  	public Game() {
	  	setPlayers(new PlayerList());
		setPlanets(new PlanetList());
  	}
  	
	/**
	 * Start a new Game with given planets.
	 */
	public Game(int number, String[] users) {

		this();
		
		setGameNr(number);
	  	setTurn(1);

		for (int i = 0; i < Math.min(NR_OF_PLAYERS, users.length); i++) {
			fPlayers.add(new Player(users[i], i + 1));
		}
	  	
	  	// planet name, planet number, player, WF, HD, PR, FI, TR, PDU
  		fPlanets.add(new Planet("Earth",            1, 1, 240, 6,  683, 15,  4,  6));
  		fPlanets.add(new Planet("Crab",             2, 2, 240, 6,  666, 15,  3,  6));
  		fPlanets.add(new Planet("Eastside",         3, 3, 240, 6,  833, 15, 10,  6));
  		fPlanets.add(new Planet("Nameless",         4, 4, 240, 6,  650, 15,  3,  6));
  		fPlanets.add(new Planet("Lenin",            5, 5, 240, 6,  616, 15,  1,  6));
  		fPlanets.add(new Planet("Shadow",           6, 6, 240, 6,  700, 15,  5,  6));
  		fPlanets.add(new Planet("Sombrero",         7, 7, 240, 6,  816, 15, 10,  6));
  		fPlanets.add(new Planet("Lone Star",        8, 8, 240, 6,  716, 15,  5,  6));
  		fPlanets.add(new Planet("Barnard's Arrow",  9, 1, 240, 6,  500, 10,  9,  7));
  		fPlanets.add(new Planet("Outpost",         10, 2, 110, 3, 1083, 10,  6, 10));
  		fPlanets.add(new Planet("Desert Rock",     11, 3, 180, 5,  666, 10,  7,  6));
  		fPlanets.add(new Planet("Mechanica",       12, 4, 180, 2,  500, 10,  5, 11));
  		fPlanets.add(new Planet("Last Hope",       13, 5, 205, 6,  583, 10,  1, 12));
  		fPlanets.add(new Planet("Wilderness",      14, 6,  90, 3,  333, 10,  6,  8));
  		fPlanets.add(new Planet("Tramp",           15, 7, 160, 4,  750, 10,  8,  6));
  		fPlanets.add(new Planet("New Nome",        16, 8, 120, 3, 1000, 10,  5,  9));
  		fPlanets.add(new Planet("Kalgourlie",      17, 0, 160, 4, 1000,  6,  3,  4));
  		fPlanets.add(new Planet("Draken",          18, 0, 180, 5,  833,  6,  3,  5));
  		fPlanets.add(new Planet("Rivet",           19, 0, 220, 6,  666,  6,  3,  6));
  		fPlanets.add(new Planet("Crossland",       20, 0, 230, 6, 1000,  6,  3,  6));
  		fPlanets.add(new Planet("Beyond",          21, 0,  80, 2,  666,  6,  1,  2));
  		fPlanets.add(new Planet("New Earth",       22, 0, 110, 3, 1000,  6,  2,  3));
  		fPlanets.add(new Planet("Scott's Home",    23, 0, 150, 4,  833,  6,  3,  4));
  		fPlanets.add(new Planet("Newton",          24, 0, 120, 3, 1166,  6,  3,  3));
  		fPlanets.add(new Planet("Murphy",          25, 0,  85, 3, 1333,  6,  1,  3));
  		fPlanets.add(new Planet("Aitchison",       26, 0, 100, 3,  583,  6,  2,  3));
  		fPlanets.add(new Planet("Landfall",        27, 0,  90, 3, 1083,  6,  1,  3));
  		fPlanets.add(new Planet("Atlas",           28, 0,  80, 2,  833,  6,  1,  2));
  		fPlanets.add(new Planet("New Mecca",       29, 0,  80, 2, 1000,  6,  1,  2));
  		fPlanets.add(new Planet("Evergreen",       30, 0, 110, 3, 1166,  6,  2,  3));
  		fPlanets.add(new Planet("New Jerusalem",   31, 0, 100, 3,  916,  6,  2,  3));
  		fPlanets.add(new Planet("Lesser Evil",     32, 0, 160, 4, 1083,  6,  3,  4));
  		fPlanets.add(new Planet("Lermontov",       33, 0,  90, 3, 1250,  6,  1,  3));
  		fPlanets.add(new Planet("Einstein",        34, 0, 220, 6, 1250,  6,  3,  6));
  		fPlanets.add(new Planet("Dunroamin",       35, 0,  80, 5,  750,  6,  3,  5));
  		fPlanets.add(new Planet("Strife",          36, 0, 140, 4, 1166,  6,  3,  4));
  		fPlanets.add(new Planet("Potter's Bar",    37, 0,  20, 1,  500,  2,  0,  1));
  		fPlanets.add(new Planet("Kaironow",        38, 0,  20, 1,  500,  2,  0,  1));
  		fPlanets.add(new Planet("Stormbringer",    39, 0,  20, 1,  500,  2,  0,  1));
  		fPlanets.add(new Planet("Mike's Dream",    40, 0,  20, 1,  500,  2,  0,  1));
  		
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

	/**
	 * 
	 */
	public static int getDistance(int fromPlanetNr, int toPlanetNr) {
		if ((fromPlanetNr < 1) || (fromPlanetNr > 40) || (toPlanetNr < 1) || (toPlanetNr > 40)) {
			return -1;
		}
		return DISTANCES[fromPlanetNr - 1][toPlanetNr - 1];
	}	

	public int getGameNr() {
		return fGameNr;
	}
	
	public void setGameNr(int number) {
		fGameNr = number;
	}
	
	public PlayerList getPlayers() {
		return fPlayers;
	}
	
	public void setPlayers(PlayerList players) {
		fPlayers = players;
	}	
	
	public PlanetList getPlanets() {
		return fPlanets;
	}
	
	public void setPlanets(PlanetList planets) {
		fPlanets = planets;
	}

	/**
	 * Planet with given number.
	 */
	public Planet getPlanet(int nr) {
		return fPlanets.get(nr - 1);
	}

	/**
	 * Player with given number
	 */
	public Player getPlayer(int nr) {
		return fPlayers.get(nr - 1);
	}

	/**
	 * Number of player with given username or 0 if no player with that name.
	 */
	public int getPlayerNrOfUser(String userName) {
		for (Player player : fPlayers) {
			if (player.getUserName().equals(userName)) {
				return player.getPlayerNr();
			}
		}
		return 0;
	}

	public int getTurn() {
		return fTurn;
	}
	
	public void setTurn(int turn) {
		fTurn = turn;
	}
	
	public Date getLastUpdate() {
		return fLastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		fLastUpdate = lastUpdate;
	}
	
	/**
	 * 
	 */
	public void addMessageToAllPlayerReports(Message message) {
		for (Player player : fPlayers) {
			player.getReport().add(message);
		}
	}
	
	/**
	 * 
	 */
	public double randomDouble() {
		return RANDOM.nextDouble();
	}

	/**
	 * Play a full turn for this game with the given commands.
	 */
	public void playTurn() {
		
		CommandList allCommands = new CommandList();
		PlayerMoveCache moveCache = AshesOfEmpire.getInstance().getMoveCache(); 
		
		//  7.1 Zuerst werden die neuen PV berechnet. Änderungen werden sofort wirksam,
		//      noch vor den Flugbewegungen. Das gilt auch für den Wechsel des Heimatplaneten
		//      und alle Namensänderungen.
		
		// execute player commands
		// (declare, homeplanet, planetname, playername, spy)
		for (Player player : fPlayers) {
			PlayerMove move = moveCache.get(getGameNr(), player.getPlayerNr(), getTurn());
			CommandList commands = (move != null) ? move.getCommands() : null;
			if (commands != null) {
				allCommands.add(commands);
				player.executeCommands(this, commands);
			}
		}		
		
		// turn start for each player
		// (update and report political terms, update pp, update fm and tm)
		for (Player player : fPlayers) {
			player.startTurn(this);
		}
		
		// play turn for each planet
		for (Planet planet : fPlanets) {
			planet.playTurn(this, allCommands);
		}		
  
		// turn end for each player
		// (calculate gnp totals, report player status)
		for (Player player: fPlayers) {
			player.endTurn(this);
		}
  
		//  7.10 Zum Abschluß wird die GNP-Tabelle berechnet.

		reportGnpTable();
		
		//  7.11 Prüfung, ob Siegbedingung erfüllt ist;
		//       falls ja, Ausdruck der Statistik für die HoF.

		// 2 Siegbedingung

		//   Ein Spieler scheidet aus, wenn er den letzten Planeten und TR verloren hat
		// (nur mit FI kann man keinen Planeten erobern). Von den MS, die das Ende der
		// Partie erleben, gewinnt der mit dem besten GNP. Die Partie endet vorzeitig,
		// wenn ein Imperium eine bestimmte Anzahl Planeten erobert hat.

		// 2.1 GNP-Sieg

		//   Bei Partiebeginn stimmen die acht MS über die Rundendauer der Partie ab,
		// indem jeder mit seinen ersten Zugbefehlen eine Rundenzahl zwischen 10 und 30
		// angibt. Der SL ermittelt daraus den ganzzahligen Durchschnittswert, der das
		// Spielende bestimmt.
		//   Die Auswertung jeder Runde enthält eine Tabelle, in der die verschiedenen
		// Besitztümer jedes MS bewertet sind. Aus den verschiedenen Einzelposten wird
		// das Bruttosozialprodukt (gross national product, GNP) ermittelt und in eine
		// Rangfolge gebracht - je kleiner der Wert, desto besser der Rang. Endet eine
		// Partie durch Erreichen dern Rundenzahl, gewinnt der MS mit dem besten GNP.

		// 2.2 Planeten-Sieg

		//   Eine Partie ist sofort beendet, wenn es einem MS gelingt, 13 Planeten zu
		// erobern und eine Runde zu halten oder mehr als 13 Planeten zu erobern. Ein
		// solcher Planeten-Sieg rangiert immer vor einem GNP-Sieg.
		
		// TODO ...

	  	/*
	  	Session session = Session.getDefaultInstance(AshesOfEmpire.getProperties(), null);
	  	for (int i = 0; i < NR_PLAYERS; i++) {
	  	  Report report = players[i].getReport();
	  	  String subject =  "Ashes Of Empire Game " + nr + " Turn " + turn + " Player " +  i;
	  	  getPlayer(i).mailTo(session, subject, report.toString());
	  	}
	  	*/
		
		// increase turn
		fTurn += 1;
		
		// generate turn secrets for next turn
		for (Player player : fPlayers) {
			PlayerMove move = new PlayerMove();
			move.setGameNr(getGameNr());
			move.setPlayerNr(player.getPlayerNr());
			move.setTurn(getTurn());
			move.setTurnSecret(TurnSecretGenerator.generateSecret());
			move.setModified(true);
			moveCache.add(move);
		}
		
		// mark this game as modified (so it will be saved by the cache)
		setModified(true);

	}
	
	//	4.5 GNP-Tabelle
	//	  Die GNP-Tabelle (gross national product) zeigt die aktuelle Wertung aller
	//	acht MS. Wenn eine Partie durch Erreichen der gewählten Rundenzahl beendet
	//	wird, gewinnt, wer auf Platz 1 der GNP-Tabelle steht. Die Qualität seines
	//	Sieges wird durch den GNP-Wert beschrieben. Mit diesem Wert gehen alle
	//	Mitspieler auch in die "Ashes Hall of Fame" ein. Die entsprechende Meldung
	//	gibt der SL nach Abschluß der Partie ab.
	//	  	Das GNP wird aus zwölf Einzelwerten ermittelt: die Anzahl der kontrollierten
	//	Planten (PL), die Anzahl an FP, OP, RP, FY, TZ, PDU, die Vorräte an FUEL, ORE
	//	und RARE summiert zu SP (stock piles), die Anzahl an FI und TR, die Höhe des
	//	GIP und die Anzahl politischer Punkte (PP). Für jede der zwölf Kategorien
	//	getrennt werden die Werte der MS in eine Rangfolge gebracht und mit
	//	Kennziffern beschrieben, das heißt, der Spieler mit den meisten Planeten
	//	bekommt für PL eine 1, der mit den wenigsten eine 8. Haben zwei oder mehr MS
	//	gleiche Werte, teilen sie sich den entsprechenden Rang.
	//	  Aus den Einzelrängen in den zwölf Kategorien wird ein Durchschnitt
	//	errechnet. Dieser Durchschnitt ist das GNP. Das besterreichbare GNP ist 1, was
	//	bedeutet, daß der betreffende Spieler in allen zwölf Wertungen auf dem ersten
	//	Platz liegt, also die meisten Planeten kontrolliert, die meisten FP, OP, RP,
	//	FY, TY, PDU, SP, FI, TR, PP und das höchste GIP besitzt. Ein Spieler der in
	//	elf Positionen Platz 1 belegt und in einer Platz 3 hat ein GNP von (14/12=)
	//	1,16.
	//	  Die GNP-Tabelle ist nützlich, um die Stärke der Gegner einzuschätzen. Wenn
	//	das Imperium "A" bei FI  Platz 4 belegt und "B" Platz 5, dann weiß "A" mit 416
	//	FI, daß "B" höchstens 415 FI besitzen kann.

	private void reportGnpTable() {
		
		List<Map<Category, Integer>> playerGnpScores = new ArrayList<Map<Category,Integer>>();
		
		List<Map<Category, Integer>> playerGnpTotals = new ArrayList<Map<Category,Integer>>();
		for (int i = 1; i <= 8; i++) {
			playerGnpTotals.add(getPlayer(i).getGnpTotals());
			playerGnpScores.add(Category.buildEmptyMap());
		}
		
		for (Category category : Category.values()) {
			List<Integer> values = new ArrayList<Integer>();
			for (Map<Category, Integer> playerGnpTotal : playerGnpTotals) {
				values.add(playerGnpTotal.get(category));
			}
			values = sortHighestFirstRemoveDuplicates(values);
			for (int i = 0; i < playerGnpTotals.size(); i++) {
				int total = playerGnpTotals.get(i).get(category); 
				for (int j = 0; j < values.size(); j++) {
					if (total == values.get(j)) {
						playerGnpScores.get(i).put(category, j + 1);
					}
				}
			}
		}
		
		Message message = new Message(Topic.GROSS_NATIONAL_PRODUCT);

		StringBuilder line = new StringBuilder();
		line.append("Player ");
		for (int i = 1; i <= NR_OF_PLAYERS; i++) {
			line.append("  (").append(i).append(")  ");
		}
		message.add(line.toString());

		addGnpLine(message, "    PL ", Category.PLANETS, playerGnpScores);
		addGnpLine(message, "    FP ", Category.FUEL_PLANTS, playerGnpScores);
		addGnpLine(message, "    OP ", Category.ORE_PLANTS, playerGnpScores);
		addGnpLine(message, "    RP ", Category.RARE_PLANTS, playerGnpScores);
		addGnpLine(message, "    FY ", Category.FIGHTER_YARDS, playerGnpScores);
		addGnpLine(message, "    TY ", Category.TRANSPORTER_YARDS, playerGnpScores);
		addGnpLine(message, "   PDU ", Category.PLANETARY_DEFENSE_UNITS, playerGnpScores);
		addGnpLine(message, "    SP ", Category.STOCK_PILES, playerGnpScores);
		addGnpLine(message, "    FI ", Category.FIGHTERS, playerGnpScores);
		addGnpLine(message, "    TR ", Category.TRANSPORTERS, playerGnpScores);
		addGnpLine(message, "   GIP ", Category.GROSS_INDUSTRIAL_PRODUCT, playerGnpScores);
		addGnpLine(message, "    PP ", Category.POLITICAL_POINTS, playerGnpScores);
		
		line = new StringBuilder();
		line.append(" Total ");
		for (Map<Category,Integer> playerGnpScore : playerGnpScores) {
			int total = 0;
			for (Category category : playerGnpScore.keySet()) {
				total += playerGnpScore.get(category);
			}
			line.append(" " + String.format("%.3f%n", (double) total / 12) + " ");
		}
		message.add(line.toString());
		
		addMessageToAllPlayerReports(message);
		
	}
	
	private void addGnpLine(
		Message message,
		String title,
		Category category,
		List<Map<Category, Integer>> playerGnpScores
	) {
		StringBuilder line = new StringBuilder();
		line.append(title);
		for (int i = 0; i < playerGnpScores.size(); i++) {
			line.append(" ").append(playerGnpScores.get(i).get(category)).append(" ");
		}
		message.add(line.toString());
	}
	
	private List<Integer> sortHighestFirstRemoveDuplicates(List<Integer> values) {
		List<Integer> result = new ArrayList<Integer>();
		Collections.sort(values, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2) {
					return -1;
				} else if (o2 > o1) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		int lastValue = 0;
		for (int i = 0; i < values.size(); i++) {
			if ((i == 0) || (lastValue != values.get(i))) {
				lastValue = values.get(i);
				result.add(lastValue);
			}
		}
		return result;
	}
	
}