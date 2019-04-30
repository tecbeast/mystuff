package com.balancedbytes.game.ashes;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.balancedbytes.game.ashes.parser.Parser;

/**
 * Central object for Ashes: handles building, space travel and fights.
 */
public class Planet {

  private final static int QUEUESIZE = 6;

  private String name      = null;  // planet name
  private int    number    = 0;     // planet number
  private int    wf        = 0;     // workforce
  private int    currentWf = 0;     // current workforce
  private int    hd        = 0;     // home defense
  private float  pr        = 0.0f;  // production rate
  private int    pdu       = 0;     // planetary defense units

  private int fy = 0;         // fighter yards
  private int currentFy = 0;  // current fighter yards
  private int ty = 0;         // transporter yards
  private int currentTy = 0;  // current transporter yards

  private int fp = 0;  // fuel plants
  private int op = 0;  // ore plants
  private int rp = 0;  // rare plants

  private int fuel = 0;  // fuel resources
  private int ore  = 0;  // ore resources
  private int rare = 0;  // rare resources

  private float pm  = 0.0f;  // planetary morale
  private int   sp  = 0;     // stock piles
  private int   gip = 0;     // gross industrial product

  private int[] distances = null;   // distance to other planets
  private int[] planetsInD = null;  // planets reachable in one hop

  private Game game = null;
  private Player player = null;

  private FleetSet[] flightQueue;
  
  private static final Logger LOG = LogManager.getLogger(Planet.class);

  /**
   * Create a Planet with the given default settings.
   */
  public Planet(Game game, String name, int nr, int playerNr, int wf, int hd, float pr, int fi, int tr, int pdu, int[] distances) {
  
  	this.game      = game;
  	this.name      = name;
  	this.number    = nr;
  	this.player    = game.getPlayer(playerNr);
  	this.wf        = wf;
  	this.hd        = hd;
  	this.pr        = pr;
  	this.pdu       = pdu;
  	this.distances = distances;
  		
  	flightQueue = new FleetSet[QUEUESIZE];
  	for (int i = 0; i < QUEUESIZE; i++) {  flightQueue[i] = new FleetSet(); }
  
  	flightQueue[0].add(new Fleet(player, fi, tr));
  
  	int cnt = 0;
  	for (int i = 0; i < distances.length; i++) {
  	  if (distances[i] == 0) { cnt++; }
  	}
  	planetsInD = new int[cnt];
  	cnt = 0;
  	for (int i = 0; i < distances.length; i++) {
  	  if (distances[i] == 0) { planetsInD[cnt++] = i; }
  	}
  
  	if (playerNr == number) {
  	  pm = 1.50f;
  	} else {
  	  pm = 1.00f;
  	}
  
  	fp = 3; op = 3; rp = 3;  fy = 6; ty = 3;
  	fuel = 30; ore = 30; rare = 30;
  
  	sp  = fuel + ore + rare;
  	gip = (int)(fp + op + rp + (int)(wf * pr / 20) + (sp / 10));
  
  }

