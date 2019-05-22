package com.balancedbytes.game.ashes;

public interface IAshesPropertyKey {
	
	// AshesOfEmpire
	String ASHES_DIR = "ashes.dir";
	
	// IMailManager
	String MAIL_MODE = "mail.mode";

	// MailManagerFiles
	String MAIL_DIR_IN = "mail.dir.in";
	String MAIL_DIR_OUT = "mail.dir.out";
	
	// MailManagerImap
	String MAIL_IMAP_HOST = "mail.imap.host";
	String MAIL_SMTP_HOST = "mail.smtp.host";
	String MAIL_FROM = "mail.from";
	String MAIL_USER = "mail.user";
	String MAIL_PASSWORD = "mail.password";

	// DbManager
	String DB_SERVER_DIR = "db.server.dir";
	String DB_URL = "db.url";
	String DB_USER = "db.user";
	String DB_PASSWORD = "db.password";
	String DB_JDBC_DRIVER = "db.jdbc.driver";
	String DB_SERVER_PORT = "db.server.port";
	
}
