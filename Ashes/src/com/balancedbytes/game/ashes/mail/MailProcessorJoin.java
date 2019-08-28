package com.balancedbytes.game.ashes.mail;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.Game;
import com.balancedbytes.game.ashes.model.Join;
import com.balancedbytes.game.ashes.model.JoinCache;
import com.balancedbytes.game.ashes.model.Player;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorJoin {

	public static final String JOIN = "join";
	
	private static final String GAME = "game";
	private static final String USER = "user";
	private static final String HOMEPLANET = "homeplanet";

	private static final String DEFAULT = "default";
	
	private static final SecureRandom RANDOM = new SecureRandom();

	protected MailProcessorJoin() {
		super();
	}
	
	// Join User TestUser
	// Join Game TestGame User TestUser
	public void process(Mail mail) {
		
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			new String[] { JOIN, USER, GAME }, mail.getSubject()
		);
		
		if (!tokenMap.containsKey(JOIN)) {
			return;
		}

		IMailManager mailManager = AshesOfEmpire.getInstance().getMailManager();
		String userName = AshesUtil.print(tokenMap.get(USER));
		String gameName = AshesUtil.print(tokenMap.get(GAME));
		if (!AshesUtil.provided(gameName)) {
			gameName = DEFAULT;
		}
		
		mailManager.sendMail(processJoinGame(userName, gameName, mail.getBody()));
		
	}
	
	private Mail processJoinGame(String userName, String gameName, String mailBody) {

		if (!AshesUtil.provided(mailBody) || !AshesUtil.provided(userName)) {
			return null;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			new String[] { HOMEPLANET }, mailBody
		);
		
		UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		User user = userCache.get(userName);
		
		if ((user == null) || (user.getRegistered() == null)) {
			return null;
		}

		JoinCache joinCache = AshesOfEmpire.getInstance().getJoinCache();
		
		joinCache.getByGameName(gameName);  // fill cache 
		List<Join> joins = joinCache.getByUserName(userName);
		
		Join myJoin = null;
		if (AshesUtil.provided(joins)) {
			for (Join join : joins) {
				if (gameName.equals(join.getGameName())) {
					myJoin = join;
				}
			}
		}

		String homePlanets = tokenMap.get(HOMEPLANET);
		List<Integer> homePlanetList = cleanHomePlanets(AshesUtil.splitIntegerList(homePlanets, ","));
		if ((homePlanetList == null) || (homePlanetList.size() != 8)) {
			return createMailJoinRejected(user, "ERROR: Invalid list of homeplanets.");
		}

		if (myJoin == null) {
			myJoin = joinCache.create(userName, gameName);
		}
		myJoin.setHomePlanets(homePlanetList);
		
		List<Join> allJoins = joinCache.getByGameName(gameName); 

		Mail answer = createMailJoined(myJoin, allJoins.size(), user);
		if (createGame(allJoins)) {
			for (Join join : allJoins) {
				joinCache.delete(join);
			}
		}
		return answer;
		
	}
	
	// make sure the list of homeplanets contains no duplicates
	// and only planet numbers between 1 and 8
	private List<Integer> cleanHomePlanets(List<Integer> homePlanets) {
		if (homePlanets == null) {
			return null;
		}
		List<Integer> result = new ArrayList<Integer>();
		for (Integer homePlanet : homePlanets) {
			if ((homePlanet != null) && (homePlanet > 0) && (homePlanet < 9) && !result.contains(homePlanet)) {
				result.add(homePlanet);
			}
		}
		return result;
	}
	
	private Mail createMailJoined(Join join, int nrOfJoins, User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getMailFrom());
		if (AshesUtil.provided(join.getGameName())) {
			mail.setSubject("Joined game " + join.getGameName() + "successfully.");
		} else {
			mail.setSubject("Joined successfully.");
		}
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User\t\t").append(join.getUserName());
		body.append(System.lineSeparator());
		body.append("Homeplanet\t").append(AshesUtil.join(join.getHomePlanets(), ","));
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		if (nrOfJoins == 8) {
			body.append("This game will start now.");
		} else {
			body.append("This game has now ").append(nrOfJoins);
			body.append((nrOfJoins == 1) ? " player." : " players.");
		}
		mail.setBody(body.toString());
		return mail; 
	}
	
	private Mail createMailJoinRejected(User user, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getMailFrom());
		mail.setSubject("Join game rejected.");
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User  ").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name  ").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Email ").append(user.getEmail());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}
	
	private boolean createGame(List<Join> allJoins) {
		if (!AshesUtil.provided(allJoins) || (allJoins.size() < 8)) {
			return false;
		}
		List<String> userNames = buildListOfUserNames(allJoins);
		Game game = AshesOfEmpire.getInstance().getGameCache().create();
		for (int i = 0; i < userNames.size(); i++) {
			Player player = new Player(userNames.get(i), i + 1);
			game.addPlayer(player);
		}
		return true;
	}
	
	protected List<String> buildListOfUserNames(List<Join> allJoins) {
		List<String> userNames = new ArrayList<String>();
		List<Join> joins = new ArrayList<Join>();
		joins.addAll(allJoins);
		// find priority for each planet
		// choose randomly if multiple joins have the same priority 
		for (int planetNr = 1; planetNr <= 8; planetNr++) {
			int lowestPrio = Integer.MAX_VALUE;
			List<Integer> indices = new ArrayList<Integer>();
			for (int joinIndex = 0; joinIndex < joins.size(); joinIndex++) {
				List<Integer> homePlanets = joins.get(joinIndex).getHomePlanets();
				int priority = 0;
				for (int homePlanetIndex = 0; homePlanetIndex < homePlanets.size(); homePlanetIndex++) {
					if (homePlanets.get(homePlanetIndex) > planetNr) {
						priority++;
					}
					if (homePlanets.get(homePlanetIndex) == planetNr) {
						if (priority == lowestPrio) {
							indices.add(joinIndex);
						}
						if (priority < lowestPrio) {
							lowestPrio = priority;
							indices.clear();
							indices.add(joinIndex);
						}
						break;
					}
				}
			}
			int joinIndex = indices.get((indices.size() > 1) ? RANDOM.nextInt(indices.size()) : 0);
			userNames.add(joins.get(joinIndex).getUserName());
			joins.remove(joinIndex);
		}
		return userNames;
	}
	
}