  /**
   *
   */
  private void battle() {
  	StringBuffer buffer = new StringBuffer();
  
  	// calculate attack strength and defense strength
  	int af = 0, df = 0, as = 0, ds = 0;
  	FleetSet attacker = new FleetSet(), defender = new FleetSet();
  	FleetSet involved = flightQueue[0];
  
  	Iterator iterator = involved.iterator();
  	while (iterator.hasNext()) {
  	  Fleet incomingFleet = (Fleet)iterator.next();
  	 	Player incomingPlayer = incomingFleet.getPlayer();
  	  if (incomingPlayer == player) {
  			defender.add(incomingFleet);
  			df += incomingFleet.getFighter();
  			ds += (int)(incomingFleet.getFighter() * player.getFighterMorale() * pm);
  	  } else {
  			int terms = incomingPlayer.getPoliticalTerms(player.getNumber());
  			if (terms != Parser.NEUTRAL) {
  		 		if (terms == Parser.PEACE) {
  					defender.add(incomingFleet);
  					df += incomingFleet.getFighter();
  					ds += (int)(incomingFleet.getFighter() * incomingPlayer.getFighterMorale());
  			  } else {
  					attacker.add(incomingFleet);
  					af += incomingFleet.getFighter();
  					as += (int)(incomingFleet.getFighter() * incomingPlayer.getFighterMorale());
  			  }
  			}
  	  }
  	}
  
    if (as > 0) { LOG.info("Battle at " + number + ":  AS " + as + " DS " + ds); }
  
  	int[] atkWin = new int[attacker.size()], atkLoss = new int[attacker.size()];
  	int atkBonus = 0;
  	double ad = 2.5;  // superior if there is no fight
  
  	// is there a fight ?
  	if (as > 0) {
  
  	  buffer.append("\nBattle at " + name + " (" + number + ") Owner: " + player.getName() + "\n");
  
  	  if (ds > 0) {
  			int[] defWin = new int[defender.size()], defLoss = new int[defender.size()];
  			int defBonus = 0;
  			if (as > ds) { ad = as / ds; } else { ad = ds / as; }
  			if (ad > 2.5) { ad = 2.5; }
  			for (int i = 0; i < attacker.size(); i++) {
  		 		Fleet atkFleet = attacker.get(i);
  			  Player atkPlayer = atkFleet.getPlayer();
  			  for (int j = 0; j < defender.size(); j++) {
  					Fleet defFleet = defender.get(j);
  					Player defPlayer = defFleet.getPlayer();
  					int defKills = (int)(((defFleet.getFighter() * defPlayer.getFighterMorale() / ad) * (atkFleet.getFighter() / af) + 1) / 2);
  					int atkKills = (int)(((atkFleet.getFighter() * atkPlayer.getFighterMorale() / ad) * (defFleet.getFighter() / df) + 1) / 2);
  
  					LOG.info("atkFighter " + atkFleet.getFighter() + " atkMorale " + atkPlayer.getFighterMorale() + " af " + af);
  					LOG.info("defFighter " + defFleet.getFighter() + " defMorale " + defPlayer.getFighterMorale() + " df " + df);
  					LOG.info("ad " + ad + " defKills " + defKills + " atkKills " + atkKills);
  
  					defWin[j] += defKills; defLoss[j] += atkKills;
  					atkWin[i] += atkKills; atkLoss[i] += defKills;
  			  }
  			}
  			if (as > ds) { atkBonus = 1; } else { defBonus = 1; }
  			for (int i = 0; i < attacker.size(); i++) {
  			  Fleet atkFleet = attacker.get(i);
  			  Player atkPlayer = atkFleet.getPlayer();
  
  			  buffer.append(atkPlayer.getName() + " attacks with " + atkFleet.getFighter() + " FI (-");
  			  if (atkLoss[i] < atkFleet.getFighter()) { buffer.append(atkLoss[i]); } else { buffer.append(atkFleet.getFighter()); }
  			  buffer.append(" FI\n");
  
  			  atkPlayer.setFighterMorale(atkPlayer.getFighterMorale() + atkWin[i] - atkLoss[i] + atkBonus);
  			  atkFleet.setFighter(atkFleet.getFighter() - atkLoss[i]);
  			}
  			for (int j = 0; j < defender.size(); j++) {
  			  Fleet defFleet = defender.get(j);
  			  Player defPlayer = defFleet.getPlayer();
  
  			  buffer.append(defPlayer.getName() + " defends with " + defFleet.getFighter() + " FI (-");
  			  if (defLoss[j] < defFleet.getFighter()) { buffer.append(defLoss[j]); } else { buffer.append(defFleet.getFighter()); }
  		 		buffer.append(" FI\n");
  			  defPlayer.setFighterMorale(defPlayer.getFighterMorale() + defWin[j] - defLoss[j] + defBonus);
  			  defFleet.setFighter(defFleet.getFighter() - defLoss[j]);
  			}
  	  } else {
  			for (int i = 0; i < attacker.size(); i++) {
  			  Fleet atkFleet = attacker.get(i);
  			  Player atkPlayer = atkFleet.getPlayer();
  			  buffer.append(atkPlayer.getName() + " attacks with " + atkFleet.getFighter() + " FI\n");
  			}
  			buffer.append("no resistance\n");
  	  }
  	  if (as > ds) {
  			if (ds > 0) {
  			  buffer.append("The defenders flee to their homeplanet (-" + defender.getNrTransporter() + " TR)\n");
  			  defender.setTransporter(0); flee(defender);
  			}
  	 	} else {
  			buffer.append("The attackers flee to their homeplanet (-" + attacker.getNrTransporter() + " TR)\n");
  			attacker.setTransporter(0); flee(attacker);
  	  }
  	}
  
  	// landing attempts and revolution
  
  	int at = attacker.getNrTransporter();
  	 int pduLoss = 0;
  
  	if ((at > 0) && (((as > 0) && (as > ds)) || ((as == 0) && (ds == 0)))) {
  
  	  as = 0;
  	  ds = (int)(this.pdu * this.pm);
  	  for (int i = 0; i < attacker.size(); i++) {
  			Fleet atkFleet = attacker.get(i);
  			Player atkPlayer = atkFleet.getPlayer();
  			int defKills = (int)((ds * atkFleet.getTransporter() / at) + 0.5);
  			int strength = (int)(atkFleet.getTransporter() * atkPlayer.getTransporterMorale());
  			int atkKills = (int)((strength * ad) + 0.5);
  			as += strength;  atkWin[i] = atkKills; atkLoss[i] = defKills; pduLoss += atkKills;
  	  }
  	  as = (int)(as * ad);
  
  	  if (as > ds) { atkBonus = 1; } else { atkBonus = 0; }
  
  	  for (int i = 0; i < attacker.size(); i++) {
  		Fleet atkFleet = attacker.get(i);
  		Player atkPlayer = atkFleet.getPlayer();
  			if (atkFleet.getTransporter() > 0) {
  			  buffer.append(atkPlayer.getName() + " attempts landing with " + atkFleet.getTransporter() + " TR (-" + atkLoss[i] + " TR)\n");
  			  atkPlayer.setTransporterMorale(atkPlayer.getTransporterMorale() + atkWin[i] - atkLoss[i] + atkBonus);
  		 		atkFleet.setTransporter(atkFleet.getTransporter() - atkLoss[i]);
  			}
  	  }
  
  	  if (this.pdu < pduLoss) { pduLoss = this.pdu; }
  
  	  buffer.append("Planet defends with " + pdu + " PDU (-" + pduLoss + " PDU)\n");
  
  	  this.pdu -= pduLoss;
  
  	  // planet conquered
  	  if (as > ds) {
  
  			int trMax = 0, conquerer = 0;
  			float tmMax = 0.0f;
  			attacker.shuffle();
  			for (int i = 0; i < attacker.size(); i++) {
  			  int tr = attacker.get(i).getTransporter();
  			  float tm = attacker.get(i).getPlayer().getTransporterMorale();
  			  if (tr > trMax) {
  					trMax = tr;  tmMax = tm;  conquerer = i;
  			  } else if (tr == trMax) {
  					if (tm > tmMax) {
  				 		tmMax = tm;  conquerer = i;
  					}
  		 		}
  			}
  			if (trMax > 0) {
  			  Player newOwner = attacker.get(conquerer).getPlayer();
  			  changeOwner(newOwner);
  			  buffer.append(newOwner.getName() + " conquers the planet.\n");
  			}
  
  	  // revolution attempt
  	  } else {
  
  			buffer.append("Planet defends successfully.\n");
  
  			pduLoss = revolt();
  
  			if ((pdu > 0) && (pduLoss > 0)) {
  			  buffer.append("Riots destroy further " + pduLoss + " PDU\n");
  			} else if (pdu == 0) {
  			  buffer.append("The population takes advantage and revolts.\n");
  			}
  	  }
  	}
  
  	if (buffer.length() > 0) {
  
  	  Report report = player.getReport();
  	  report.add(Report.BATTLES, buffer);
  
  	  iterator = involved.iterator();
  	  while (iterator.hasNext()) {
  			Fleet fleet = (Fleet)iterator.next();
  			if (fleet.getPlayer() != player) {
  			  report = fleet.getPlayer().getReport();
  		 		report.add(Report.BATTLES, buffer);
  			}
  	  }
  
  	}
  
  }

