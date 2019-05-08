package com.balancedbytes.game.ashes.model;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.command.CmdBuild;
import com.balancedbytes.game.ashes.command.CmdSend;
import com.balancedbytes.game.ashes.command.Command;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.command.CommandType;
import com.balancedbytes.game.ashes.command.ICommandFilter;

/**
 * Central object for Ashes: handles building, space travel and fights.
 */
public class Planet {

	private String fName;
	private int fNumber;
	private int fPlayerNr;
	
	private int fWorkforce;
	private int fCurrentWorkforce;
	
	private int fFighterYards;
	private int fCurrentFighterYards;
	
	private int fTransporterYards;
	private int fCurrentTransporterYards;	

	private int fPlanetaryDefenseUnits;
	private int fFuelPlants;
	private int fOrePlants;
	private int fRarePlants;

	private int fFuelResources;
	private int fOreResources;
	private int fRareResources;

	private int fHomeDefense;
	private int fProductionRate;   // per mill

	private int fPlanetaryMorale;  // percent (min = 10%, max = 250%)
	private int fStockPiles;
	private int fGrossIndustrialProduct;
	
	private FleetList[] fFlightQueue;

	private static final Log LOG = LogFactory.getLog(Planet.class);

	/**
	 * Create a Planet with the given default settings.
	 */
	public Planet(
		String name,
		int number,
		int playerNr,
		int workforce,
		int homeDefense,
		int productionRate,  // per mill
		int fighters,
		int transporters,
		int planetaryDefenseUnits
	) {

		fName = name;
		fNumber = number;
		fPlayerNr = playerNr;
		fWorkforce = workforce;
		fPlanetaryDefenseUnits = planetaryDefenseUnits;

		fHomeDefense = homeDefense;
		fProductionRate = productionRate;

		fFlightQueue = new FleetList[6];
		for (int i = 0; i < fFlightQueue.length; i++) {
			fFlightQueue[i] = new FleetList();
		}

		fFlightQueue[0].add(new Fleet(fPlayerNr, fighters, transporters));

		if (playerNr == fNumber) {
			fPlanetaryMorale = 150;
		} else {
			fPlanetaryMorale = 100;
		}

		fFuelPlants = 3;
		fOrePlants = 3;
		fRarePlants = 3;
		fFighterYards = 6;
		fTransporterYards = 3;
		fFuelResources = 30;
		fOreResources = 30;
		fRareResources = 30;

		updateStockpileAndGip();

	}
	  
	public String getName() {
		return fName;
	}

	public int getNumber() {
		return fNumber;
	}
	
	public int getPlayerNr() {
		return fPlayerNr;
	}

	public int getWorkforce() {
		return fWorkforce;
	}

	public int getFighterYards() {
		return fFighterYards;
	}

	public int getTransporterYards() {
		return fTransporterYards;
	}
	
	public int getPlanetaryDefenseUnits() {
		return fPlanetaryDefenseUnits;
	}
	
	public int getPlanetaryMorale() {
		return fPlanetaryMorale;
	}
	
	/**
	 * Execute a build command.
	 */
	private boolean build(Game game, CmdBuild buildCommand) {
		
		if ((game == null) || (buildCommand == null) || (buildCommand.getPlanetNr() != getNumber())) {
			return false;
		}
		
		int count = buildCommand.getCount();
		switch (buildCommand.getUnit()) {
			case PLANETARY_DEFENSE_UNIT:
				while ((count > 0) && (fCurrentWorkforce >= 15)) {
					fCurrentWorkforce -= 15;
					fPlanetaryDefenseUnits++;
					count--;
				}
				break;
			case FUEL_PLANT:
				while ((count > 0) && (fCurrentWorkforce >= 5)) {
					fCurrentWorkforce -= 5;
					fFuelPlants++;
					count--;
				}
				break;
			case ORE_PLANT:
				while ((count > 0) && (fCurrentWorkforce >= 4)) {
					fCurrentWorkforce -= 4;
					fOrePlants++;
					count--;
				}
				break;
			case RARE_PLANT:
				while ((count > 0) && (fCurrentWorkforce >= 10)) {
					fCurrentWorkforce -= 10;
					fRarePlants++;
					count--;
				}
				break;
			case FIGHTER_YARD:
				while ((count > 0) && (fCurrentWorkforce >= 15)) {
					fCurrentWorkforce -= 15;
					fFighterYards++;
					count--;
				}
				break;
			case TRANSPORTER_YARD:
				while ((count > 0) && (fCurrentWorkforce >= 20)) {
					fCurrentWorkforce -= 20;
					fTransporterYards++;
					count--;
				}
				break;
			case FIGHTER:
				int fighters = 0;
				while ((count > 0) && (fCurrentWorkforce >= 10)
					&& (fFuelResources >= 1) && (fOreResources >= 1) && (fRareResources >= 3) && (fCurrentFighterYards >= 1)) {
					fCurrentWorkforce -= 10;
					fFuelResources -= 1;
					fOreResources -= 1;
					fRareResources -= 3;
					fCurrentFighterYards -= 1;
					fighters++;
					count--;
				}
				if (fighters > 0) {
					receive(new Fleet(fPlayerNr, fighters, 0), 0);
				}
				break;
			case TRANSPORTER:
				int transporters = 0;
				while ((count > 0) && (fCurrentWorkforce >= 20)
					&& (fFuelResources >= 2) && (fOreResources >= 3) && (fRareResources >= 1) && (fCurrentTransporterYards >= 1)) {
					fCurrentWorkforce -= 20;
					fFuelResources -= 2;
					fOreResources -= 3;
					fRareResources -= 1;
					fCurrentTransporterYards -= 1;
					transporters++;
					count--;
				}
				if (transporters > 0) {
					receive(new Fleet(fPlayerNr, 0, transporters), 0);
				}
				break;
			default:
				break;
		}
		
		LOG.info("build " + (buildCommand.getCount() - count) + buildCommand.getUnit());
		if (count == 0) {
			return true;  // everything has been built successfully
		}
		
		return false;
		
	}
	
