package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Report {

	private Map<Topic, List<Message>> fMessagesByTopic;

	/**
	 * 
	 */
	public Report() {
		fMessagesByTopic = new HashMap<Topic, List<Message>>();
	}
	
	/**
	 *
	 */
	public Report add(Message report) {
		if (report != null) { 
			List<Message> reports = fMessagesByTopic.get(report.getTopic());
			if (reports == null) {
				reports = new ArrayList<Message>();
				fMessagesByTopic.put(report.getTopic(), reports);
			}
			reports.add(report);
		}
		return this;
	}
	
	/**
	 * 
	 */
	public Report clear() {
		fMessagesByTopic.clear();
		return this;
	}
	
  // private final static String MAIL_PROPERTY = "mail.smtp.host";

  // private static Session session = null;
  // private static Vector all = null;

  /*
  public void sendMail(String address) {

	for (int i = 0; i < NR_PLAYERS; i++) {
	  Report report = players[i].getReport();
	  String subject =  "Ashes Of Empire Game " + nr + " Turn " + turn + " Player " +  i;
	  getPlayer(i).mailTo(session, subject, report.toString());
	}

	try {
	  if (user.getEmail() != null) {
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(AshesOfEmpire.getFromAddress()));
		msg.setSubject(subject);
		InternetAddress[] toAddresses = new InternetAddress[1];
		toAddresses[0] = new InternetAddress(user.getEmail());
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setContent(body, "text/plain");
		// or: msg.setText(report.toString());
		Transport.send(msg);
	  }
	} catch (MessagingException me) {
	  AshesOfEmpire.log("! Cannot mail report to " + user.getEmail());
	}

  }
  */
  	
  	/*
  	if (session == null) {
  	  Properties props = new Properties();
  	  if (master.getProperty(MAIL_PROPERTY) != null) {
  		props.setProperty(MAIL_PROPERTY, master.getProperty(MAIL_PROPERTY));
  	  } else {
  		props.setProperty(MAIL_PROPERTY, "localhost");
  	  }
  	  session = Session.getDefaultInstance(props, null);
  	}
  	*/
  
}