  /**
   *
   */
  private boolean build(int type, int quantity) {
  
  	switch (type) {
  	  // buildType -> PDU | FP | OP | RP | FY | TY | FI | TR
  	  case Parser.PDU:
  			while ((quantity > 0) && (currentWf >= 15)) {
  			  LOG.info("build PDU");
  			  currentWf -= 15; ++pdu;  --quantity;
  			}
  			break;
  	  case Parser.FP:
  			while ((quantity > 0) && (currentWf >= 5)) {
  				LOG.info("build FP");
  			  currentWf -= 5;  ++fp;  --quantity;
  			}
  			break;
  	  case Parser.OP:
  			while ((quantity > 0) && (currentWf >= 4)) {
  		 		LOG.info("build OP");
  			  currentWf -= 4; ++op;  --quantity;
  			}
  			break;
  	  case Parser.RP:
  			while ((quantity > 0) && (currentWf >= 10)) {
  		 		LOG.info("build RP");
  			  currentWf -= 10; ++rp; --quantity;
  			}
  			break;
  	  case Parser.FY:
  			while ((quantity > 0) && (currentWf >= 15)) {
  		 		LOG.info("build FY");
  			  currentWf -= 15; ++fy; --quantity;
  			}
  			break;
  	 	case Parser.TY:
  			while ((quantity > 0) && (currentWf >= 20)) {
  		  	LOG.info("build TY");
  		  	currentWf -= 20; ++ty; --quantity;
  			}
  			break;
  	  case Parser.FI:
  			int fi = 0;
  			while ((quantity > 0) && (currentWf >= 10) && (fuel >= 1) && (ore >= 1) && (rare >= 3) && (currentFy >=1)) {
  		  	LOG.info("build FI");
  		  	currentWf -= 10; fuel -= 1; ore -= 1; rare -= 3; currentFy -= 1; ++fi; --quantity;
  			}
  			if (fi > 0) { flightQueue[0].add(new Fleet(player, fi, 0)); }
  			break;
  	  case Parser.TR:
  			int tr = 0;
  			while ((quantity > 0) && (currentWf >= 20) && (fuel >= 2) && (ore >= 3) && (rare >= 1) && (currentTy >= 1)) {
  		  	LOG.info("build TR");
  		  	currentWf -= 20; fuel -= 2; ore -= 3; rare -= 1; currentTy -= 1; ++tr; --quantity;
  			}
  			if (tr > 0) { flightQueue[0].add(new Fleet(player, 0, tr)); }
  			break;
  	}
  	if (quantity == 0) { return true; } else { return false; }
  }

