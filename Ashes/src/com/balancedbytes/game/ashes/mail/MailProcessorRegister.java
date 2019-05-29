package com.balancedbytes.game.ashes.mail;

import java.util.Date;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.balancedbytes.game.ashes.AshesOfEmpire;
import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.model.User;
import com.balancedbytes.game.ashes.model.UserCache;

public class MailProcessorRegister {

	public static final String REGISTER = "register";
	
	private static final String USER = "user";
	private static final String NAME = "name";
	private static final String EMAIL = "email";
	private static final String SECRET = "secret";
	
	protected MailProcessorRegister() {
		super();
	}
	
	public void process(Mail mail) {
		
		if ((mail == null) || !AshesUtil.provided(mail.getSubject())) {
			return;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			new String[] { REGISTER, USER }, mail.getSubject()
		);
		
		if (!tokenMap.containsKey(REGISTER)) {
			return;
		}

		IMailManager mailManager = AshesOfEmpire.getInstance().getMailManager();
		mailManager.sendMail(processUserRegistration(tokenMap.get(USER), mail.getBody()));
		
	}
	
	private Mail processUserRegistration(String userName, String mailBody) {

		if (!AshesUtil.provided(userName) || !AshesUtil.provided(mailBody)) {
			return null;
		}
		
		Map<String, String> tokenMap = MailProcessorUtil.buildTokenMap(
			new String[] { NAME, EMAIL, SECRET }, mailBody
		);
		
		UserCache userCache = AshesOfEmpire.getInstance().getUserCache();
		User user = userCache.get(userName);
		
		if (user != null) {
			
			if (user.getRegistered() != null) {
				return createMailRegistrationRejected(user, "ERROR: This username is in use.");
			}

			if (AshesUtil.provided(tokenMap.get(SECRET))) {

				if (!user.getSecret().equals(tokenMap.get(SECRET))) {
					return createMailRegistrationRejected(user, "ERROR: Invalid secret.");
				
				} else {
					user.setSecret(null);
					user.setLastProcessed(new Date());
					user.setRegistered(user.getLastProcessed());
					user.setModified(true);
					return createMailUserRegistration(user);
				}
				
			} else  {

				if (checkValues(tokenMap)) {
					user.setRealName(tokenMap.get(NAME));
					user.setEmail(tokenMap.get(EMAIL));
					user.setSecret(AshesOfEmpire.getInstance().generateSecret());
					user.setLastProcessed(new Date());
					user.setModified(true);
					return createMailUserVerification(user);
				}
				
			}
			
		} else {

			if (checkValues(tokenMap)) {
				user = new User();
				user.setUserName(userName);
				userCache.add(user);
				user.setRealName(tokenMap.get(NAME));
				user.setEmail(tokenMap.get(EMAIL));
				user.setSecret(AshesOfEmpire.getInstance().generateSecret());
				user.setLastProcessed(new Date());
				user.setModified(true);
				return createMailUserVerification(user);
			}
			
		}
		
		return null;
		
	}
	
	private boolean checkValues(Map<String, String> tokenMap) {
		if (AshesUtil.provided(tokenMap.get(NAME)) && AshesUtil.provided(tokenMap.get(EMAIL))) {
			try {
				InternetAddress.parse(tokenMap.get(EMAIL), true);  // check email 
			} catch (AddressException ae) {
				return false;
			}
			return true;
		}
		return false;
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

	private Mail createMailRegistrationRejected(User user, String error) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		mail.setSubject("User registration rejected.");
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

	private Mail createMailUserVerification(User user) {
		Mail mail = new Mail();
		mail.setFrom(AshesOfEmpire.getInstance().getMailManager().getEmailAddress());
		StringBuilder subject = new StringBuilder();
		subject.append("Register User ").append(user.getUserName());
		subject.append(" - Please verify");
		mail.setSubject(subject.toString());
		mail.setTo(user.getEmail());
		StringBuilder body = new StringBuilder();
		body.append("Name   \"").append(user.getRealName()).append("\"");
		body.append(System.lineSeparator());
		body.append("Secret ").append(user.getSecret());
		body.append(System.lineSeparator());
		body.append(System.lineSeparator());
		body.append("To finish your registration simply respond to this email.");
		body.append(System.lineSeparator());
		body.append("The body of that mail needs to contain the secret shown above.");
		body.append(System.lineSeparator());
		mail.setBody(body.toString());
		return mail;
	}

}
