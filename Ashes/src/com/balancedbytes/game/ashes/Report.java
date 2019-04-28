package com.balancedbytes.game.ashes;

import java.util.Vector;

/**
 *
 */
public class Report {

  public final static int    NR_PHASES = 12;
  
  public final static int     POLITICS = 0;
  public final static int      PLANETS = 1;
  public final static int       FLEETS = 2;
  public final static int      BATTLES = 3;
  public final static int      REVOLTS = 6;
  public final static int        CARGO = 7;
  public final static int INTELLIGENCE = 8;
  public final static int     MESSAGES = 9;

  private final static String[] title = {
  	"--- Politics ---",
  	"--- Planets ---",
  	"--- Fleets ---",
  	"--- Battles ---",
  	"title",
  	"title",
  	"--- Revolts ---",
  	"--- Cargo ---",
  	"--- Intelligence Report ---",
  	"--- Messages ---",
  	"title",
  	"title"
  };
  // private final static String MAIL_PROPERTY = "mail.smtp.host";

  private static StarRealms master = null;

  // private static Session session = null;
  private static Vector all = null;

  private StringBuffer[] phase = null;

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

  /**
   * Reports constructor.
   */
  public Report() {
  	phase = new StringBuffer[NR_PHASES];
  	
  	if (master == null) { master = StarRealms.getInstance(); }
  	
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
  	
  	if (all == null) { all = new Vector(); }
  	all.addElement(this);
  }

  /**
   *
   */
  public void add(int phaseNr, String text) {
  	if ((phaseNr >= 0) && (phaseNr < NR_PHASES) && (text != null)) {
  	  if (phase[phaseNr] == null) { phase[phaseNr] = new StringBuffer(); }
  	  phase[phaseNr].append(text);
  	}
  }

  /**
   *
   */
  public void add(int phaseNr, StringBuffer buffer) {
  	if ((phaseNr >= 0) && (phaseNr < NR_PHASES) && (buffer != null)) {
  	  if (phase[phaseNr] == null) { phase[phaseNr] = new StringBuffer(); }
  	  phase[phaseNr].append(buffer);
  	}
  }

  /**
   *
   */
  public static void addAll(int phaseNr, String text) {
  	if ((phaseNr >= 0) && (phaseNr < NR_PHASES) && (text != null)) {
  	  Enumeration enum = all.elements();
  	  while (enum.hasMoreElements()) {
  			Report report = (Report)enum.nextElement();
  			report.add(phaseNr, text);
  	  }
  	}
  }

  /**
   *
   */
  public static void addAll(int phaseNr, StringBuffer buffer) {
  	if ((phaseNr >= 0) && (phaseNr < NR_PHASES) && (buffer != null)) {
  	  Enumeration enum = all.elements();
  	  while (enum.hasMoreElements()) {
  			Report report = (Report)enum.nextElement();
  			report.add(phaseNr, buffer);
  	  }
  	}
  }

  /**
   * Cleans up and remove this Report from the list of all Reports.
   */
  public void cleanUp() {
  	all.removeElement(this);
  	if (all.size() == 0) { all = null; }
  }

  /**
   *
   */
  public String toString() {
  	StringBuffer buffer = new StringBuffer();
  
  	for (int i = 0; i < NR_PHASES; i++) {
  	  buffer.append(title[i]); buffer.append('\n');
  	  if (phase[i] != null) {
  		buffer.append(phase[i]); buffer.append('\n');
  	  }
  	}
  
  	return buffer.toString();
  }
  
}