  /**
   *
   */
  private void cargoReport() {
    /*
  	StringBuffer buffer = new StringBuffer();
  	int total = 0;
  
  	if ((flightQueue[0] != null) && flightQueue[0].hasCargo()) {
  	  buffer.append("(O) ");
  	  int[] cargo = flightQueue[0].getCargo();
  	  for (int i = 0; i < Fleet.CARGOTYPES; i++) {
  		if (cargo[i] > 0) { buffer.append(cargo[i] + " C" + i + " "); total += cargo[i]; }
  	  }
  	}
  	if ((flightQueue[1] != null) && flightQueue[1].hasCargo()) {
  	  buffer.append("(D) ");
  	  int[] cargo = flightQueue[1].getCargo();
  	  for (int i = 0; i < Fleet.CARGOTYPES; i++) {
  		if (cargo[i] > 0) { buffer.append(cargo[i] + " C" + i + " "); total += cargo[i]; }
  	  }
  	}
  	if ((flightQueue[2] != null) && flightQueue[2].hasCargo()) {
  	  buffer.append("(D+1) ");
  	  int[] cargo = flightQueue[2].getCargo();
  	  for (int i = 0; i < Fleet.CARGOTYPES; i++) {
  		if (cargo[i] > 0) { buffer.append(cargo[i] + " C" + i + " "); total += cargo[i]; }
  	  }
  	}
  	if ((flightQueue[3] != null) && flightQueue[3].hasCargo()) {
  	  buffer.append("(D+2) ");
  	  int[] cargo = flightQueue[3].getCargo();
  	  for (int i = 0; i < Fleet.CARGOTYPES; i++) {
  		if (cargo[i] > 0) { buffer.append(cargo[i] + " C" + i + " "); total += cargo[i]; }
  	  }
  	}
  	if ((flightQueue[4] != null) && flightQueue[4].hasCargo()) {
  	  buffer.append("(D+3) ");
  	  int[] cargo = flightQueue[4].getCargo();
  	  for (int i = 0; i < Fleet.CARGOTYPES; i++) {
  		if (cargo[i] > 0) { buffer.append(cargo[i] + " C" + i + " "); total += cargo[i]; }
  	  }
  	}
  	if (buffer.length() > 0) {
  	  buffer.insert(0, nr + " "); buffer.append('\n');
  	  for (int i = 1; i < game.NR_PLAYERS; i++) {
  		Report report = game.getPlayer(i).getReport();
  		if ((flightQueue[0].getFleet(i) != null) || (i == playerNr)) {
  		  report.add(Report.CARGO, buffer);
  		} else {
  		  report.add(Report.CARGO, total + "Cargoships approaching " + name + " (" + nr + ")\n");
  		}
  	  }
  	}
  	*/
  
  }

