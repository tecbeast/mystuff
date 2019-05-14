package com.balancedbytes.game.ashes.model;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.command.CmdDeclare;
import com.balancedbytes.game.ashes.command.CmdHomeplanet;
import com.balancedbytes.game.ashes.command.CmdPlanetname;
import com.balancedbytes.game.ashes.command.CmdPlayername;
import com.balancedbytes.game.ashes.command.CmdResearch;
import com.balancedbytes.game.ashes.command.CmdSpy;
import com.balancedbytes.game.ashes.command.Command;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.balancedbytes.game.ashes.json.JsonObjectWrapper;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class Player implements IJsonSerializable {
	
	private static final String USER = "user";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private static final String HOME_PLANET_NR = "homePlanetNr";
	private static final String POLITICAL_POINTS = "politicalPoints";
	private static final String FIGHTER_MORALE = "fighterMorale";
	private static final String TRANSPORTER_MORALE = "transporterMorale";
	private static final String POLITICAL_TERMS = "politicalTerms";

	private String fUser;
	private String fName;
	private int fNumber;
	private int fHomePlanetNr;
	private int fPoliticalPoints;
	private int fFighterMorale;      // percent (min = 50%, max = 150%)
	private int fTransporterMorale;  // percent (min = 50%, max = 150%) 
	private PoliticalTerm[] fPoliticalTerms;
	
	private transient Report fReport;
	private transient Map<Category, Integer> fGnpTotals;
	
	private static Log LOG = LogFactory.getLog(Player.class);

	public Player(String user, int number) {

		fNumber = number;

		setUser(user);
		setHomePlanetNr(fNumber);
		setName("player" + fNumber);
		
		setPoliticalPoints(0);
		setFighterMorale(100);
		setTransporterMorale(100);
		fReport = new Report();
		fGnpTotals = Category.buildEmptyMap();
		
		// you start at WAR with neutral,
		// at PEACE with yourself and at NEUTRAL with anyone else
		fPoliticalTerms = new PoliticalTerm[9];
		for (int i = 0; i < fPoliticalTerms.length; i++) {
			if (i == 0) {
				setPoliticalTerm(i, PoliticalTerm.WAR);
			} else if (i == fNumber) {
				setPoliticalTerm(i, PoliticalTerm.PEACE);
			} else {
				setPoliticalTerm(i, PoliticalTerm.NEUTRAL);
			}
		}
		
	}  
  
	public String getUser() {
		return fUser;
	}
	
	public void setUser(String user) {
		fUser = user;
	}
	
	public int getHomePlanetNr() {
		return fHomePlanetNr;
	}
		
	public void setHomePlanetNr(int planetNr) {
		if ((planetNr >= 1) && (planetNr <= 40)) {
			fHomePlanetNr = planetNr;
		}
	}

	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}

	public int getNumber() {
		return fNumber;
	}
	
	public int getPoliticalPoints() {
		return fPoliticalPoints;
	}
	
	public void setPoliticalPoints(int politicalPoints) {
		fPoliticalPoints = politicalPoints;
	}
	
	public int getFighterMorale() {
		return fFighterMorale;
	}

	public void setFighterMorale(int percent) {
		if (percent < 50) {
			fFighterMorale = 50;
		} else if (percent > 150) {
			fFighterMorale = 150;
		} else {
			fFighterMorale = percent;
		}
	}

	public int getTransporterMorale() {
		return fTransporterMorale;
	}
	
	public void setTransporterMorale(int percent) {
		if (percent < 50) {
			fTransporterMorale = 50;
		} else if (percent > 150) {
			fTransporterMorale = 150;
		} else {
			fTransporterMorale = percent;
		}
	}
	
	public PoliticalTerm getPoliticalTerm(int otherPlayer) {
		if ((otherPlayer < 0) || (otherPlayer > fPoliticalTerms.length)) {
			return null;
		}
		return fPoliticalTerms[otherPlayer];
	}
	
	public void setPoliticalTerm(int otherPlayer, PoliticalTerm politicalTerm) {
		if ((otherPlayer >= 0) && (otherPlayer < fPoliticalTerms.length)) {
			fPoliticalTerms[otherPlayer] = politicalTerm;
		}
	}
	
	public Map<Category, Integer> getGnpTotals() {
		return fGnpTotals;
	}
	
	private void executeDeclare(Game game, CmdDeclare declareCmd) {
		int opponentNr = declareCmd.getOtherPlayerNr();
		if ((opponentNr > 0) && (opponentNr != fNumber)) {
			PoliticalTerm newPt = declareCmd.getPoliticalTerm();
			PoliticalTerm oldPt = getPoliticalTerm(opponentNr);
			if (((newPt == PoliticalTerm.WAR) && (oldPt == PoliticalTerm.PEACE))
				|| ((newPt == PoliticalTerm.PEACE) && (oldPt == PoliticalTerm.WAR))) {
				newPt = PoliticalTerm.NEUTRAL;
			}
			setPoliticalTerm(opponentNr, newPt);
			LOG.debug("Player " + fNumber + " declares " + newPt + " with player " + opponentNr);
		}
	}
	
	private void executeHomeplanet(Game game, CmdHomeplanet homeplanetCmd) {
		Planet homePlanet = game.getPlanet(homeplanetCmd.getPlanetNr());
		if ((homePlanet != null) && (homePlanet.getPlayerNr() == fNumber)) {
			fHomePlanetNr = homePlanet.getNumber();
			LOG.debug("Player " + fNumber + " sets homeplanet on " + homePlanet.getNumber());
		}					
	}
	
	private void executePlanetname(Game game, CmdPlanetname planetnameCmd) {
		Planet namedPlanet = game.getPlanet(planetnameCmd.getPlanetNr());
		if ((namedPlanet != null) && (namedPlanet.getPlayerNr() == fNumber)) {
			namedPlanet.setName(planetnameCmd.getName());
			LOG.debug("Player " + fNumber + " renames planet " + namedPlanet.getNumber() +  " to " + namedPlanet.getName());
		}
	}

	private void executePlayername(Game game, CmdPlayername playernameCmd) {
		setName(playernameCmd.getName());
		LOG.debug("Player " + fNumber + " renames his/herself to " + fName);
	}
	
	private void executeResearch(Game game, CmdResearch researchCmd) {
		
		// 6.3.4 Politische Punkte (PP)		
		//   PP können auch zur Forschung eingesetzt werden. Dabei kann die
		// wirtschaftliche Entwicklung eines Planeten (Produktionsrate) verbessert oder
		// die Kampfmoral der FI (FM%) oder TR (TM%) angehoben werden.
		// Um die PR anzuheben schreibt man auf das Befehlsblatt "x PP für PR
		// (-Forschung) auf 20 (Crossland)". Mit jedem PP kann die PR des genannten
		// Planeten um den Betrag 0,016 angehoben werden. Die Wahrscheinlichkeit des
		// Gelingens beträgt 50%.
		//   Um die Fighter- oder Transporter-Moral anzuheben, notiert man: "x PP für FM%
		// (bzw TM%)". Mit jedem PP kann FM% (TM%) um den Betrag 0,01 angehoben werden.
		// Die Wahrscheinlichkeit des Gelingens beträgt 50%.
		
		int countSuccess = 0;
		int countFailure = 0;
		for (int i = 1; i <= researchCmd.getCount(); i++) {
			if (fPoliticalPoints > 0) {
				fPoliticalPoints -= 1;
				if ((int) (Math.random() * 100 + 1) <= 50) {
					countSuccess += 1;
					switch (researchCmd.getImprovement()) {
						case PRODUCTION_RATE:
							Planet planet = game.getPlanet(researchCmd.getPlanetNr());
							if ((planet != null) && (planet.getPlayerNr() == fNumber)) {
								planet.setProductionRate(planet.getProductionRate() + 16);
							}
							break;
						case FIGHTER_MORALE:
							setFighterMorale(getFighterMorale() + 1);
							break;
						case TRANSPORTER_MORALE:
							setTransporterMorale(getTransporterMorale() + 1);
							break;
						default:
							break;
					}
				}
			} else {
				countFailure += 1;
			}
		}
		LOG.debug("Player " + fNumber + " improves " + countSuccess + " x " + researchCmd.getImprovement() + " for " + (countSuccess + countFailure) + " PP");					
		
	}
	
	private void executeSpy(Game game, CmdSpy spyCmd) {
		Planet planet = game.getPlanet(spyCmd.getPlanetNr());
		if ((planet != null) && (planet.getPlayerNr() != fNumber) && (fPoliticalPoints >= 7)) {
			fPoliticalPoints -= 7;
			// 100% chance for spyLevel 1 or higher
			// 50% chance for spyLevel 2 or higher
			// 20% chance for spyLevel 3
			int spyLevel = 3;
  			int spyProb = (int) (Math.random() * 100 + 1);
  			if (spyProb > 20) { 
  				spyLevel = 2;
  			}
  			if (spyProb > 50) {
  				spyLevel = 1;
  			}  			
  			// 50% chance to detect spy for max. pm of 250%
  			// 10% chance to detect spy for min. pm of 50%
			boolean detected = (int) (Math.random() * 500 + 1) <= planet.getPlanetaryMorale();			
			getReport().add(planet.createPlanetReport(game, new Message(Topic.INTELLIGENCE), spyLevel));
			if (detected) {
				getReport().add(new Message(Topic.INTELLIGENCE).add("Spy has been caught."));
	  			Player planetOwner = game.getPlayer(planet.getPlayerNr());
	  			planetOwner.getReport().add(new Message(Topic.INTELLIGENCE).add("A spy from " + fName + " (" + fNumber + ") has been caught"
	  				+ " on " + planet.getName() + " ("+ planet.getNumber() + ")"));
			}
			LOG.debug("Player " + fNumber + " uses level " + spyLevel + " spy on " + planet.getNumber() + (detected ? " - caught" : " - not caught"));			
		}		
	}
		
	public void executeCommands(Game game, CommandList cmdList) {
		
		//  7.1 Zuerst werden die neuen PV berechnet. Änderungen werden sofort wirksam,
		//      noch vor den Flugbewegungen. Das gilt auch für den Wechsel des Heimatplaneten
		//      und alle Namensänderungen.
		
		for (Command cmd : cmdList.forPlayer(fNumber).toList()) {
			switch (cmd.getType()) {
				case DECLARE:
					executeDeclare(game, (CmdDeclare) cmd);
					break;
				case HOMEPLANET:
					executeHomeplanet(game, (CmdHomeplanet) cmd);
					break;
				case PLANETNAME:
					executePlanetname(game, (CmdPlanetname) cmd);
					break;
				case PLAYERNAME:
					executePlayername(game, (CmdPlayername) cmd);
					break;
				case RESEARCH:
					executeResearch(game, (CmdResearch) cmd);
				case SPY:
					executeSpy(game, (CmdSpy) cmd);
					break;
				default:
					break;
			}
		}
		
	}
	
	public void startTurn(Game game) {
		
		// correct my political terms (after all players have executed their declare commands) 
		for (int i = 1; i <= 8; i++) {
			PoliticalTerm myPt = getPoliticalTerm(i);
			PoliticalTerm otherPt = game.getPlayer(i).getPoliticalTerm(fNumber);
			if ((myPt == PoliticalTerm.PEACE) && (otherPt != PoliticalTerm.PEACE)) {
				setPoliticalTerm(i, PoliticalTerm.NEUTRAL);
			}
			if ((otherPt == PoliticalTerm.WAR) && (myPt != PoliticalTerm.WAR)) {
				setPoliticalTerm(i, PoliticalTerm.WAR);
			}
		}
		
		int ppMod = 0;
		
		// report political terms and calculate pp modifier
		Message message = new Message(Topic.POLITICS);
		for (int i = 1; i <= 8; i++) {
			if (i != fNumber) {
				Player otherPlayer = game.getPlayer(i);
				StringBuilder line = new StringBuilder();
				line.append("(").append(i).append(") ");
				line.append(AshesUtil.rightpad(otherPlayer.getName(), 20));
				line.append(" ").append(getPoliticalTerm(i).name());
				message.add(line.toString());
				if (getPoliticalTerm(i) == PoliticalTerm.PEACE) {
					ppMod += 1;
				}
				if (getPoliticalTerm(i) == PoliticalTerm.WAR) {
					ppMod -= 1;
				}
			}
		}
		getReport().add(message);

		// modify political points
		setPoliticalPoints(getPoliticalPoints() + ppMod);		
		
		// raise fighter and transporter morale by 5% each turn (max. 100%)
		if (getFighterMorale() <= 95) {
			setFighterMorale(getFighterMorale() + 5);
		}
		if (getTransporterMorale() <= 95) {
			setTransporterMorale(getTransporterMorale() + 5);
		}
		
	}
	
	public void endTurn(Game game) {

		// calculate GNP totals
		fGnpTotals = Category.buildEmptyMap();
		for (int i = 1; i <= 40; i++) {
			Planet planet = game.getPlanet(i);
			FleetList playerFleets = planet.findFleetsForPlayerNr(fNumber);
			fGnpTotals.put(Category.FIGHTERS, fGnpTotals.get(Category.FIGHTERS) + playerFleets.totalFighters());
			fGnpTotals.put(Category.TRANSPORTERS, fGnpTotals.get(Category.TRANSPORTERS) + playerFleets.totalTransporters());
			if (planet.getPlayerNr() == fNumber) {
				fGnpTotals.put(Category.PLANETS, fGnpTotals.get(Category.PLANETS) + 1);
				fGnpTotals.put(Category.FUEL_PLANTS, fGnpTotals.get(Category.FUEL_PLANTS) + planet.getFuelPlants());
				fGnpTotals.put(Category.ORE_PLANTS, fGnpTotals.get(Category.ORE_PLANTS) + planet.getOrePlants());
				fGnpTotals.put(Category.RARE_PLANTS, fGnpTotals.get(Category.RARE_PLANTS) + planet.getRarePlants());
				fGnpTotals.put(Category.FIGHTER_YARDS, fGnpTotals.get(Category.FIGHTER_YARDS) + planet.getFighterYards());
				fGnpTotals.put(Category.TRANSPORTER_YARDS, fGnpTotals.get(Category.TRANSPORTER_YARDS) + planet.getTransporterYards());
				fGnpTotals.put(Category.PLANETARY_DEFENSE_UNITS, fGnpTotals.get(Category.PLANETARY_DEFENSE_UNITS) + planet.getPlanetaryDefenseUnits());
				fGnpTotals.put(Category.STOCK_PILES, fGnpTotals.get(Category.STOCK_PILES) + planet.getStockPiles());
				fGnpTotals.put(Category.GROSS_INDUSTRIAL_PRODUCT, fGnpTotals.get(Category.GROSS_INDUSTRIAL_PRODUCT) + planet.getGrossIndustrialProduct());
			}
		}

		// 4.7 Statusbericht
		//   Direkt an die Flottenübersicht schließt sich der Statusbericht an, in dem
		// alle Variablen des Imperiums als Summen aufgeführt werden, also die Summe
		// aller A, aller Planeten etc., aber auch Werte für Stockpiles (SP), Fighter-
		// (FM%) und Transporter-Moral (TM%), das GIP und die politischen Punkte (PP),
		// die nur hier erscheinen (FM% und TM% -> 6.25).
		
		Message message = new Message(Topic.STATUS);
		message.add(" PL " + fGnpTotals.get(Category.PLANETS));
		message.add(" FP " + fGnpTotals.get(Category.FUEL_PLANTS));
		message.add(" OP " + fGnpTotals.get(Category.ORE_PLANTS));
		message.add(" RP " + fGnpTotals.get(Category.RARE_PLANTS));
		message.add(" FY " + fGnpTotals.get(Category.FIGHTER_YARDS));
		message.add(" TY " + fGnpTotals.get(Category.TRANSPORTER_YARDS));
		message.add("PDU " + fGnpTotals.get(Category.PLANETARY_DEFENSE_UNITS));
		message.add(" SP " + fGnpTotals.get(Category.STOCK_PILES));
		message.add(" FI " + fGnpTotals.get(Category.FIGHTERS));
		message.add(" TR " + fGnpTotals.get(Category.TRANSPORTERS));
		message.add("GIP " + fGnpTotals.get(Category.GROSS_INDUSTRIAL_PRODUCT));
		message.add(" PP " + fGnpTotals.get(Category.POLITICAL_POINTS));
		message.add("FM% " + fFighterMorale);
		message.add("TM% " + fTransporterMorale);
		getReport().add(message);
		
	}	
	
	public Report getReport() {
		return fReport;
	}

	@Override
	public JsonValue toJson() {
		JsonObjectWrapper json = new JsonObjectWrapper(new JsonObject());
		json.add(NUMBER, getNumber());
		json.add(USER, getUser());
		json.add(NAME, getName());
		json.add(HOME_PLANET_NR, getHomePlanetNr());
		json.add(POLITICAL_POINTS, getPoliticalPoints());
		json.add(FIGHTER_MORALE, getFighterMorale());
		json.add(TRANSPORTER_MORALE, getTransporterMorale());
		JsonArray jsonArray = new JsonArray();
		for (int i = 0; i < fPoliticalTerms.length; i++) {
			jsonArray.add(fPoliticalTerms[i].toString());
		}
		json.add(POLITICAL_TERMS, jsonArray);
		return json.toJsonObject();
	}
	
	@Override
	public Player fromJson(JsonValue jsonValue) {
		JsonObjectWrapper json = new JsonObjectWrapper(jsonValue.asObject());
		fNumber = json.getInt(NUMBER);
		setUser(json.getString(USER));
		setName(json.getString(NAME));
		setHomePlanetNr(json.getInt(HOME_PLANET_NR));
		setPoliticalPoints(json.getInt(POLITICAL_POINTS));
		setFighterMorale(json.getInt(FIGHTER_MORALE));
		setTransporterMorale(json.getInt(TRANSPORTER_MORALE));
		JsonArray jsonArray = json.getArray(POLITICAL_TERMS);
		for (int i = 0; i < jsonArray.size(); i++) {
			setPoliticalTerm(i, PoliticalTerm.valueOf(jsonArray.get(i).asString()));
		}
		return this;
	}
  
}