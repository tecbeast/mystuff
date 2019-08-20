package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.mail.IMailManager;
import com.balancedbytes.game.ashes.mail.Mail;

/**
 *
 */
public class Report {
	
	private static Log LOG = LogFactory.getLog(Report.class);

	private Map<Topic, List<Message>> fMessagesByTopic;

	public Report() {
		fMessagesByTopic = new HashMap<Topic, List<Message>>();
	}
	
	public Report add(Message message) {
		if (message == null) {
			return null;
		}
		List<Message> messages = fMessagesByTopic.get(message.getTopic());
		if (messages == null) {
			messages = new ArrayList<Message>();
			fMessagesByTopic.put(message.getTopic(), messages);
		}
		messages.add(message);
		return this;
	}
	
	public List<Message> get(Topic topic) {
		if (topic == null) {
			return null;
		}
		return fMessagesByTopic.get(topic);
	}

	public Message getFirst(Topic topic) {
		return AshesUtil.firstElement(get(topic));
	}

	public Report clear() {
		fMessagesByTopic.clear();
		return this;
	}
	
	public Report sendMail(Game game, Player player) {
		if ((game == null) || (player == null)) {
			return null;
		}
		User user = AshesOfEmpire.getInstance().getUserCache().get(player.getUserName());
		if (user == null) {
			LOG.error("No user \"" + AshesUtil.print(player.getUserName()) + "\" found in userCache.");
			return null;
		}
		Mail mail = new Mail();
		mail.setTo(user.getEmail());
		StringBuilder subject = new StringBuilder();
		subject.append("Ashes Of Empire Game ").append(game.getGameNr());
		subject.append(" Turn ").append(game.getTurn());
		subject.append(" Player ").append(player.getPlayerNr());
		mail.setSubject(subject.toString());
		StringBuilder body = new StringBuilder();
		Message politics = getFirst(Topic.POLITICS);
		if (politics != null) {
			body.append(System.lineSeparator());
			body.append("=== POLITICS ===");
			body.append(System.lineSeparator());
			body.append(politics);
		}
		Message gnp = getFirst(Topic.GROSS_NATIONAL_PRODUCT);
		if (gnp != null) {
			body.append(System.lineSeparator());
			body.append("=== GROSS NATIONAL PRODUCT ===");
			body.append(System.lineSeparator());
			body.append(gnp);
		}
		Message turnSecret = getFirst(Topic.TURN_SECRET);
		if (turnSecret != null) {
			body.append(System.lineSeparator());
			body.append("=== TURN SECRET ===");
			body.append(System.lineSeparator());
			body.append("Next TurnSecret is ").append(AshesUtil.print(turnSecret.get(0)));
		}
		mail.setBody(body.toString());
		IMailManager mailManager = AshesOfEmpire.getInstance().getMailManager();
		mail.setFrom(mailManager.getMailFrom());
		mailManager.sendMail(mail);
		return this;
	}
 
}