  /**
   *
   */
  private void changeOwner(Player newOwner) {
  	// planetary morale drops to 50% if planet was conquered
  	this.pm = 0.5f;
  	this.player = newOwner;
  }

  /**
   *
   */
  private void flee(FleetSet fleets) {
  	Iterator iterator = fleets.iterator();
  	while (iterator.hasNext()) {
  	  Fleet fleet = (Fleet)iterator.next();
  	  // cargo stays
  	  /*
  	  if (fleet.hasCargo()) {
  		Fleet temp = new Fleet(fleet.getPlayerNr(), fleet.getFighter(), fleet.getTransporter());
  		fleet.setFighter(0);
  		fleet.setTransporter(0);
  		fleet = temp;
  	  }
  	  */
  	  // fighter (and transporter) flee
  	  // this takes an extra turn for orientation (distance increased by one)
  	  if ((fleet.getFighter() > 0) || (fleet.getTransporter() > 0)) {
  			Planet destination = game.getPlanet(fleet.getPlayer().getHomePlanetNr());
  			if (destination.getNumber() > number) {
  		 		destination.receive(fleet, distances[destination.getNumber()]+2);
  			} else {
  		 		destination.receive(fleet, distances[destination.getNumber()]+1);
  			}
  	  }
  	  // this fleet no longer in flight queue
  	  flightQueue[0].remove(fleet);
  	}
  }

  /**
   *
   */
  private void fleetReport(int playerNr) {
  	StringBuffer buffer = new StringBuffer();
  	int limit = 0;
  
  	if (playerNr != player.getNumber()) { limit = game.getTurn() * 2 + 9; }
  
  	if (flightQueue[0].getNrShips() > limit) {
  	  buffer.append("(O) ");
  	  if (flightQueue[0].getNrFighter() > 0) { buffer.append(flightQueue[0].getNrFighter() + " FI "); }
  	  if (flightQueue[0].getNrTransporter() > 0) { buffer.append(flightQueue[0].getNrTransporter() + " TR "); }
  	}
  	if (flightQueue[1].getNrShips() > limit) {
  	  buffer.append("(D) ");
  	  if (flightQueue[1].getNrFighter() > 0) { buffer.append(flightQueue[1].getNrFighter() + " FI "); }
  	  if (flightQueue[1].getNrTransporter() > 0) { buffer.append(flightQueue[1].getNrTransporter() + " TR "); }
  	}
  	if (flightQueue[2].getNrShips() > limit) {
  	  buffer.append("(D+1) ");
  	  if (flightQueue[2].getNrFighter() > 0) { buffer.append(flightQueue[2].getNrFighter() + " FI "); }
  	  if (flightQueue[2].getNrTransporter() > 0) { buffer.append(flightQueue[2].getNrTransporter() + " TR "); }
  	}
  	if (flightQueue[3].getNrShips() > limit) {
  	  buffer.append("(D+2) ");
  	  if (flightQueue[3].getNrFighter() > 0) { buffer.append(flightQueue[3].getNrFighter() + " FI "); }
  	  if (flightQueue[3].getNrTransporter() > 0) { buffer.append(flightQueue[3].getNrTransporter() + " TR "); }
  	}
  	if (flightQueue[4].getNrShips() > limit) {
  	  buffer.append("(D+3) ");
  	  if (flightQueue[4].getNrFighter() > 0) { buffer.append(flightQueue[4].getNrFighter() + " FI "); }
  	  if (flightQueue[4].getNrTransporter() > 0) { buffer.append(flightQueue[4].getNrTransporter() + " TR "); }
  	}
  	if (buffer.length() > 0) {
  	  if (number < 10) { buffer.insert(0, " " + number + " "); } else { buffer.insert(0, number + " "); }
  	  buffer.append('\n');
  	  Report report = player.getReport();
  	  if (playerNr == player.getNumber()) {
  			report.add(Report.FLEETS, buffer);
  	  } else {
  			report.add(Report.INTELLIGENCE, buffer);
  	  }
  	}
  }

