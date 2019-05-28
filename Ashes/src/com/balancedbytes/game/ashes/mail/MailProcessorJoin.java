package com.balancedbytes.game.ashes.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.Join;
import com.balancedbytes.game.ashes.model.JoinCache;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorJoin {

	public static final String JOIN = "join";
	
	private static final String GAME = "game";
	private static final String USER = "user";
	private static final String HOMEPLANET = "homeplanet";
	
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
		
		if (user == null) {
			return null;
		}
		
		JoinCache joinCache = AshesOfEmpire.getInstance().getJoinCache();
		Join join = joinCache.getByUserName(userName);
		
		if (join == null) {
			join = new Join();
			join.setUserName(userName);
			joinCache.add(join);
		}
		
		join.setGameName(gameName);
		String homePlanets = tokenMap.get(HOMEPLANET);
		if (AshesUtil.provided(homePlanets)) {
			join.setHomePlanets(AshesUtil.splitIntegerList(homePlanets, ","));
		} else {
			join.setHomePlanets(new ArrayList<Integer>());
		}
		join.setJoined(new Date());
		join.setModified(true);
		
		List<Join> allJoins = joinCache.getByGameName(gameName); 
		
		return createMailJoined(join, allJoins.size(), user);
		
	}
	
	private Mail createMailJoined(Join join, int nrOfJoins, User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
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
		body.append("This game has currently " + nrOfJoins + " players.");
		mail.setBody(body.toString());
		return mail;
	}

}
