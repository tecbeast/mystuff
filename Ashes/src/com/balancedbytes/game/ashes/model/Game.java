package com.balancedbytes.game.ashes.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.balancedbytes.game.ashes.TurnSecretGenerator;
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
  	
  	private static final Random RANDOM = new SecureRandom();
  
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
	  	fTurn = 0;
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
	public void playTurn(CommandList cmdList) {
		
		//  7.1 Zuerst werden die neuen PV berechnet. Änderungen werden sofort wirksam,
		//      noch vor den Flugbewegungen. Das gilt auch für den Wechsel des Heimatplaneten
		//      und alle Namensänderungen.
		
		// increase turn
		fTurn += 1;
		
		// generate turn secrets for next turn
		for (int i = 1; i <= 8; i++) {
			getPlayer(i).setTurnSecret(TurnSecretGenerator.generateSecret());
		}		
		
		// execute player commands
		// (declare, homeplanet, planetname, playername, spy)
		for (int i = 1; i <= 8; i++) {
			getPlayer(i).executeCommands(this, cmdList);
		}		
		
		// turn start for each player
		// (update and report political terms, update pp, update fm and tm)
		for (int i = 1; i <= 8; i++) {
			getPlayer(i).startTurn(this);
		}
		
		// play turn for each planet
		for (int i = 1; i <= 40; i++) {
			getPlanet(i).playTurn(this, cmdList);
		}		
  
		// turn end for each player
		// (calculate gnp totals, report player status)
		for (int i = 1; i <= 8; i++) {
			getPlayer(i).endTurn(this);
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
		
		Message message = new Message(Topic.GNP);

		StringBuilder line = new StringBuilder();
		line.append("Player ");
		for (int i = 1; i <= 8; i++) {
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