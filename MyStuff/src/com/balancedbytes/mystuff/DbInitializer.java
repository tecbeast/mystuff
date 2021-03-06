package com.balancedbytes.mystuff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Create Tables in HSQLDB and fill with initial test data.
 */
public class DbInitializer {

	// these constants must match with the entries in WEB-INF/context.xml 
	private static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	private static final String DB_URL = "jdbc:hsqldb:file:/temp/mystuff";
	private static final String DB_USER = "SA";
	private static final String DB_PASSWORD = "";

	private Connection openConnection() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException cnfe) {
			throw new SQLException("JDBCDriver Class not found");
		}
		Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		connection.setAutoCommit(false);
		return connection;
	}

	private int dropTable(Statement statement, String table) throws SQLException {
		System.out.println("drop table " + table);
		StringBuilder sql = new StringBuilder();
		sql.append("DROP TABLE IF EXISTS ").append(table).append(";");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableCountries(Statement statement) throws SQLException {
		System.out.println("create table countries");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE countries (");
		sql.append("  code CHAR(2) NOT NULL,");
		sql.append("  name VARCHAR(80) NOT NULL,");
		sql.append("  PRIMARY KEY(code)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGames(Statement statement) throws SQLException {
		System.out.println("create table games");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE games (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  title VARCHAR(80) NOT NULL,");
		sql.append("  subtitle VARCHAR(80),");
		sql.append("  published_year INTEGER,");
		sql.append("  players_min TINYINT,");
		sql.append("  players_max TINYINT,");
		sql.append("  playtime_min TINYINT,");
		sql.append("  playtime_max TINYINT,");
		sql.append("  playtime_per_player BOOLEAN,");
		sql.append("  age_min TINYINT,");
		sql.append("  description VARCHAR(255),");
		sql.append("  rating TINYINT");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}
	
	private int createTableAuthors(Statement statement) throws SQLException {
		System.out.println("create table authors");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE authors (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  last_name VARCHAR(80) NOT NULL,");
		sql.append("  first_name VARCHAR(80) NOT NULL,");
		sql.append("  country_code CHAR(2) NOT NULL,");
		sql.append("  FOREIGN KEY(country_code) REFERENCES countries(code)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGameAuthors(Statement statement) throws SQLException {
		System.out.println("create table game_authors");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE game_authors (");
		sql.append("  game_id BIGINT NOT NULL,");
		sql.append("  author_id BIGINT NOT NULL,");
		sql.append("  PRIMARY KEY(game_id, author_id),");
		sql.append("  FOREIGN KEY(game_id) REFERENCES games(id),");
		sql.append("  FOREIGN KEY(author_id) REFERENCES authors(id)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTablePublishers(Statement statement) throws SQLException {
		System.out.println("create table publishers");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE publishers (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  name VARCHAR(80) NOT NULL,");
		sql.append("  country_code CHAR(2) NOT NULL,");
		sql.append("  FOREIGN KEY(country_code) REFERENCES countries(code)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGamePublishers(Statement statement) throws SQLException {
		System.out.println("create table game_publishers");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE game_publishers (");
		sql.append("  game_id BIGINT NOT NULL,");
		sql.append("  publisher_id BIGINT NOT NULL,");
		sql.append("  PRIMARY KEY(game_id, publisher_id),");
		sql.append("  FOREIGN KEY(game_id) REFERENCES games(id),");
		sql.append("  FOREIGN KEY(publisher_id) REFERENCES publishers(id)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableAwards(Statement statement) throws SQLException {
		System.out.println("create table awards");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE awards (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  name VARCHAR(80),");
		sql.append("  country_code CHAR(2) NOT NULL,");
		sql.append("  FOREIGN KEY(country_code) REFERENCES countries(code)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGameAwards(Statement statement) throws SQLException {
		System.out.println("create table game_awards");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE game_awards (");
		sql.append("  game_id BIGINT NOT NULL,");
		sql.append("  award_id BIGINT NOT NULL,");
		sql.append("  year INTEGER NOT NULL,");
		sql.append("  PRIMARY KEY(game_id, award_id, year),");
		sql.append("  FOREIGN KEY(game_id) REFERENCES games(id),");
		sql.append("  FOREIGN KEY(award_id) REFERENCES awards(id)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableImages(Statement statement) throws SQLException {
		System.out.println("create table images");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE images (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  showing VARCHAR(80) NOT NULL,");
		sql.append("  width INTEGER,");
		sql.append("  height INTEGER,");
		sql.append("  url VARCHAR(255) NOT NULL,");
		sql.append("  description VARCHAR(255)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGameImages(Statement statement) throws SQLException {
		System.out.println("create table game_images");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE game_images (");
		sql.append("  game_id BIGINT NOT NULL,");
		sql.append("  image_id BIGINT NOT NULL,");
		sql.append("  PRIMARY KEY(game_id, image_id),");
		sql.append("  FOREIGN KEY(game_id) REFERENCES games(id),");
		sql.append("  FOREIGN KEY(image_id) REFERENCES images(id)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableNotes(Statement statement) throws SQLException {
		System.out.println("create table notes");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE notes (");
		sql.append("  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,");
		sql.append("  timestamp DATETIME,");
		sql.append("  text VARCHAR(255)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int createTableGameNotes(Statement statement) throws SQLException {
		System.out.println("create table game_notes");
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE game_notes (");
		sql.append("  game_id BIGINT NOT NULL,");
		sql.append("  note_id BIGINT NOT NULL,");
		sql.append("  PRIMARY KEY(game_id, note_id),");
		sql.append("  FOREIGN KEY(game_id) REFERENCES games(id),");
		sql.append("  FOREIGN KEY(note_id) REFERENCES notes(id)");
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}
	
	private String quote(String text) {
		if (text == null) {
			return "null";
		}
		StringBuilder quotedText = new StringBuilder();
		quotedText.append("'").append(text).append("'");
		return quotedText.toString();
	}
	
	private int addCountry(
		Statement statement,
		String code,
		String name
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO countries (code,name) VALUES(");
		sql.append(quote(code)).append(",");
		sql.append(quote(name));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	
	private int fillTableCountries(Statement statement) throws SQLException {
		int total = 0;
		total += addCountry(statement, "AC", "Ascension");
		total += addCountry(statement, "AD", "Andorra");
		total += addCountry(statement, "AE", "Vereinigte Arabische Emirate");
		total += addCountry(statement, "AF", "Afghanistan");
		total += addCountry(statement, "AG", "Antigua und Barbuda");
		total += addCountry(statement, "AI", "Anguilla");
		total += addCountry(statement, "AL", "Albanien");
		total += addCountry(statement, "AM", "Armenien");
		total += addCountry(statement, "AO", "Angola");
		total += addCountry(statement, "AQ", "Antarktika");
		total += addCountry(statement, "AR", "Argentinien");
		total += addCountry(statement, "AS", "Amerikanisch-Samoa");
		total += addCountry(statement, "AT", "Österreich");
		total += addCountry(statement, "AU", "Australien");
		total += addCountry(statement, "AW", "Aruba");
		total += addCountry(statement, "AX", "Åland");
		total += addCountry(statement, "AZ", "Aserbaidschan");
		total += addCountry(statement, "BA", "Bosnien und Herzegowina");
		total += addCountry(statement, "BB", "Barbados");
		total += addCountry(statement, "BD", "Bangladesch");
		total += addCountry(statement, "BE", "Belgien");
		total += addCountry(statement, "BF", "Burkina Faso");
		total += addCountry(statement, "BG", "Bulgarien");
		total += addCountry(statement, "BH", "Bahrain");
		total += addCountry(statement, "BI", "Burundi");
		total += addCountry(statement, "BJ", "Benin");
		total += addCountry(statement, "BL", "Saint-Barth‚lemy");
		total += addCountry(statement, "BM", "Bermuda");
		total += addCountry(statement, "BN", "Brunei Darussalam");
		total += addCountry(statement, "BO", "Bolivien");
		total += addCountry(statement, "BQ", "Bonaire, Sint Eustatius und Saba (Niederlande)");
		total += addCountry(statement, "BR", "Brasilien");
		total += addCountry(statement, "BS", "Bahamas");
		total += addCountry(statement, "BT", "Bhutan");
		total += addCountry(statement, "BV", "Bouvetinsel");
		total += addCountry(statement, "BW", "Botswana");
		total += addCountry(statement, "BY", "Weiárussland");
		total += addCountry(statement, "BZ", "Belize");
		total += addCountry(statement, "CA", "Kanada");
		total += addCountry(statement, "CC", "Kokosinseln");
		total += addCountry(statement, "CD", "Kongo, Demokratische Republik");
		total += addCountry(statement, "CF", "Zentralafrikanische Republik");
		total += addCountry(statement, "CG", "Republik Kongo");
		total += addCountry(statement, "CH", "Schweiz");
		total += addCountry(statement, "CI", "Elfenbeinküste");
		total += addCountry(statement, "CK", "Cookinseln");
		total += addCountry(statement, "CL", "Chile");
		total += addCountry(statement, "CM", "Kamerun");
		total += addCountry(statement, "CN", "China");
		total += addCountry(statement, "CO", "Kolumbien");
		total += addCountry(statement, "CR", "Costa Rica");
		total += addCountry(statement, "CU", "Kuba");
		total += addCountry(statement, "CV", "Kap Verde");
		total += addCountry(statement, "CW", "Curaçao");
		total += addCountry(statement, "CX", "Weihnachtsinsel");
		total += addCountry(statement, "CY", "Zypern");
		total += addCountry(statement, "CZ", "Tschechien");
		total += addCountry(statement, "DE", "Deutschland");
		total += addCountry(statement, "DJ", "Dschibuti");
		total += addCountry(statement, "DK", "D„nemark");
		total += addCountry(statement, "DM", "Dominica");
		total += addCountry(statement, "DO", "Dominikanische Republik");
		total += addCountry(statement, "DZ", "Algerien");
		total += addCountry(statement, "EA", "Ceuta, Melilla");
		total += addCountry(statement, "EC", "Ecuador");
		total += addCountry(statement, "EE", "Estland");
		total += addCountry(statement, "EG", "Ägypten");
		total += addCountry(statement, "EH", "Westsahara");
		total += addCountry(statement, "ER", "Eritrea");
		total += addCountry(statement, "ES", "Spanien");
		total += addCountry(statement, "ET", "Äthiopien");
		total += addCountry(statement, "EU", "Europäische Union");
		total += addCountry(statement, "FI", "Finnland");
		total += addCountry(statement, "FJ", "Fidschi");
		total += addCountry(statement, "FK", "Falklandinseln");
		total += addCountry(statement, "FM", "Mikronesien");
		total += addCountry(statement, "FO", "Faröer");
		total += addCountry(statement, "FR", "Frankreich");
		total += addCountry(statement, "GA", "Gabun");
		total += addCountry(statement, "GB", "Großbritannien");
		total += addCountry(statement, "GD", "Grenada");
		total += addCountry(statement, "GE", "Georgien");
		total += addCountry(statement, "GF", "Französisch-Guayana");
		total += addCountry(statement, "GG", "Guernsey (Kanalinsel)");
		total += addCountry(statement, "GH", "Ghana");
		total += addCountry(statement, "GI", "Gibraltar");
		total += addCountry(statement, "GL", "Grönland");
		total += addCountry(statement, "GM", "Gambia");
		total += addCountry(statement, "GN", "Guinea");
		total += addCountry(statement, "GP", "Guadeloupe");
		total += addCountry(statement, "GQ", "Äquatorialguinea");
		total += addCountry(statement, "GR", "Griechenland");
		total += addCountry(statement, "GS", "Südgeorgien und die Südlichen Sandwichinseln");
		total += addCountry(statement, "GT", "Guatemala");
		total += addCountry(statement, "GU", "Guam");
		total += addCountry(statement, "GW", "Guinea-Bissau");
		total += addCountry(statement, "GY", "Guyana");
		total += addCountry(statement, "HK", "Hongkong");
		total += addCountry(statement, "HM", "Heard und McDonaldinseln");
		total += addCountry(statement, "HN", "Honduras");
		total += addCountry(statement, "HR", "Kroatien");
		total += addCountry(statement, "HT", "Haiti");
		total += addCountry(statement, "HU", "Ungarn");
		total += addCountry(statement, "IC", "Kanarische Inseln");
		total += addCountry(statement, "ID", "Indonesien");
		total += addCountry(statement, "IE", "Irland");
		total += addCountry(statement, "IL", "Israel");
		total += addCountry(statement, "IM", "Insel Man");
		total += addCountry(statement, "IN", "Indien");
		total += addCountry(statement, "IO", "Britisches Territorium im Indischen Ozean");
		total += addCountry(statement, "IQ", "Irak");
		total += addCountry(statement, "IR", "Iran");
		total += addCountry(statement, "IS", "Island");
		total += addCountry(statement, "IT", "Italien");
		total += addCountry(statement, "JE", "Jersey (Kanalinsel)");
		total += addCountry(statement, "JM", "Jamaika");
		total += addCountry(statement, "JO", "Jordanien");
		total += addCountry(statement, "JP", "Japan");
		total += addCountry(statement, "KE", "Kenia");
		total += addCountry(statement, "KG", "Kirgisistan");
		total += addCountry(statement, "KH", "Kambodscha");
		total += addCountry(statement, "KI", "Kiribati");
		total += addCountry(statement, "KM", "Komoren");
		total += addCountry(statement, "KN", "St. Kitts und Nevis");
		total += addCountry(statement, "KP", "Nordkorea");
		total += addCountry(statement, "KR", "Südkorea");
		total += addCountry(statement, "KW", "Kuwait");
		total += addCountry(statement, "KY", "Kaimaninseln");
		total += addCountry(statement, "KZ", "Kasachstan");
		total += addCountry(statement, "LA", "Laos");
		total += addCountry(statement, "LB", "Libanon");
		total += addCountry(statement, "LC", "St. Lucia");
		total += addCountry(statement, "LI", "Liechtenstein");
		total += addCountry(statement, "LK", "Sri Lanka");
		total += addCountry(statement, "LR", "Liberia");
		total += addCountry(statement, "LS", "Lesotho");
		total += addCountry(statement, "LT", "Litauen");
		total += addCountry(statement, "LU", "Luxemburg");
		total += addCountry(statement, "LV", "Lettland");
		total += addCountry(statement, "LY", "Libyen");
		total += addCountry(statement, "MA", "Marokko");
		total += addCountry(statement, "MC", "Monaco");
		total += addCountry(statement, "MD", "Moldawien");
		total += addCountry(statement, "ME", "Montenegro");
		total += addCountry(statement, "MF", "Saint-Martin (franz. Teil)");
		total += addCountry(statement, "MG", "Madagaskar");
		total += addCountry(statement, "MH", "Marshallinseln");
		total += addCountry(statement, "MK", "Mazedonien");
		total += addCountry(statement, "ML", "Mali");
		total += addCountry(statement, "MM", "Myanmar");
		total += addCountry(statement, "MN", "Mongolei");
		total += addCountry(statement, "MO", "Macau");
		total += addCountry(statement, "MP", "Nördliche Marianen");
		total += addCountry(statement, "MQ", "Martinique");
		total += addCountry(statement, "MR", "Mauretanien");
		total += addCountry(statement, "MS", "Montserrat");
		total += addCountry(statement, "MT", "Malta");
		total += addCountry(statement, "MU", "Mauritius");
		total += addCountry(statement, "MV", "Malediven");
		total += addCountry(statement, "MW", "Malawi");
		total += addCountry(statement, "MX", "Mexiko");
		total += addCountry(statement, "MY", "Malaysia");
		total += addCountry(statement, "MZ", "Mosambik");
		total += addCountry(statement, "NA", "Namibia");
		total += addCountry(statement, "NC", "Neukaledonien");
		total += addCountry(statement, "NE", "Niger");
		total += addCountry(statement, "NF", "Norfolkinsel");
		total += addCountry(statement, "NG", "Nigeria");
		total += addCountry(statement, "NI", "Nicaragua");
		total += addCountry(statement, "NL", "Niederlande");
		total += addCountry(statement, "NO", "Norwegen");
		total += addCountry(statement, "NP", "Nepal");
		total += addCountry(statement, "NR", "Nauru");
		total += addCountry(statement, "NU", "Niue");
		total += addCountry(statement, "NZ", "Neuseeland");
		total += addCountry(statement, "OM", "Oman");
		total += addCountry(statement, "PA", "Panama");
		total += addCountry(statement, "PE", "Peru");
		total += addCountry(statement, "PF", "Französisch-Polynesien");
		total += addCountry(statement, "PG", "Papua-Neuguinea");
		total += addCountry(statement, "PH", "Philippinen");
		total += addCountry(statement, "PK", "Pakistan");
		total += addCountry(statement, "PL", "Polen");
		total += addCountry(statement, "PM", "Saint-Pierre und Miquelon");
		total += addCountry(statement, "PN", "Pitcairninseln");
		total += addCountry(statement, "PR", "Puerto Rico");
		total += addCountry(statement, "PS", "Staat Palästina");
		total += addCountry(statement, "PT", "Portugal");
		total += addCountry(statement, "PW", "Palau");
		total += addCountry(statement, "PY", "Paraguay");
		total += addCountry(statement, "QA", "Katar");
		total += addCountry(statement, "RE", "Reunion");
		total += addCountry(statement, "RO", "Rumänien");
		total += addCountry(statement, "RS", "Serbien");
		total += addCountry(statement, "RU", "Russische Föderation");
		total += addCountry(statement, "RW", "Ruanda");
		total += addCountry(statement, "SA", "Saudi-Arabien");
		total += addCountry(statement, "SB", "Salomonen");
		total += addCountry(statement, "SC", "Seychellen");
		total += addCountry(statement, "SD", "Sudan");
		total += addCountry(statement, "SE", "Schweden");
		total += addCountry(statement, "SG", "Singapur");
		total += addCountry(statement, "SH", "St. Helena");
		total += addCountry(statement, "SI", "Slowenien");
		total += addCountry(statement, "SJ", "Svalbard und Jan Mayen");
		total += addCountry(statement, "SK", "Slowakei");
		total += addCountry(statement, "SL", "Sierra Leone");
		total += addCountry(statement, "SM", "San Marino");
		total += addCountry(statement, "SN", "Senegal");
		total += addCountry(statement, "SO", "Somalia");
		total += addCountry(statement, "SR", "Suriname");
		total += addCountry(statement, "SS", "Südsudan");
		total += addCountry(statement, "ST", "São Tomé und Príncipe");
		total += addCountry(statement, "SV", "El Salvador");
		total += addCountry(statement, "SX", "Sint Maarten (niederl. Teil)");
		total += addCountry(statement, "SY", "Syrien");
		total += addCountry(statement, "SZ", "Swasiland");
		total += addCountry(statement, "TC", "Turks- und Caicosinseln");
		total += addCountry(statement, "TD", "Tschad");
		total += addCountry(statement, "TF", "Französische Süd- und Antarktisgebiete");
		total += addCountry(statement, "TG", "Togo");
		total += addCountry(statement, "TH", "Thailand");
		total += addCountry(statement, "TJ", "Tadschikistan");
		total += addCountry(statement, "TK", "Tokelau");
		total += addCountry(statement, "TL", "Osttimor");
		total += addCountry(statement, "TM", "Turkmenistan");
		total += addCountry(statement, "TN", "Tunesien");
		total += addCountry(statement, "TO", "Tonga");
		total += addCountry(statement, "TR", "Türkei");
		total += addCountry(statement, "TT", "Trinidad und Tobago");
		total += addCountry(statement, "TV", "Tuvalu");
		total += addCountry(statement, "TW", "Taiwan");
		total += addCountry(statement, "TZ", "Tansania");
		total += addCountry(statement, "UA", "Ukraine");
		total += addCountry(statement, "UG", "Uganda");
		total += addCountry(statement, "US", "Vereinigte Staaten von Amerika");
		total += addCountry(statement, "UY", "Uruguay");
		total += addCountry(statement, "UZ", "Usbekistan");
		total += addCountry(statement, "VA", "Vatikanstadt");
		total += addCountry(statement, "VC", "St. Vincent und die Grenadinen");
		total += addCountry(statement, "VE", "Venezuela");
		total += addCountry(statement, "VG", "Britische Jungferninseln");
		total += addCountry(statement, "VI", "Amerikanische Jungferninseln");
		total += addCountry(statement, "VN", "Vietnam");
		total += addCountry(statement, "VU", "Vanuatu");
		total += addCountry(statement, "WF", "Wallis und Futuna");
		total += addCountry(statement, "WS", "Samoa");
		total += addCountry(statement, "XK", "Kosovo");
		total += addCountry(statement, "YE", "Jemen");
		total += addCountry(statement, "YT", "Mayotte");
		total += addCountry(statement, "ZA", "Südafrika");
		total += addCountry(statement, "ZM", "Sambia");
		total += addCountry(statement, "ZW", "Simbabwe");
		System.out.println("filled table countries with " + total + " entries");
		return total;
	}

	private int addGame(
		Statement statement,
		Integer id,
		String title,
		String subtitle,
		Integer publishedYear,
		Integer playersMin,
		Integer playersMax,
		Integer playtimeMin,
		Integer playtimeMax,
		Boolean playtimePerPlayer,
		Integer ageMin,
		String description,
		Integer rating
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO games (id,title,subtitle,published_year,players_min,players_max,");
		sql.append("playtime_min,playtime_max,playtime_per_player,age_min,description,rating) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(title)).append(",");
		sql.append(quote(subtitle)).append(",");
		sql.append(publishedYear).append(",");
		sql.append(playersMin).append(",");
		sql.append(playersMax).append(",");
		sql.append(playtimeMin).append(",");
		sql.append(playtimeMax).append(",");
		sql.append(playtimePerPlayer).append(",");
		sql.append(ageMin).append(",");
		sql.append(quote(description)).append(",");
		sql.append(rating);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}
	
	private int fillTableGames(Statement statement) throws SQLException {
		int total = 0;
		total += addGame(statement, 1, "Die Siedler von Catan", "Basisspiel", 1995, 3, 4, 60, null, false, 10, "Beschreibung folgt", null);
		total += addGame(statement, 2, "6 nimmt!", null, 1994, 2, 10, 45, null, false, 10, null, null);
		total += addGame(statement, 3, "Colt Express", "Basisspiel", 2014, 2, 6, 30, null, false, 10, null, null);
		System.out.println("filled table games with " + total + " entries");
		return total;
	}
	
	private int addAuthor(
		Statement statement,
		Integer id,
		String lastName,
		String firstName,
		String countryCode
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO authors (id,last_name,first_name,country_code) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(lastName)).append(",");
		sql.append(quote(firstName)).append(",");
		sql.append(quote(countryCode));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableAuthors(Statement statement) throws SQLException {
		int total = 0;
		total += addAuthor(statement, 1, "Teuber", "Klaus", "DE");
		total += addAuthor(statement, 2, "Kramer", "Wolfgang", "DE");
		total += addAuthor(statement, 3, "Raimbault", "Christophe", "FR");
		System.out.println("filled table authors with " + total + " entries");
		return total;
	}
	
	private int addAuthorToGame(
		Statement statement,
		Integer gameId,
		Integer authorId
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO game_authors (game_id,author_id) VALUES(");
		sql.append(gameId).append(",");
		sql.append(authorId);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableGameAuthors(Statement statement) throws SQLException {
		int total = 0;
		total += addAuthorToGame(statement, 1, 1);
		total += addAuthorToGame(statement, 2, 2);
		total += addAuthorToGame(statement, 3, 3);
		System.out.println("filled table game_authors with " + total + " entries");
		return total;
	}

	private int addPublisher(
		Statement statement,
		Integer id,
		String name,
		String countryCode
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO publishers (id,name,country_code) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(name)).append(",");
		sql.append(quote(countryCode));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTablePublishers(Statement statement) throws SQLException {
		int total = 0;
		total += addPublisher(statement, 1, "Kosmos", "DE");
		total += addPublisher(statement, 2, "Amigo", "DE");
		total += addPublisher(statement, 3, "Ludonaute", "FR");
		System.out.println("filled table publishers with " + total + " entries");
		return total;
	}

	private int addPublisherToGame(
		Statement statement,
		Integer gameId,
		Integer publisherId
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO game_publishers (game_id,publisher_id) VALUES(");
		sql.append(gameId).append(",");
		sql.append(publisherId);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableGamePublishers(Statement statement) throws SQLException {
		int total = 0;
		total += addPublisherToGame(statement, 1, 1);
		total += addPublisherToGame(statement, 2, 2);
		total += addPublisherToGame(statement, 3, 3);
		System.out.println("filled table game_publishers with " + total + " entries");
		return total;
	}
	
	private int addAward(
		Statement statement,
		Integer id,
		String name,
		String countryCode
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO awards (id,name,country_code) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(name)).append(",");
		sql.append(quote(countryCode));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableAwards(Statement statement) throws SQLException {
		int total = 0;
		total += addAward(statement, 1, "Spiel des Jahres", "DE");
		total += addAward(statement, 2, "Deutscher Spiele-Preis", "DE");
		total += addAward(statement, 3, "Auswahlliste Spiel des Jahres", "DE");
		System.out.println("filled table awards with " + total + " entries");
		return total;
	}

	private int addAwardToGame(
		Statement statement,
		Integer gameId,
		Integer awardId,
		Integer year
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO game_awards (game_id,award_id,year) VALUES(");
		sql.append(gameId).append(",");
		sql.append(awardId).append(",");
		sql.append(year);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableGameAwards(Statement statement) throws SQLException {
		int total = 0;
		total += addAwardToGame(statement, 1, 1, 1995);
		total += addAwardToGame(statement, 1, 2, 1995);
		total += addAwardToGame(statement, 2, 3, 1994);
		total += addAwardToGame(statement, 3, 1, 2015);
		System.out.println("filled table game_awards with " + total + " entries");
		return total;
	}
	
	private int addImage(
		Statement statement,
		Integer id,
		String showing,
		Integer width,
		Integer height,
		String url,
		String description
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO images (id,showing,width,height,url,description) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(showing)).append(",");
		sql.append(width).append(",");
		sql.append(height).append(",");
		sql.append(quote(url)).append(",");
		sql.append(quote(description));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableImages(Statement statement) throws SQLException {
		int total = 0;
		total += addImage(statement, 1, "box", 1067, 1500, "https://images-na.ssl-images-amazon.com/images/I/91ReTacJ0WL._SL1500_.jpg", "Box von 6 Nimmt (Amazon)");
		System.out.println("filled table images with " + total + " entries");
		return total;
	}

	private int addImageToGame(
		Statement statement,
		Integer gameId,
		Integer imageId
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO game_images (game_id,image_id) VALUES(");
		sql.append(gameId).append(",");
		sql.append(imageId);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableGameImages(Statement statement) throws SQLException {
		int total = 0;
		total += addImageToGame(statement, 2, 1);
		System.out.println("filled table game_images with " + total + " entries");
		return total;
	}
	
	private int addNote(
		Statement statement,
		Integer id,
		Timestamp timestamp,
		String text
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO notes (id,timestamp,text) VALUES(");
		sql.append(id).append(",");
		sql.append(quote(MyStuffUtil.toString(timestamp))).append(",");
		sql.append(quote(text));
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableNotes(Statement statement) throws SQLException {
		int total = 0;
		total += addNote(statement, 1, MyStuffUtil.toTimestamp("2016-10-24 23:04:00"), "Ein erster Kommentar.");
		System.out.println("filled table notes with " + total + " entries");
		return total;
	}

	private int addNoteToGame(
		Statement statement,
		Integer gameId,
		Integer noteId
	) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO game_notes (game_id,note_id) VALUES(");
		sql.append(gameId).append(",");
		sql.append(noteId);
		sql.append(");");
		return statement.executeUpdate(sql.toString());
	}

	private int fillTableGameNotes(Statement statement) throws SQLException {
		int total = 0;
		total += addNoteToGame(statement, 1, 1);
		System.out.println("filled table game_notes with " + total + " entries");
		return total;
	}
	
	public void init(boolean testData) throws SQLException {

		Connection connection = openConnection();
		Statement statement = connection.createStatement();

		dropTable(statement, "game_notes");
		dropTable(statement, "notes");
		dropTable(statement, "game_images");
		dropTable(statement, "images");
		dropTable(statement, "game_awards");
		dropTable(statement, "awards");
		dropTable(statement, "game_publishers");
		dropTable(statement, "publishers");
		dropTable(statement, "game_authors");
		dropTable(statement, "authors");
		dropTable(statement, "games");
		dropTable(statement, "countries");
		connection.commit();

		createTableCountries(statement);
		createTableGames(statement);
		createTableAuthors(statement);
		createTableGameAuthors(statement);
		createTablePublishers(statement);
		createTableGamePublishers(statement);
		createTableAwards(statement);
		createTableGameAwards(statement);
		createTableImages(statement);
		createTableGameImages(statement);
		createTableNotes(statement);
		createTableGameNotes(statement);
		connection.commit();
		
		if (testData) {
			
			fillTableCountries(statement);
			connection.commit();
			
			fillTableGames(statement);
			fillTableAuthors(statement);
			fillTablePublishers(statement);
			fillTableAwards(statement);
			fillTableImages(statement);
			fillTableNotes(statement);
			connection.commit();

			fillTableGameAuthors(statement);
			fillTableGamePublishers(statement);
			fillTableGameAwards(statement);
			fillTableGameImages(statement);
			fillTableGameNotes(statement);
			connection.commit();
			
		}
		
		connection.close();

		System.out.println("done.");
		
	}
	
	public static void main(String[] args) {
		try {
			new DbInitializer().init(true);
		} catch (Exception any) {
			any.printStackTrace();
		}
	}

}