  /**
   *
   */
  public String getName() {
  	return this.name;
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
  public float getPlanetaryMorale() {
  	return this.pm;
  }

  /**
   *
   */
  public Player getPlayer() {
  	return player;
  }

  /**
   * Play a full turn for this planet with the given commands.
   * <pre>
   * Order of evaluation:
   *  1) new PT (political terms)
   *  2) load CS (cargos ships)
   *  3) movement
   * 4a) build commands
   * 4b) calculate new resources
   *  5) revolt (if PDU < HD or PP < 0)
   * 6a) produce resources
   * 6b) calculate PM%, GIP, WF and PR
   *  7) fights
   * 8a) landing attempts
   * 8b) revolution
   *  9) unload CS
   * 10) calculate GNP table
   * 11) test for victory
   * </pre>
   */
  public void phase(int phaseNr, CommandList cmdList) {
  	Iterator iterator = null;
  	Command cmd = null;
  
  	if (phaseNr > 0) {
  
  	  // fleet report to all friendly players and owner (who is friendly to himself)
  	  for (int i = 0; i < Game.NR_PLAYERS; i++) {
  			if (game.getPlayer(i).getPoliticalTerms(player.getNumber()) == Parser.PEACE) { fleetReport(i); }
  	  }
  
  	  // report approaching cargoships
  	  cargoReport();
  
  	} else {
  
  	  LOG.info("Processing Planet " + number);
  
  	  // cargo ships (unloading)
  
  	  int tr = 0;  // nr of transporter gained
  	  iterator = flightQueue[0].iterator();
      /*
  	  while (iterator.hasNext()) {
  			Fleet fleet = (Fleet)iterator.next();
  		 	if (fleet.hasCargo()) {
  
  		   if (DEBUG) { System.err.println(" unloading cargo"); }
  
  		   if (fleet.getCargo(0) > 0) { wf += fleet.getCargo(0) * 8; tr += fleet.getCargo(0); }
  		   if (fleet.getCargo(1) > 0) { pdu += fleet.getCargo(1); tr += fleet.getCargo(1);  }
  		   if (fleet.getCargo(2) > 0) { fp += fleet.getCargo(2) * 4; tr += fleet.getCargo(2); }
  		   if (fleet.getCargo(3) > 0) { op += fleet.getCargo(3) * 5; tr += fleet.getCargo(3); }
  		   if (fleet.getCargo(4) > 0) { rp += fleet.getCargo(4) * 2; tr += fleet.getCargo(4); }
  		   if (fleet.getCargo(5) > 0) { fuel += fleet.getCargo(5) * 40; tr += fleet.getCargo(5);  }
  		   if (fleet.getCargo(6) > 0) { ore += fleet.getCargo(6) * 50; tr += fleet.getCargo(6); }
  		   if (fleet.getCargo(7) > 0) { rare += fleet.getCargo(7) * 20; tr += fleet.getCargo(7); }
  		   if (fleet.getCargo(8) > 0) { fy += fleet.getCargo(8); tr += fleet.getCargo(8); }
  		   if (fleet.getCargo(9) > 0) { ty += fleet.getCargo(9); tr += fleet.getCargo(9); }
  		 	}
  	  }
      */
  	  if (tr > 0) {
  			flightQueue[0].add(new Fleet(player, 0, tr));
  	  }
  
  	  // space travel and cargo ships (loading this time)
  		iterator = cmdList.iterator();
  		while (iterator.hasNext()) {
  		  cmd = (Command)iterator.next();
  	 		if ((cmd.getSource() == number) && (cmd.getPlayer() == player.getNumber())) {
  				switch (cmd.getToken()) {
  		  		case Parser.SEND:
  			 			send(cmd.getDestination(), cmd.getType(), cmd.getNumber());
  						break;
  			  	case Parser.HOMEPLANET:
  						Planet homePlanet = game.getPlanet(cmd.getSource());
  						if (homePlanet.getPlayer() == player) {
  				 			player.setHomePlanetNr(homePlanet.getNumber());
  						}
  						break;
  			  	case Parser.PLANETNAME:
  						name = cmd.getText();
  						break;
  				}
  		  }
  		}
  
  	  // current workforce (for building) equals base workforce etc.
  	  currentWf = wf;  currentFy = fy;  currentTy = ty;
  
  	  // building and resources
  		iterator = cmdList.iterator();
  		while (iterator.hasNext()) {
  	  	cmd = (Command)iterator.next();
  	  	if ((cmd.getToken() == Parser.BUILD) && (cmd.getSource() == number) && (cmd.getPlayer() == player.getNumber())) {
  				build(cmd.getType(), cmd.getNumber());
  		  }
  		}
  
  	  // fleets in flightQueue approach
  	  flightQueue[0].add(flightQueue[1]);
  	  for (int i = 1; i < flightQueue.length-1; i++) { flightQueue[i] = flightQueue[i+1]; }
  	  flightQueue[flightQueue.length-1] = new FleetSet();
  
  	  // test for revolt
  
  	  if ((pdu < hd) || (player.getPoliticalPoints() < 0)) { revolt(); }
  
  	  // production of resources and recalculation of planet characteristics
  
  	  fuel += fp; if (fuel > (fp * 10)) { fuel = fp * 10; }
  	  ore  += fp; if (ore > (op * 10))  { ore = fp * 10; }
  	  rare += fp; if (rare > (rp * 10)) { rare = fp * 10; }
  
  	  if (pm < 2.5f) { pm += 0.1f; }
  	  sp  = fuel + ore + rare;
  	  gip = fp + op + rp + (int)(wf * pr / 20) + (sp / 10);
  	  wf += (gip / 40);
  	  pr += (gip / 200 / 60);
  	  hd  = ((wf - 1) / 40) + 1;
  
  	  // space fight
  
  	  battle();
  
  	  // report about planet
  
  	  Report report = player.getReport();
  	  report.add(Report.PLANETS, "\n" + toString());
  
  	}
  }

  /**
   *
   */
  private void receive(Fleet fleet, int position) {
  	if ((fleet != null) && (position < QUEUESIZE)) {
  	  flightQueue[position].add(fleet);
  	}
  }

  /**
   *
   */
  private int revolt() {
  	int pduLoss = (int)(Math.random() * (hd + 1));
  	if (pduLoss > pdu) { pduLoss = pdu; }
  	pdu -= pduLoss;
  	if (pdu == 0) {
  	  // changeOwner(new Enemy(game));
  	  Report.addAll(Report.REVOLTS, "\nRevolution on " + name + " (" + number + ")\n");
  	  Report.addAll(Report.REVOLTS, "Planet falls to neutral.\n");
  	}
  	return pduLoss;
  }

  /**
   *
   */
  private void send(int dest, int type, int quantity) {
  	Fleet orbit = flightQueue[0].get(player);
  
  	if (orbit == null) { return; }
  
  	int fi = orbit.getFighter();
  	int tr = orbit.getTransporter();
  	int ships = 0;
  
  	// sendType -> FI | TR | C0 | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C7 | C8 | C9
  	switch (type) {
  	  case Parser.FI:
  			if (fi > quantity) {
  			  ships = quantity; fi -= quantity;
  			} else {
  			  ships = fi; fi = 0;
  			}
  			if (ships > 0) { send(new Fleet(player, ships, 0), dest); }
  			break;
  	  case Parser.TR:
  			if (tr > quantity) {
  			  ships = quantity; tr -= quantity;
  			} else {
  			  ships = fi; tr = 0;
  			}
  			if (ships > 0) { send(new Fleet(player, 0, ships), dest); }
  			break;
  		/*
  	  case Parser.C0:
  		while ((quantity > 0) && (wf >= 8) && (tr >= 1)) {
  		  wf -= 8; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(0, ships), dest); }
  		break;
  	  case Parser.C1:
  		while ((quantity > 0) && (pdu >= 1) && (tr >= 1)) {
  		  pdu -= 1; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(1, ships), dest); }
  		break;
  	  case Parser.C2:
  		while ((quantity > 0) && (fp >= 4) && (tr >= 1)) {
  		  fp -= 4; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(2, ships), dest); }
  		break;
  	  case Parser.C3:
  		while ((quantity > 0) && (op >= 5) && (tr >= 1)) {
  		  op -= 5; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(3, ships), dest); }
  		break;
  	  case Parser.C4:
  		while ((quantity > 0) && (rp >= 2) && (tr >= 1)) {
  		  rp -= 2; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(4, ships), dest); }
  		break;
  	  case Parser.C5:
  		while ((quantity > 0) && (fuel >= 40) && (tr >= 1)) {
  		  fuel -= 40; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(5, ships), dest); }
  		break;
  	  case Parser.C6:
  		while ((quantity > 0) && (ore >= 50) && (tr >= 1)) {
  		  ore -= 50; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(6, ships), dest); }
  		break;
  	  case Parser.C7:
  		while ((quantity > 0) && (rare >= 20) && (tr >= 1)) {
  		  rare -= 20; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(7, ships), dest); }
  		break;
  	  case Parser.C8:
  		while ((quantity > 0) && (fy >= 1) && (tr >= 1)) {
  		  fy -= 1; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(8, ships), dest); }
  		break;
  	  case Parser.C9:
  		while ((quantity > 0) && (ty >= 1) && (tr >= 1)) {
  		  ty -= 1; tr -= 1; ++ships; --quantity;
  		}
  		if (ships > 0) { send(new Fleet(9, ships), dest); }
  		break;
  		*/
  	  default:  // no other send types possible (checked by parser already)
  	}
  
  	if ((fi > 0) || (tr > 0)) {
  	  orbit.setFighter(fi);
  	  orbit.setTransporter(tr);
  	} else {
  	  flightQueue[0].remove(orbit);
  	}
  
  }

  /**
   *
   */
  private void send(Fleet fleet, int planetNr) {
  	Planet destination = game.getPlanet(planetNr);
  	if (planetNr > number) {
  	  destination.receive(fleet, distances[planetNr]+1);
  	} else {
  	  destination.receive(fleet, distances[planetNr]);
  	}
  	flightQueue[0].remove(fleet);  // remove fleet from orbit (if it's from there)
  }

  /**
   *
   */
  public String spy(int level) {
  	StringBuffer buffer = new StringBuffer();
  
  	buffer.append("Planet: "); buffer.append(name);
  	buffer.append(" (");
  	buffer.append(number);
  	buffer.append(") ");
  	buffer.append("Owner: "); buffer.append(player.getName());
  	/*
  	if ((playerNr == 0) && (level > 2)) {
  	  buffer.append(" (");
  	  buffer.append(STRATEGYNAME[strategy]);
  	  buffer.append(")");
  	}
  	*/
  	buffer.append('\n');
  
  	if (level > 1) {
  	  buffer.append("GIP: ");  buffer.append(gip);
  	  buffer.append(" PM: ");   buffer.append(pm);
  	  buffer.append(" WF: ");    buffer.append(wf);
  	  buffer.append(" PR: ");   buffer.append(pr);
  	  buffer.append(" HD: ");   buffer.append(hd);
  	  buffer.append('\n');
  	}
  
  	if (level > 0) {
  	  buffer.append("PDU "); buffer.append(pdu);
  	  buffer.append(" FP "); buffer.append(fp);
  	  buffer.append(" OP "); buffer.append(op);
  	  buffer.append(" RP "); buffer.append(rp);
  	  buffer.append(" FY "); buffer.append(fy);
  	  buffer.append(" TY "); buffer.append(ty);
  	  buffer.append('\n');
  	}
  
  	buffer.append("Fuel: "); buffer.append(fuel);
  	buffer.append(" Ore: ");  buffer.append(ore);
  	buffer.append(" Rare: "); buffer.append(rare);
  	buffer.append('\n');
  
  	return buffer.toString();
  }

  /**
   *
   */
  public String toString() {
  	StringBuffer buffer = new StringBuffer();
  
  	buffer.append("Planet: "); buffer.append(name);
  	buffer.append(" (");
  	buffer.append(number);
  	buffer.append(") ");
  	buffer.append("Owner: "); buffer.append(player.getName());
  	/*
  	if (playerNr == 0) {
  	  buffer.append(" (");
  	  buffer.append(STRATEGYNAME[strategy]);
  	  buffer.append(")");
  	}
  	*/
  	buffer.append('\n');
  
  	buffer.append("GIP: "); buffer.append(gip);
  	buffer.append(" PM: "); buffer.append(pm);
  	buffer.append(" WF: "); buffer.append(wf);
  	buffer.append(" PR: "); buffer.append(pr);
  	buffer.append(" HD: "); buffer.append(hd);
  	buffer.append('\n');
  
  	buffer.append("PDU "); buffer.append(pdu);
  	buffer.append(" FP "); buffer.append(fp);
  	buffer.append(" OP "); buffer.append(op);
  	buffer.append(" RP "); buffer.append(rp);
  	buffer.append(" FY "); buffer.append(fy);
  	buffer.append(" TY "); buffer.append(ty);
  	buffer.append('\n');
  
  	buffer.append("Fuel: ");  buffer.append(fuel);
  	buffer.append(" Ore: ");  buffer.append(ore);
  	buffer.append(" Rare: "); buffer.append(rare);
  	buffer.append('\n');
  
  	return buffer.toString();
  }
  
}