	/**
	 * Execute a send command.
	 */
	private boolean send(Game game, CmdSend sendCommand) {
		
		if ((game == null) || (sendCommand == null)) {
			return false;
		}
	  	
		Fleet orbit = fFlightQueue[0].forPlayerNr(fPlayerNr);
	  	if (orbit == null) {
	  		return false;
	  	}
	  
	  	int fighters = orbit.getFighters();
	  	int transporters = orbit.getTransporters();
	  	int count = 0;
	  
	  	switch (sendCommand.getUnit()) {
	  	  	case FIGHTER:
	  	  		count = Math.min(sendCommand.getCount(), fighters);
	  	  		fighters -= count;
	  			if (count > 0) {
	  				send(game, new Fleet(fPlayerNr, count, 0), sendCommand.getToPlanetNr());
	  			}
	  			break;
	  	  	case TRANSPORTER:
	  	  		count = Math.min(sendCommand.getCount(), transporters);
	  	  		transporters -= count;
	  	  		if (count > 0) {
	  	  			send(game, new Fleet(fPlayerNr, 0, count), sendCommand.getToPlanetNr());
	  	  		}
	  			break;
	  	  	case CARGO_0:
		  		while ((count < sendCommand.getCount()) && (fWorkforce >= 8) && (transporters >= 1)) {
		  			fWorkforce -= 8;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(0, count)), sendCommand.getToPlanetNr());
			  	}
			  	break;
			case CARGO_1:
		  		while ((count < sendCommand.getCount()) && (fPlanetaryDefenseUnits >= 1) && (transporters >= 1)) {
		  			fPlanetaryDefenseUnits -= 1;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(1, count)), sendCommand.getToPlanetNr());
			  	}
		  		break;
			case CARGO_2:
		  		while ((count < sendCommand.getCount()) && (fFuelPlants >= 4) && (transporters >= 1)) {
		  			fFuelPlants -= 4;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(2, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_3:
		  		while ((count < sendCommand.getCount()) && (fOrePlants >= 5) && (transporters >= 1)) {
		  			fOrePlants -= 5;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(3, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_4:
		  		while ((count < sendCommand.getCount()) && (fRarePlants >= 2) && (transporters >= 1)) {
		  			fRarePlants -= 2;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(4, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_5:
		  		while ((count < sendCommand.getCount()) && (fFuelResources >= 40) && (transporters >= 1)) {
		  			fFuelResources -= 40;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(5, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_6:
		  		while ((count < sendCommand.getCount()) && (fOreResources >= 50) && (transporters >= 1)) {
			  		fOreResources -= 50;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(6, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_7:
		  		while ((count < sendCommand.getCount()) && (fRareResources >= 20) && (transporters >= 1)) {
			  		fRareResources -= 20;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(7, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_8:
		  		while ((count < sendCommand.getCount()) && (fFighterYards >= 1) && (transporters >= 1)) {
			  		fFighterYards -= 1;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(8, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			case CARGO_9:
		  		while ((count < sendCommand.getCount()) && (fTransporterYards >= 1) && (transporters >= 1)) {
			  		fTransporterYards -= 1;
		  			transporters -= 1;
		  			count++;
		  		}
			  	if (count > 0) {
			  		send(game, new Fleet(new Cargo(9, count)), sendCommand.getToPlanetNr());
			  	}
				break;
			default:
				// no other units possible
				break;
	  	}
	  		
		LOG.info("send " + (sendCommand.getCount() - count) + sendCommand.getUnit());
		if (count == 0) {
			return true;  // everything has been sent successfully
		}

		return false;
	  
	}
	
	/**
	 *
	 */
	private void send(Game game, Fleet fleet, int toPlanetNr) {
		
		if ((game == null) || (fleet == null) || (toPlanetNr <= 0) || (toPlanetNr > 40)) {
			return;
		}
		
		int distance = Game.getDistance(getNumber(), toPlanetNr);
		Planet destination = game.getPlanet(toPlanetNr);
		// flight to a higher planet number is distance + 1
		// because that planet's turn has not been played yet
		destination.receive(fleet, (toPlanetNr > getNumber()) ? distance + 1 : distance);
		
		Fleet orbit = fFlightQueue[0].forPlayerNr(fPlayerNr);
		orbit.setFighters(orbit.getFighters() - fleet.getFighters());
		orbit.setTransporters(orbit.getTransporters() - fleet.getTransporters() - fleet.getCargo().total());
		if ((orbit.getFighters() == 0) && (orbit.getTransporters() == 0)) {
			fFlightQueue[0].remove(orbit);
		}
		
	}
	
	/**
	 *
	 */
	private void receive(Fleet fleet, int position) {
		if ((fleet == null) || (position < 0) || (position >= fFlightQueue.length)) {
			return;
		}
		fFlightQueue[position].add(fleet);
	}

	
	/**
	 *
	 */
	private void flee(Game game, FleetList fleets) {
		
		if ((game == null) || (fleets == null)) {
			return;
		}
		
		for (int i = 0; i < fleets.size(); i++) {
			
			Fleet fleet = fleets.get(i);
			if (fleet.getPlayerNr() > 0) {
				
				// fighters and transporters flee
				// this takes an extra turn for orientation (distance increased by one)
				if ((fleet.getFighters() > 0) || (fleet.getTransporters() > 0)) {
					Player otherPlayer = game.getPlayer(fleet.getPlayerNr());
					Planet destination = game.getPlanet(otherPlayer.getHomePlanetNr());
					int distance = Game.getDistance(getNumber(), destination.getNumber()) + 1;
					destination.receive(fleet, (destination.getNumber() > getNumber()) ? distance + 1 : distance);
				}
				
				// this fleet no longer in flight queue
				fFlightQueue[0].remove(fleet);
				
			}

		}
		
	}
	
	/**
	 * Play a full turn for this planet with the given commands.
	 * 
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
	public void turn(Game game, CommandList cmdList) {

		LOG.info("Planet " + getNumber() + " turn " + game.getTurn());
		
		// fleet report to all friendly players (if higher than limit) and owner (who is friendly to himself)
  	  	for (int i = 1; i < 9; i++) {
  	  		if (game.getPoliticalTerm(fPlayerNr, i) == PoliticalTerm.PEACE) {
  	  			Report fleetReport = createFleetReport(game, i);
  	  			game.getPlayer(i).getReporting().add(fleetReport);
  	  		}
  	  	}
  
  	  	// report approaching cargoships
  	  	reportCargo(game);

		//    7.1 Zuerst werden die neuen PV berechnet. Änderungen werden sofort wirksam,
		//  noch vor den Flugbewegungen. Das gilt auch für den Wechsel des Heimatplaneten
		//  und alle Namensänderungen.

		//    7.2 Liegen entsprechende Befehle vor und sind ausreichend TR und Güter
		//  vorhanden, werden Frachtschiffe beladen. Die verladenen Güter stehen in
		//  derselben Runde nicht weiter zur Verfügung.

		for (Command command : getSendCommands(cmdList, true)) {
			send(game, (CmdSend) command);
		}

		//    7.3 Bei der Abwicklung der Flugbewegungen rückt jede Flotte um eine Etappe
		//  auf ihrer Route vor. Neutrale Planeten schicken Flotten (FI und TR) aus ihrem
		//  Orbit mit zufälligem Ziel auf die Reise.
		
	  	advanceFlightQueue();
	  	
		for (Command command : getSendCommands(cmdList, false)) {
			send(game, (CmdSend) command);
		}

		//    7.4 Als nächstes werden alle Produktionsbefehle ausgeführt und zwar in der
		//  Reihenfolge: PDU, FP, OP, RP, FY, TY, FI und TR. Reichen die vorhandenen A für
		//  die angegebenen Produktionen nicht aus oder fehlen für den Bau von FI oder TR
		//  Werften oder FUEL, ORE und RARE, wird die Produktion entsprechend gekürzt.
		//  Viele SL weisen durch eine Meldung ("A short on (Planetennummer oder -name)",
		//  o.ä. in den GM darauf hin.
		//    Hierbei werden die Vorräte neu berechnet. Der Wert verringert sich
		//  entsprechend dem Verbrauch durch den Bau von FI oder TR.
		//    (Neutrale Planeten bauen auch und zwar in erster Linie FI und dann TR, die
		//  restlichen A werden für FI-Werften und Fabriken aufgewendet).
		
		for (Command command : getOrderedBuildCommands(cmdList)) {
			build(game, (CmdBuild) command);
		}

		//    7.5 Ist PDU kleiner als HD oder PP kleiner als 0, so wird eine Revolte
		//  abgewickelt. Dies veröffentlicht der SL in den GM. Die Revolte kann auch dazu
		//  führen, daß der Planet an Neutral fällt.
		
  	  	if ((fPlanetaryDefenseUnits < fHomeDefense) || (game.getPlayer(getPlayerNr()).getPoliticalPoints() < 0)) {
  	  		revolt(game);
  	  	}

		//    7.6 Erst danach wird produziert. (Vorräte erhöhen sich um Anzahl Fabriken
		//  mal Produktionsrate). Übersteigt die Produktion die Lagerkapazität (Anzahl
		//  Fabriken mal 10), so ist der Überschuß verloren. Anschließend werden
		//  Planetenmoral (PM%), GIP, A, PR und HD neu berechnet (Steigt also HD infolge
		//  eines Anstiegs von A über den Wert von PDU, kommt es nicht zur Revolte. Da die
		//  Baubefehle vor der Revolte ausgeführt werden, können in der folgenden Runde
		//  noch rechtzeitig PDU nachgebaut werden).
		//    Bei neutralen Planeten ist PDU immer gleich HD. Dafür werden keine
		//  Arbeitskräfte benötigt. Es werden hier auch keine weiteren PDU gebaut;
  	  	//  weitere PDU können jedoch von Frachtern stammen!
	  	  
	  	fFuelResources = Math.min(fFuelResources + fFuelPlants, fFuelPlants * 10);
		fOreResources = Math.min(fOreResources + fOrePlants, fOrePlants * 10);
		fRareResources = Math.min(fRareResources + fRarePlants, fRarePlants * 10);
		
	  	updateStockpileAndGip();
	  	
	  	fPlanetaryMorale = Math.min(fPlanetaryMorale + 10, 250);
		fWorkforce += fGrossIndustrialProduct / 40;
		fProductionRate += fGrossIndustrialProduct / 12;  // pr += (gip / 200 / 60);
		fHomeDefense = ((fWorkforce - 1) / 40) + 1;

		//    7.7 Befinden sich nun im Orbit (I) eines Planeten eine oder mehrere Flotten,
		//  die zum Planetenbesitzer PV=0 haben, treten alle anwesenden Flotten in ein
		//  Raumgefecht ein, sofern sie nicht zum Besitzer des Planeten neutral (PV=1)
		//  sind.

		//    7.8 Hat der Besitzer des Planeten (oder seine Verbündeten) keine Raumschiffe
		//  (mehr) im Orbit, also die Raumherrschaft verloren, wird anschließend ein
		//  Landeversuch abgewickelt, falls noch angreifende Flotten da sind. Hat der
		//  Planetenbesitzer dem Landeversuch abgewehrt, nutzt die Bevölkerung die Gunst
		//  der Stunde und revoltiert. Hierbei zählt aber bereits die aktualisierte HD.

		battle(game);		

		//    7.9 Erst jetzt können eventuell angenommene Frachter entladen und wieder in
		//  TR umgewandelt werden; andernfalls beziehen sie eine Warteposition in (D+0).
		
		//  Zu beachten ist, daß Frachter nur be- und entladen werden können, wenn man
		//  die Lufthoheit über dem Planeten besitzt und dazu muß man einen (eigenen,
		//  nicht alliierten) FI im Orbit haben.
		//    Bei den Gütern ist zu berücksichtigen, daß sie sofort eingeladen werden. Das
		//  heißt, wenn man zum Beispiel PDU verschifft, kann die Zahl der verbleibenden
		//  dadurch unter HD fallen, wodurch (nach der Produktionsphase unter Umständen)
		//  eine Revolte ausgelöst wird. Bei der Verschiffung von FUEL, ORE oder RARE
		//  sollte auf dem Zielplaneten zum Zeitpunkt des Entladens ausreichend
		//  Lagerkapazität vorhanden sein, weil der Überschuß sonst verloren geht.
		
		Fleet ownFleet = fFlightQueue[0].forPlayerNr(fPlayerNr);
		if ((ownFleet != null) && (ownFleet.getFighters() > 0)) {
			unloadCargo();
		}

  	  	// report about planet
		Report planetReport = createPlanetReport(game);
		game.getPlayer(getPlayerNr()).getReporting().add(planetReport);

		//    7.10 Zum Abschluß wird die GNP-Tabelle berechnet.

		//    7.11 Prüfung, ob Siegbedingung erfüllt ist; falls ja, Ausdruck der Statistik
		//  für die HoF.  

  	}

	private List<Command> getSendCommands(CommandList cmdList, boolean cargo) {
		return cmdList.filter(new ICommandFilter() {
			@Override
			public boolean filter(Command cmd) {
				return (cmd.getType() == CommandType.SEND)
					&& (cmd.getPlayerNr() == getPlayerNr()) && (((CmdSend) cmd).getFromPlanetNr() == getNumber())
					&& (((CmdSend) cmd).isCargo() == cargo);
			}
		}).toList();
	}

	private List<Command> getOrderedBuildCommands(CommandList cmdList) {
		return cmdList.filter(new ICommandFilter() {
			@Override
			public boolean filter(Command cmd) {
				return (cmd != null) && (cmd.getType() == CommandType.BUILD) && (cmd.getPlayerNr() == getPlayerNr()) && (((CmdBuild) cmd).getPlanetNr() == getNumber());
			}
		}).sort(new Comparator<Command>() {
			@Override
			public int compare(Command c1, Command c2) {
				int o1 = ((CmdBuild) c1).getUnit().getOrder();
				int o2 = ((CmdBuild) c2).getUnit().getOrder();
				if (o1 > o2) {
					return 1;
				} else if (o1 < o2) {
					return -1;
				} else {
					return 0;
				}
			}
		}).toList();
	}

	/**
	 * 
	 */
	private void unloadCargo() {
		int transporters = 0;  // nr of transporter gained
		for (Fleet fleet : fFlightQueue[0].toList()) {
			Cargo cargo = fleet.getCargo();
			if (cargo.get(0) > 0) {
				fWorkforce += cargo.get(0) * 8;
				transporters += cargo.get(0);
			}
			if (cargo.get(1) > 0) {
				fPlanetaryDefenseUnits += cargo.get(1);
				transporters += cargo.get(1);
			}
			if (cargo.get(2) > 0) {
				fFuelPlants += cargo.get(2) * 4;
				transporters += cargo.get(2);
			}
			if (cargo.get(3) > 0) {
				fOrePlants += cargo.get(3) * 5;
				transporters += cargo.get(3);
			}
			if (cargo.get(4) > 0) {
				fRarePlants += cargo.get(4) * 2;
				transporters += cargo.get(4);
			}
			if (cargo.get(5) > 0) {
				fFuelResources += cargo.get(5) * 40;
				transporters += cargo.get(5);
			}
			if (cargo.get(6) > 0) {
				fOreResources += cargo.get(6) * 50;
				transporters += cargo.get(6);
			}
			if (cargo.get(7) > 0) {
				fRareResources += cargo.get(7) * 20;
				transporters += cargo.get(7);
			}
			if (cargo.get(8) > 0) {
				fFighterYards += cargo.get(8);
				transporters += cargo.get(8);
			}
			if (cargo.get(9) > 0) {
				fTransporterYards += cargo.get(9);
				transporters += cargo.get(9);
			}
		}
		if (transporters > 0) {
			fFlightQueue[0].add(new Fleet(fPlayerNr, 0, transporters));
		}
	}
	
	/**
	 * 
	 */
	private void updateStockpileAndGip() {
		fStockPiles = fFuelResources + fOreResources + fRareResources;
		fGrossIndustrialProduct = (int) (fFuelPlants + fOrePlants + fRarePlants + (int) (fWorkforce * (double) fProductionRate / 20000) + (fStockPiles / 10));
	}
	
	/**
	 * 
	 */
	private void advanceFlightQueue() {
		for (int i = 0; i < fFlightQueue.length; i++) {
	  		if (i == 0) {
	  		  	fFlightQueue[i].add(fFlightQueue[i+1]);
	  		} else if (i < fFlightQueue.length - 1) {
		  		fFlightQueue[i] = fFlightQueue[i+1];
	  		} else {
	  			fFlightQueue[i] = new FleetList();
	  		}
	  	}
	}	

	/**
	 *
	 */
	private void battle(Game game) {
  	
		Report report = new Report(Topic.BATTLE);

		// calculate attack strength and defense strength
		int af = 0, df = 0, as = 0, ds = 0;
		FleetList attacker = new FleetList();
		FleetList defender = new FleetList();
		FleetList involved = new FleetList().add(fFlightQueue[0]);

		for (int i = 0; i < involved.size(); i++) {
			
			Fleet fleet = involved.get(i);
			Player player = game.getPlayer(fleet.getPlayerNr());
			
			if (player.getNumber() == getPlayerNr()) {
				defender.add(fleet);
				df += fleet.getFighters();
				ds += (int) (fleet.getFighters() * (double) player.getFighterMorale() / 100 * fPlanetaryMorale);
			
			} else {
				PoliticalTerm pt = game.getPoliticalTerm(player.getNumber(), getPlayerNr());
				if (pt != PoliticalTerm.NEUTRAL) {
					if (pt == PoliticalTerm.PEACE) {
						defender.add(fleet);
						df += fleet.getFighters();
						ds += (int) (fleet.getFighters() * (double) player.getFighterMorale() / 100);
					} else {
						attacker.add(fleet);
						af += fleet.getFighters();
						as += (int) (fleet.getFighters() * (double) player.getFighterMorale() / 100);
					}
				}
			}
			
		}
  
		if (as > 0) {
			LOG.info("Battle at " + fNumber + ":  AS " + as + " DS " + ds);
		}
  
	  	int[] atkWin = new int[attacker.size()], atkLoss = new int[attacker.size()];
	  	int atkBonus = 0, defBonus = 0; 
	  	double ad = 2.5;  // superior if there is no fight
  
	  	// is there a fight ?
	  	if (as > 0) {
  
	  		Player owner = game.getPlayer(fPlayerNr);
	  		report.add("Battle at " + fName + " (" + fNumber + ") Owner: " + owner.getName() + " (" + owner.getNumber() + ")");
  
	  		if (ds > 0) {
	  			
	  			int[] defWin = new int[defender.size()];
	  			int[] defLoss = new int[defender.size()];

	  			ad = Math.min((as > ds) ? ((double) as / ds) : ((double) ds / as), 2.5);
	  			
	  			for (int i = 0; i < attacker.size(); i++) {
	  				
	  				Fleet atkFleet = attacker.get(i);
	  				Player atkPlayer = game.getPlayer(atkFleet.getPlayerNr());
	  				
	  				for (int j = 0; j < defender.size(); j++) {
	  			
	  					Fleet defFleet = defender.get(j);
	  					Player defPlayer = game.getPlayer(defFleet.getPlayerNr());
	  					
	  					int defKills = (int) ((((double) defFleet.getFighters() * defPlayer.getFighterMorale() / 100 / ad) * ((double) atkFleet.getFighters() / af) + 1) / 2);
	  					int atkKills = (int) ((((double) atkFleet.getFighters() * atkPlayer.getFighterMorale() / 100 / ad) * ((double) defFleet.getFighters() / df) + 1) / 2);
	  
	  					LOG.info("atkFighter " + atkFleet.getFighters() + " atkMorale% " + atkPlayer.getFighterMorale() + " af " + af);
	  					LOG.info("defFighter " + defFleet.getFighters() + " defMorale% " + defPlayer.getFighterMorale() + " df " + df);
	  					LOG.info("ad " + ad + " defKills " + defKills + " atkKills " + atkKills);
	  
	  					defWin[j] += defKills;
	  					defLoss[j] += atkKills;
	  					
	  					atkWin[i] += atkKills;
	  					atkLoss[i] += defKills;
	  					
	  				}
	  				
	  			}
	  		
	  			if (as > ds) {
	  				atkBonus = 1;
	  			} else {
	  				defBonus = 1;
	  			}
  			
	  			for (int i = 0; i < attacker.size(); i++) {
	  				
	  				Fleet atkFleet = attacker.get(i);
	  				Player atkPlayer = game.getPlayer(atkFleet.getPlayerNr());
  
	  				StringBuilder line = new StringBuilder();
	  				line.append(atkPlayer.getName()).append(" (").append(atkPlayer.getNumber()).append(") attacks with ");
	  				line.append(atkFleet.getFighters()).append(" FI (-");
	  				if (atkLoss[i] < atkFleet.getFighters()) {
	  					line.append(atkLoss[i]);
	  				} else {
	  					line.append(atkFleet.getFighters());
	  				}
	  				line.append(" FI)");
	  				report.add(line.toString());
  
	  				atkPlayer.setFighterMorale(atkPlayer.getFighterMorale() + atkWin[i] - atkLoss[i] + atkBonus);
	  				atkFleet.setFighters(atkFleet.getFighters() - atkLoss[i]);
	  				
	  			}
  			
	  			for (int j = 0; j < defender.size(); j++) {
	  				
	  				Fleet defFleet = defender.get(j);
	  				Player defPlayer = game.getPlayer(defFleet.getPlayerNr());
  
	  				StringBuilder line = new StringBuilder();
	  				line.append(defPlayer.getName()).append(" (").append(defPlayer.getNumber()).append(")  defends with ");
	  				line.append(defFleet.getFighters()).append(" FI (-");
	  				if (defLoss[j] < defFleet.getFighters()) {
	  					line.append(defLoss[j]);
	  				} else {
	  					line.append(defFleet.getFighters());
	  				}
	  				line.append(" FI)");
	  				report.add(line.toString());
	  				
	  				defPlayer.setFighterMorale(defPlayer.getFighterMorale() + defWin[j] - defLoss[j] + defBonus);
	  				defFleet.setFighters(defFleet.getFighters() - defLoss[j]);
	  				
	  			}
	  			
	  		} else {
	  			
	  			for (int i = 0; i < attacker.size(); i++) {
	  				Fleet atkFleet = attacker.get(i);
	  				Player atkPlayer = game.getPlayer(atkFleet.getPlayerNr());
	  				report.add(atkPlayer.getName() + " (" + atkPlayer.getNumber() + ") attacks with " + atkFleet.getFighters() + " FI");
	  			}
	  			
	  			report.add("no resistance");
	  			
	  		}
	  		
	  		if (as > ds) {
	  			
	  			if (ds > 0) {
	  				report.add("The defenders flee to their homeplanet (-" + defender.totalTransporters() + " TR)");
	  				defender.removeTransporters();
	  				flee(game, defender);
	  			}
	  			
	  		} else {
	  			
	  			report.add("The attackers flee to their homeplanet (-" + attacker.totalTransporters() + " TR)");
	  			attacker.removeTransporters();
	  			flee(game, attacker);
	  			
	  		}
	  		
	  	}
  
	  	// landing attempts and revolution
  
	  	int at = attacker.totalTransporters();
	  	int pduLoss = 0;
  
	  	if ((at > 0) && (((as > 0) && (as > ds)) || ((as == 0) && (ds == 0)))) {
  
	  		as = 0;
	  		ds = (int) ((double) getPlanetaryDefenseUnits() * getPlanetaryMorale() / 100);
	  		
	  		for (int i = 0; i < attacker.size(); i++) {
	  			
	  			Fleet atkFleet = attacker.get(i);
	  			Player atkPlayer = game.getPlayer(atkFleet.getPlayerNr());
	  			int defKills = (int) (0.5 + (ds * atkFleet.getTransporters() / at));
	  			int strength = (int) ((double) atkFleet.getTransporters() * atkPlayer.getTransporterMorale() / 100);
	  			int atkKills = (int) (0.5 + (strength * ad));
	  			
	  			as += strength;
	  			atkWin[i] = atkKills;
	  			
	  			atkLoss[i] = defKills;
	  			pduLoss += atkKills;
	  			
	  		}
	  		
	  		as = (int)(as * ad);
  
	  		if (as > ds) {
	  			atkBonus = 1;
	  		} else {
	  			atkBonus = 0;
	  		}
  
	  		for (int i = 0; i < attacker.size(); i++) {
	  			Fleet atkFleet = attacker.get(i);
	  			Player atkPlayer = game.getPlayer(atkFleet.getPlayerNr());
	  			if (atkFleet.getTransporters() > 0) {
	  				report.add(atkPlayer.getName() + " (" + atkPlayer.getNumber() + ") attempts landing with " + atkFleet.getTransporters() + " TR (-" + atkLoss[i] + " TR)");
	  				atkPlayer.setTransporterMorale(atkPlayer.getTransporterMorale() + atkWin[i] - atkLoss[i] + atkBonus);
	  				atkFleet.setTransporters(atkFleet.getTransporters() - atkLoss[i]);
	  			}
	  		}
  
	  		if (getPlanetaryDefenseUnits() < pduLoss) {
	  			pduLoss = getPlanetaryDefenseUnits();
	  		}
  
	  		report.add("Planet defends with " + fPlanetaryDefenseUnits + " PDU (-" + pduLoss + " PDU)");
	  		fPlanetaryDefenseUnits -= pduLoss;
  
	  		// planet conquered
	  		if (as > ds) {
  
	  			int trMax = 0, conquerer = 0;
	  			double tmMax = 0.0;
	  			attacker.shuffle();
	  			
	  			for (int i = 0; i < attacker.size(); i++) {
	  				int tr = attacker.get(i).getTransporters();
	  				double tm = (double) game.getPlayer(attacker.get(i).getPlayerNr()).getTransporterMorale() / 100;
	  				if (tr > trMax) {
	  					trMax = tr;
	  					tmMax = tm;
	  					conquerer = i;
	  				} else if (tr == trMax) {
	  					if (tm > tmMax) {
	  						tmMax = tm;
	  						conquerer = i;
	  					}
	  				}
	  			}
	  			
	  			if (trMax > 0) {
	  				Player newOwner = game.getPlayer(attacker.get(conquerer).getPlayerNr());
	  				changeOwner(newOwner.getNumber());
	  				report.add(newOwner.getName() + " (" + newOwner.getNumber() + ")  conquers the planet.");
	  			}
  
	  		// revolution attempt
	  		} else {
  
	  			report.add("Planet defends successfully.");
  
	  			pduLoss = revolt(game);
  
	  			if ((fPlanetaryDefenseUnits > 0) && (pduLoss > 0)) {
	  				report.add("Riots destroy further " + pduLoss + " PDU");
	  			} else if (fPlanetaryDefenseUnits == 0) {
	  				report.add("The population takes advantage and revolts.");
	  			}
	  			
	  		}
	  		
	  	}
  
	  	if (report.size() > 0) {
	  		Player owner = game.getPlayer(getPlayerNr());
	  		owner.getReporting().add(report);
	  		for (int i = 0; i < involved.size(); i++) {
	  			Fleet fleet = involved.get(i);
	  			if (fleet.getPlayerNr() != getPlayerNr()) {
	  				Player player = game.getPlayer(fleet.getPlayerNr());
	  				player.getReporting().add(report);
	  			}
	  		}
	  	}
  
	}


	/**
	 *
	 */
	private void reportCargo(Game game) {
		
		int total = 0;
		Report orbitReport = new Report(Topic.CARGO);
	
  		for (int i = 0; i < 5; i++) {
  			
  			Cargo cargo = fFlightQueue[i].getCargo();
  			if (cargo.total() > 0) {
  				total += cargo.total();
  				StringBuilder line = new StringBuilder();
  				line.append((getNumber() < 10) ? " " : "").append(getNumber());
  				if (i == 0) {
  					line.append("   (I)");
  				} else if (i == 1) {
  					line.append("   (D)");
  				} else {
  					line.append(" (D+").append(i - 1).append(")");
  				}
  				for (int j = 0; j < 10; j++) {
  					if (cargo.get(j) > 0) {
  						line.append(" ").append(cargo.get(j)).append(" C").append(j);
  					}
  				}
  				orbitReport.add(line.toString());
  			}
  			
  		}
  		
  		Report otherReport = new Report(Topic.CARGO);
  		otherReport.add(total + " cargoships approaching " + fName + " (" + fNumber + ")");
  		
  		for (int i = 1; i < 9; i++) {
  			Player player = game.getPlayer(i);
  			Fleet fleet = fFlightQueue[0].forPlayerNr(i);
  			if ((i == fPlayerNr) || ((fleet != null) && ((fleet.getFighters() > 0) || (fleet.getTransporters() > 0)))) {
  				player.getReporting().add(orbitReport);
  			} else {
  				player.getReporting().add(otherReport);
  			}
  		}
  
	}

  	private void changeOwner(int newOwnerNr) {
  		// planetary morale drops to 50% if planet was conquered
  		fPlanetaryMorale = 50;
  		fPlayerNr = newOwnerNr;
  	}

	/**
	 *
	 */
	private Report createFleetReport(Game game, int playerNr) {

		Report report = new Report((playerNr == fPlayerNr) ? Topic.FLEET : Topic.INTELLIGENCE);

		int limit = 0;
  		if (playerNr != fPlayerNr) {
  			limit = game.getTurn() * 2 + 9;
  		}
  		
  		for (int i = 0; i < 5; i++) {
  			
  			int ships = fFlightQueue[i].totalFighters() + fFlightQueue[i].totalTransporters();
  			if (ships > limit) {
  				StringBuilder line = new StringBuilder();
  				line.append((getNumber() < 10) ? " " : "").append(getNumber());
  				if (i == 0) {
  					line.append("   (I) ");
  				} else if (i == 1) {
  					line.append("   (D) ");
  				} else {
  					line.append(" (D+").append(i - 1).append(") ");
  				}
  				if (fFlightQueue[i].totalFighters() > 0) {
  					line.append(fFlightQueue[0].totalFighters()).append(" FI ");
  				}
  				if (fFlightQueue[i].totalTransporters() > 0) {
  					line.append(fFlightQueue[0].totalTransporters()).append(" TR ");
  				}
  				report.add(line.toString());
  			}
  			
  		}

  		return report;
  	  
	}


	/**
	 *
	 */
	private int revolt(Game game) {
		int pduLoss = (int) (Math.random() * (fHomeDefense + 1));
		if (pduLoss > fPlanetaryDefenseUnits) {
			pduLoss = fPlanetaryDefenseUnits;
		}
		fPlanetaryDefenseUnits -= pduLoss;
		if (fPlanetaryDefenseUnits == 0) {
			changeOwner(0);
			Report report = new Report(Topic.REVOLT);
			report.add("Revolution on " + fName + " (" + fNumber + ")");
			report.add("Planet falls to neutral.");
			game.addReportToAllPlayers(report);
		}
		return pduLoss;
	}

	/**
	 *
	 */
	public Report createPlanetReport(Game game) {

		Report report = new Report(Topic.PLANET);
		Player owner = game.getPlayer(fPlayerNr);
		
		StringBuilder line = new StringBuilder();
		line.append("Planet: ").append(fName);
		line.append(" (").append(fNumber).append(")");
		line.append(" Owner: ").append(owner.getName());
		line.append(" (").append(owner.getNumber()).append(")");
		report.add(line.toString());

		line = new StringBuilder();
		line.append("GIP: ").append(fGrossIndustrialProduct);
		line.append(" PM: ").append(fPlanetaryMorale);
		line.append(" WF: ").append(fWorkforce);
		line.append(" PR: ").append(fProductionRate);
		line.append(" HD: ").append(fHomeDefense);
		report.add(line.toString());

		line = new StringBuilder();
		line.append("PDU ").append(fPlanetaryDefenseUnits);
		line.append(" FP ").append(fFuelPlants);
		line.append(" OP ").append(fOrePlants);
		line.append(" RP ").append(fRarePlants);
		line.append(" FY ").append(fFighterYards);
		line.append(" TY ").append(fTransporterYards);
		report.add(line.toString());

		line = new StringBuilder();
		line.append("Fuel: ").append(fFuelResources);
		line.append(" Ore: ").append(fOreResources);
		line.append(" Rare: ").append(fRareResources);
		report.add(line.toString());

		return report;

	}
  
}