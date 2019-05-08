package com.balancedbytes.game.ashes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Report {

	private Map<ReportSection, List<String>> fContentBySection;

	/**
	 * 
	 */
	public Report() {
		fContentBySection = new HashMap<ReportSection, List<String>>();
	}
	
	/**
	 *
	 */
	public void add(ReportSection section, String line) {
		List<String> content = fContentBySection.get(section);
		if (content == null) {
			content = new ArrayList<String>();
			fContentBySection.put(section, content);
		}
		content.add(line);
	}
	
	/**
	 * 
	 */
	public void clear() {
		fContentBySection.clear();
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