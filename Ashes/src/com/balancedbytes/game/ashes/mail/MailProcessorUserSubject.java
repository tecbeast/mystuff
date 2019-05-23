package com.balancedbytes.game.ashes.mail;

import java.util.Date;
import java.util.Scanner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.TurnSecretGenerator;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorUserSubject {
	
	protected MailProcessorUserSubject() {
		super();
	}
	
	public void process(Mail mail) {
		if ((mail == null) || !AshesUtil.isProvided(mail.getSubject())) {
			return;
		}
		Mail result = null;
		try (Scanner subjectScanner = new Scanner(mail.getSubject())) {
			if (!subjectScanner.hasNext() || !subjectScanner.next().equalsIgnoreCase("user")) {
				return;
			}
			String action = subjectScanner.hasNext() ? subjectScanner.next() : null;
			if ("register".equalsIgnoreCase(action)) {
				if (!subjectScanner.hasNext()) {
					return;
				}
				result = processUserRegistration(subjectScanner.next(), mail.getBody());
			}
		}
		AshesOfEmpire.getInstance().getMailManager().sendMail(result);
	}
	
	private Mail processUserRegistration(String userName, String mailBody) {

		if (!AshesUtil.isProvided(userName) || !AshesUtil.isProvided(mailBody)) {
			return null;
		}
		
		String email = null;
		String secret = null;		
		String realName = null;
		
		try (Scanner scanner = new Scanner(mailBody)) {
			while (scanner.hasNextLine()) {
				if (scanner.hasNext()) {
					String command = scanner.next();
					if (scanner.hasNext()) {
						String value = scanner.nextLine().trim();
						if ("name".equalsIgnoreCase(command)) {
							realName = value;
						}
						if ("email".equalsIgnoreCase(command)) {
							email = value;
						}
						if ("secret".equalsIgnoreCase(command)) {
							secret = value;
						}
					}
				}
			}
		}
		
		UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		User user = userCache.get(userName);
		
		if (user != null) {
			if (AshesUtil.isProvided(user.getSecret()) && user.getSecret().equals(secret)) {
				user.setSecret(null);
				user.setRegistered(new Date());
				user.setModified(true);
				return createMailUserRegistration(user);
			}
				
		} else {
			if (AshesUtil.isProvided(realName) && AshesUtil.isProvided(email)) {
				try {
					InternetAddress.parse(email, true);  // check email 
				} catch (AddressException ae) {
					return null;
				}
				user = new User();
				user.setUserName(userName);
				user.setRealName(realName);
				user.setEmail(email);
				user.setSecret(TurnSecretGenerator.generateSecret());
				user.setModified(true);
				return createMailUserVerification(user);
			}
		}
		
		return null;
		
	}
	
	private Mail createMailUserRegistration(User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject("User registration successful.");
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User  ").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name  ").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Email ").append(user.getEmail());
		body.append(System.lineSeparator());
		mail.setBody(body.toString());
		return mail;
	}
	
	private Mail createMailUserVerification(User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject("Please verify user registration.");
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User   ").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name   ").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Secret ").append(user.getSecret());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append("To finish your registration please respond to this email with a subject of");
		body.append(System.lineSeparator());
		body.append("user register ").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append("The content of that mail needs to be a single line with the given secret.");
		body.append(System.lineSeparator());
		body.append("(Simply copy the line above including the keyword \"Secret\")");
		mail.setBody(body.toString());
		return mail;
	}

}
