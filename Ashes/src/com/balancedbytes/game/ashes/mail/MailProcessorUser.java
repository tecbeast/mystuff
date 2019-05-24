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

public class MailProcessorUser {
	
	protected MailProcessorUser() {
		super();
	}
	
	public Mail process(Mail mail) {
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return null;
		}
		try (Scanner subjectScanner = new Scanner(mail.getSubject())) {
			if (!subjectScanner.hasNext() || !subjectScanner.next().equalsIgnoreCase("user")) {
				return null;
			}
			String action = subjectScanner.hasNext() ? subjectScanner.next() : null;
			if ("register".equalsIgnoreCase(action)) {
				if (!subjectScanner.hasNext()) {
					return null;
				}
				return processUserRegistration(subjectScanner.next(), mail.getBody());
			}
		}
		return null;
	}
	
	private Mail processUserRegistration(String userName, String mailBody) {

		if (!AshesUtil.provided(userName) || !AshesUtil.provided(mailBody)) {
			return null;
		}
		
		// TODO: improvement (support quotes)
		//		String rx = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
		//		Scanner scanner = new Scanner("P 160 SomethingElse \"A string literal\" end");
		//		System.out.println(scanner.findInLine(rx)); // => P
		//		System.out.println(scanner.findInLine(rx)); // => 160
		//		System.out.println(scanner.findInLine(rx)); // => SomethingElse
		//		System.out.println(scanner.findInLine(rx)); // => "A string literal"
		//		System.out.println(scanner.findInLine(rx)); // => end
		
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
			
			if (user.getRegistered() != null) {
				return createMailRegistrationRejected(user, "ERROR: This username is in use.");
			}

			if (AshesUtil.provided(secret)) {

				if (!user.getSecret().equals(secret)) {
					return createMailRegistrationRejected(user, "ERROR: Invalid secret.");
				
				} else {
					user.setSecret(null);
					user.setLastProcessed(new Date());
					user.setRegistered(user.getLastProcessed());
					user.setModified(true);
					return createMailUserRegistration(user);
				}
				
			} // else treat as new user
			
		} else {

			if (AshesUtil.provided(realName) && AshesUtil.provided(email)) {
				try {
					InternetAddress.parse(email, true);  // check email 
				} catch (AddressException ae) {
					return createMailRegistrationRejected(user, "ERROR: Invalid email address.");
				}
				if (user == null) {
					user = new User();
					user.setUserName(userName);
					userCache.add(user);
				}
				user.setRealName(realName);
				user.setEmail(email);
				user.setSecret(TurnSecretGenerator.generateSecret());
				user.setLastProcessed(new Date());
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
		body.append("User\t").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name\t").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Email\t").append(user.getEmail());
		body.append(System.lineSeparator());
		mail.setBody(body.toString());
		return mail;
	}

	private Mail createMailRegistrationRejected(User user, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject("User registration rejected.");
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User\t").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name\t").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Email\t").append(user.getEmail());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append(error);
		mail.setBody(body.toString());
		return mail;
	}

	private Mail createMailUserVerification(User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject("Please verify user registration.");
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("User\t").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("Name\t").append(user.getRealName());
		body.append(System.lineSeparator());
		body.append("Secret\t").append(user.getSecret());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append("To finish your registration please respond to this email with a subject of");
		body.append(System.lineSeparator());
		body.append("user register ").append(user.getUserName());
		body.append(System.lineSeparator());
		body.append("The body of that mail needs to contain the given secret.");
		body.append(System.lineSeparator());
		body.append("(Simply copy the line above including the keyword \"Secret\")");
		mail.setBody(body.toString());
		return mail;
	}

}
