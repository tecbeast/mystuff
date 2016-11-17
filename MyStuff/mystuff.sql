
CREATE DATABASE mystuff;
USE mystuff;

CREATE TABLE countries (
	code CHAR(2) NOT NULL,
	name VARCHAR(80) NOT NULL,
	PRIMARY KEY(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE games (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(80) NOT NULL,
	published_year INTEGER,
	players_min TINYINT,
	players_max TINYINT,
	playtime_min TINYINT,
	playtime_max TINYINT,
	playtime_per_player BOOLEAN,
	age_min TINYINT,
	description VARCHAR(255),
	rating TINYINT,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE authors (
	id BIGINT NOT NULL AUTO_INCREMENT,
	last_name VARCHAR(80) NOT NULL,
	first_name VARCHAR(80) NOT NULL,
	country_code CHAR(2) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(country_code) REFERENCES countries(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE game_authors (
	game_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	PRIMARY KEY(game_id, author_id),
	FOREIGN KEY(game_id) REFERENCES games(id),
	FOREIGN KEY(author_id) REFERENCES authors(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE publishers (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(80) NOT NULL,
	country_code CHAR(2) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(country_code) REFERENCES countries(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE game_publishers (
	game_id BIGINT NOT NULL,
	publisher_id BIGINT NOT NULL,
	PRIMARY KEY(game_id, publisher_id),
	FOREIGN KEY(game_id) REFERENCES games(id),
	FOREIGN KEY(publisher_id) REFERENCES publishers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE awards (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(80),
	country_code CHAR(2) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(country_code) REFERENCES countries(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE game_awards (
	game_id BIGINT NOT NULL,
	award_id BIGINT NOT NULL,
	year INTEGER NOT NULL,
	PRIMARY KEY(game_id, award_id, year),
	FOREIGN KEY(game_id) REFERENCES games(id),
	FOREIGN KEY(award_id) REFERENCES awards(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE images (
	id BIGINT NOT NULL AUTO_INCREMENT,
	role VARCHAR(40) NOT NULL,
	width INTEGER,
	height INTEGER,
	url VARCHAR(255) NOT NULL,
	description VARCHAR(120),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE game_images (
	game_id BIGINT NOT NULL,
	image_id BIGINT NOT NULL,
	PRIMARY KEY(game_id, image_id),
	FOREIGN KEY(game_id) REFERENCES games(id),
	FOREIGN KEY(image_id) REFERENCES images(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE notes (
	id BIGINT NOT NULL AUTO_INCREMENT,
	timestamp DATETIME,
	text VARCHAR(255),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE game_notes (
	game_id BIGINT NOT NULL,
	note_id BIGINT NOT NULL,
	PRIMARY KEY(game_id, note_id),
	FOREIGN KEY(game_id) REFERENCES games(id),
	FOREIGN KEY(note_id) REFERENCES notes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* Table countries: code, name */
INSERT INTO countries VALUES('AC','Ascension');
INSERT INTO countries VALUES('AD','Andorra');
INSERT INTO countries VALUES('AE','Vereinigte Arabische Emirate');
INSERT INTO countries VALUES('AF','Afghanistan');
INSERT INTO countries VALUES('AG','Antigua und Barbuda');
INSERT INTO countries VALUES('AI','Anguilla');
INSERT INTO countries VALUES('AL','Albanien');
INSERT INTO countries VALUES('AM','Armenien');
INSERT INTO countries VALUES('AO','Angola');
INSERT INTO countries VALUES('AQ','Antarktika');
INSERT INTO countries VALUES('AR','Argentinien');
INSERT INTO countries VALUES('AS','Amerikanisch-Samoa');
INSERT INTO countries VALUES('AT','Österreich');
INSERT INTO countries VALUES('AU','Australien');
INSERT INTO countries VALUES('AW','Aruba');
INSERT INTO countries VALUES('AX','Åland');
INSERT INTO countries VALUES('AZ','Aserbaidschan');
INSERT INTO countries VALUES('BA','Bosnien und Herzegowina');
INSERT INTO countries VALUES('BB','Barbados');
INSERT INTO countries VALUES('BD','Bangladesch');
INSERT INTO countries VALUES('BE','Belgien');
INSERT INTO countries VALUES('BF','Burkina Faso');
INSERT INTO countries VALUES('BG','Bulgarien');
INSERT INTO countries VALUES('BH','Bahrain');
INSERT INTO countries VALUES('BI','Burundi');
INSERT INTO countries VALUES('BJ','Benin');
INSERT INTO countries VALUES('BL','Saint-Barth‚lemy');
INSERT INTO countries VALUES('BM','Bermuda');
INSERT INTO countries VALUES('BN','Brunei Darussalam');
INSERT INTO countries VALUES('BO','Bolivien');
INSERT INTO countries VALUES('BQ','Bonaire, Sint Eustatius und Saba (Niederlande)');
INSERT INTO countries VALUES('BR','Brasilien');
INSERT INTO countries VALUES('BS','Bahamas');
INSERT INTO countries VALUES('BT','Bhutan');
INSERT INTO countries VALUES('BV','Bouvetinsel');
INSERT INTO countries VALUES('BW','Botswana');
INSERT INTO countries VALUES('BY','Weiárussland');
INSERT INTO countries VALUES('BZ','Belize');
INSERT INTO countries VALUES('CA','Kanada');
INSERT INTO countries VALUES('CC','Kokosinseln');
INSERT INTO countries VALUES('CD','Kongo, Demokratische Republik');
INSERT INTO countries VALUES('CF','Zentralafrikanische Republik');
INSERT INTO countries VALUES('CG','Republik Kongo');
INSERT INTO countries VALUES('CH','Schweiz');
INSERT INTO countries VALUES('CI','Elfenbeinküste');
INSERT INTO countries VALUES('CK','Cookinseln');
INSERT INTO countries VALUES('CL','Chile');
INSERT INTO countries VALUES('CM','Kamerun');
INSERT INTO countries VALUES('CN','China');
INSERT INTO countries VALUES('CO','Kolumbien');
INSERT INTO countries VALUES('CR','Costa Rica');
INSERT INTO countries VALUES('CU','Kuba');
INSERT INTO countries VALUES('CV','Kap Verde');
INSERT INTO countries VALUES('CW','Curaçao');
INSERT INTO countries VALUES('CX','Weihnachtsinsel');
INSERT INTO countries VALUES('CY','Zypern');
INSERT INTO countries VALUES('CZ','Tschechien');
INSERT INTO countries VALUES('DE','Deutschland');
INSERT INTO countries VALUES('DJ','Dschibuti');
INSERT INTO countries VALUES('DK','D„nemark');
INSERT INTO countries VALUES('DM','Dominica');
INSERT INTO countries VALUES('DO','Dominikanische Republik');
INSERT INTO countries VALUES('DZ','Algerien');
INSERT INTO countries VALUES('EA','Ceuta, Melilla');
INSERT INTO countries VALUES('EC','Ecuador');
INSERT INTO countries VALUES('EE','Estland');
INSERT INTO countries VALUES('EG','Ägypten');
INSERT INTO countries VALUES('EH','Westsahara');
INSERT INTO countries VALUES('ER','Eritrea');
INSERT INTO countries VALUES('ES','Spanien');
INSERT INTO countries VALUES('ET','Äthiopien');
INSERT INTO countries VALUES('EU','Europäische Union');
INSERT INTO countries VALUES('FI','Finnland');
INSERT INTO countries VALUES('FJ','Fidschi');
INSERT INTO countries VALUES('FK','Falklandinseln');
INSERT INTO countries VALUES('FM','Mikronesien');
INSERT INTO countries VALUES('FO','Faröer');
INSERT INTO countries VALUES('FR','Frankreich');
INSERT INTO countries VALUES('GA','Gabun');
INSERT INTO countries VALUES('GB','Großbritannien');
INSERT INTO countries VALUES('GD','Grenada');
INSERT INTO countries VALUES('GE','Georgien');
INSERT INTO countries VALUES('GF','Französisch-Guayana');
INSERT INTO countries VALUES('GG','Guernsey (Kanalinsel)');
INSERT INTO countries VALUES('GH','Ghana');
INSERT INTO countries VALUES('GI','Gibraltar');
INSERT INTO countries VALUES('GL','Grönland');
INSERT INTO countries VALUES('GM','Gambia');
INSERT INTO countries VALUES('GN','Guinea');
INSERT INTO countries VALUES('GP','Guadeloupe');
INSERT INTO countries VALUES('GQ','Äquatorialguinea');
INSERT INTO countries VALUES('GR','Griechenland');
INSERT INTO countries VALUES('GS','Südgeorgien und die Südlichen Sandwichinseln');
INSERT INTO countries VALUES('GT','Guatemala');
INSERT INTO countries VALUES('GU','Guam');
INSERT INTO countries VALUES('GW','Guinea-Bissau');
INSERT INTO countries VALUES('GY','Guyana');
INSERT INTO countries VALUES('HK','Hongkong');
INSERT INTO countries VALUES('HM','Heard und McDonaldinseln');
INSERT INTO countries VALUES('HN','Honduras');
INSERT INTO countries VALUES('HR','Kroatien');
INSERT INTO countries VALUES('HT','Haiti');
INSERT INTO countries VALUES('HU','Ungarn');
INSERT INTO countries VALUES('IC','Kanarische Inseln');
INSERT INTO countries VALUES('ID','Indonesien');
INSERT INTO countries VALUES('IE','Irland');
INSERT INTO countries VALUES('IL','Israel');
INSERT INTO countries VALUES('IM','Insel Man');
INSERT INTO countries VALUES('IN','Indien');
INSERT INTO countries VALUES('IO','Britisches Territorium im Indischen Ozean');
INSERT INTO countries VALUES('IQ','Irak');
INSERT INTO countries VALUES('IR','Iran');
INSERT INTO countries VALUES('IS','Island');
INSERT INTO countries VALUES('IT','Italien');
INSERT INTO countries VALUES('JE','Jersey (Kanalinsel)');
INSERT INTO countries VALUES('JM','Jamaika');
INSERT INTO countries VALUES('JO','Jordanien');
INSERT INTO countries VALUES('JP','Japan');
INSERT INTO countries VALUES('KE','Kenia');
INSERT INTO countries VALUES('KG','Kirgisistan');
INSERT INTO countries VALUES('KH','Kambodscha');
INSERT INTO countries VALUES('KI','Kiribati');
INSERT INTO countries VALUES('KM','Komoren');
INSERT INTO countries VALUES('KN','St. Kitts und Nevis');
INSERT INTO countries VALUES('KP','Nordkorea');
INSERT INTO countries VALUES('KR','Südkorea');
INSERT INTO countries VALUES('KW','Kuwait');
INSERT INTO countries VALUES('KY','Kaimaninseln');
INSERT INTO countries VALUES('KZ','Kasachstan');
INSERT INTO countries VALUES('LA','Laos');
INSERT INTO countries VALUES('LB','Libanon');
INSERT INTO countries VALUES('LC','St. Lucia');
INSERT INTO countries VALUES('LI','Liechtenstein');
INSERT INTO countries VALUES('LK','Sri Lanka');
INSERT INTO countries VALUES('LR','Liberia');
INSERT INTO countries VALUES('LS','Lesotho');
INSERT INTO countries VALUES('LT','Litauen');
INSERT INTO countries VALUES('LU','Luxemburg');
INSERT INTO countries VALUES('LV','Lettland');
INSERT INTO countries VALUES('LY','Libyen');
INSERT INTO countries VALUES('MA','Marokko');
INSERT INTO countries VALUES('MC','Monaco');
INSERT INTO countries VALUES('MD','Moldawien');
INSERT INTO countries VALUES('ME','Montenegro');
INSERT INTO countries VALUES('MF','Saint-Martin (franz. Teil)');
INSERT INTO countries VALUES('MG','Madagaskar');
INSERT INTO countries VALUES('MH','Marshallinseln');
INSERT INTO countries VALUES('MK','Mazedonien');
INSERT INTO countries VALUES('ML','Mali');
INSERT INTO countries VALUES('MM','Myanmar');
INSERT INTO countries VALUES('MN','Mongolei');
INSERT INTO countries VALUES('MO','Macau');
INSERT INTO countries VALUES('MP','Nördliche Marianen');
INSERT INTO countries VALUES('MQ','Martinique');
INSERT INTO countries VALUES('MR','Mauretanien');
INSERT INTO countries VALUES('MS','Montserrat');
INSERT INTO countries VALUES('MT','Malta');
INSERT INTO countries VALUES('MU','Mauritius');
INSERT INTO countries VALUES('MV','Malediven');
INSERT INTO countries VALUES('MW','Malawi');
INSERT INTO countries VALUES('MX','Mexiko');
INSERT INTO countries VALUES('MY','Malaysia');
INSERT INTO countries VALUES('MZ','Mosambik');
INSERT INTO countries VALUES('NA','Namibia');
INSERT INTO countries VALUES('NC','Neukaledonien');
INSERT INTO countries VALUES('NE','Niger');
INSERT INTO countries VALUES('NF','Norfolkinsel');
INSERT INTO countries VALUES('NG','Nigeria');
INSERT INTO countries VALUES('NI','Nicaragua');
INSERT INTO countries VALUES('NL','Niederlande');
INSERT INTO countries VALUES('NO','Norwegen');
INSERT INTO countries VALUES('NP','Nepal');
INSERT INTO countries VALUES('NR','Nauru');
INSERT INTO countries VALUES('NU','Niue');
INSERT INTO countries VALUES('NZ','Neuseeland');
INSERT INTO countries VALUES('OM','Oman');
INSERT INTO countries VALUES('PA','Panama');
INSERT INTO countries VALUES('PE','Peru');
INSERT INTO countries VALUES('PF','Französisch-Polynesien');
INSERT INTO countries VALUES('PG','Papua-Neuguinea');
INSERT INTO countries VALUES('PH','Philippinen');
INSERT INTO countries VALUES('PK','Pakistan');
INSERT INTO countries VALUES('PL','Polen');
INSERT INTO countries VALUES('PM','Saint-Pierre und Miquelon');
INSERT INTO countries VALUES('PN','Pitcairninseln');
INSERT INTO countries VALUES('PR','Puerto Rico');
INSERT INTO countries VALUES('PS','Staat Palästina');
INSERT INTO countries VALUES('PT','Portugal');
INSERT INTO countries VALUES('PW','Palau');
INSERT INTO countries VALUES('PY','Paraguay');
INSERT INTO countries VALUES('QA','Katar');
INSERT INTO countries VALUES('RE','Reunion');
INSERT INTO countries VALUES('RO','Rumänien');
INSERT INTO countries VALUES('RS','Serbien');
INSERT INTO countries VALUES('RU','Russische Föderation');
INSERT INTO countries VALUES('RW','Ruanda');
INSERT INTO countries VALUES('SA','Saudi-Arabien');
INSERT INTO countries VALUES('SB','Salomonen');
INSERT INTO countries VALUES('SC','Seychellen');
INSERT INTO countries VALUES('SD','Sudan');
INSERT INTO countries VALUES('SE','Schweden');
INSERT INTO countries VALUES('SG','Singapur');
INSERT INTO countries VALUES('SH','St. Helena');
INSERT INTO countries VALUES('SI','Slowenien');
INSERT INTO countries VALUES('SJ','Svalbard und Jan Mayen');
INSERT INTO countries VALUES('SK','Slowakei');
INSERT INTO countries VALUES('SL','Sierra Leone');
INSERT INTO countries VALUES('SM','San Marino');
INSERT INTO countries VALUES('SN','Senegal');
INSERT INTO countries VALUES('SO','Somalia');
INSERT INTO countries VALUES('SR','Suriname');
INSERT INTO countries VALUES('SS','Südsudan');
INSERT INTO countries VALUES('ST','São Tomé und Príncipe');
INSERT INTO countries VALUES('SV','El Salvador');
INSERT INTO countries VALUES('SX','Sint Maarten (niederl. Teil)');
INSERT INTO countries VALUES('SY','Syrien');
INSERT INTO countries VALUES('SZ','Swasiland');
INSERT INTO countries VALUES('TC','Turks- und Caicosinseln');
INSERT INTO countries VALUES('TD','Tschad');
INSERT INTO countries VALUES('TF','Französische Süd- und Antarktisgebiete');
INSERT INTO countries VALUES('TG','Togo');
INSERT INTO countries VALUES('TH','Thailand');
INSERT INTO countries VALUES('TJ','Tadschikistan');
INSERT INTO countries VALUES('TK','Tokelau');
INSERT INTO countries VALUES('TL','Osttimor');
INSERT INTO countries VALUES('TM','Turkmenistan');
INSERT INTO countries VALUES('TN','Tunesien');
INSERT INTO countries VALUES('TO','Tonga');
INSERT INTO countries VALUES('TR','Türkei');
INSERT INTO countries VALUES('TT','Trinidad und Tobago');
INSERT INTO countries VALUES('TV','Tuvalu');
INSERT INTO countries VALUES('TW','Taiwan');
INSERT INTO countries VALUES('TZ','Tansania');
INSERT INTO countries VALUES('UA','Ukraine');
INSERT INTO countries VALUES('UG','Uganda');
INSERT INTO countries VALUES('US','Vereinigte Staaten von Amerika');
INSERT INTO countries VALUES('UY','Uruguay');
INSERT INTO countries VALUES('UZ','Usbekistan');
INSERT INTO countries VALUES('VA','Vatikanstadt');
INSERT INTO countries VALUES('VC','St. Vincent und die Grenadinen');
INSERT INTO countries VALUES('VE','Venezuela');
INSERT INTO countries VALUES('VG','Britische Jungferninseln');
INSERT INTO countries VALUES('VI','Amerikanische Jungferninseln');
INSERT INTO countries VALUES('VN','Vietnam');
INSERT INTO countries VALUES('VU','Vanuatu');
INSERT INTO countries VALUES('WF','Wallis und Futuna');
INSERT INTO countries VALUES('WS','Samoa');
INSERT INTO countries VALUES('XK','Kosovo');
INSERT INTO countries VALUES('YE','Jemen');
INSERT INTO countries VALUES('YT','Mayotte');
INSERT INTO countries VALUES('ZA','Südafrika');
INSERT INTO countries VALUES('ZM','Sambia');
INSERT INTO countries VALUES('ZW','Simbabwe');

/* Table games: id, name, edition_year, players_min, players_max, playtime_min, playtime_max, playtime_per_player, age_min, last_played, rating */
INSERT INTO games VALUES(1, 'Die Siedler von Catan', 1995, 3, 4, 60, null, false, 10, 'Beschreibung folgt', null);
INSERT INTO games VALUES(2, '6 nimmt!', 1994, 2, 10, 45, null, false, 10, null, null);
INSERT INTO games VALUES(3, 'Colt Express', 2014, 2, 6, 30, null, false, 10, null, null);

/* Table authors: id, last_name, first_name, country_code */
INSERT INTO authors VALUES(1, 'Teuber', 'Klaus', 'DE');
INSERT INTO authors VALUES(2, 'Kramer', 'Wolfgang', 'DE');
INSERT INTO authors VALUES(3, 'Raimbault', 'Christophe', 'FR');

/* Table game_authors: game_id, author_id */
INSERT INTO game_authors VALUES(1, 1);
INSERT INTO game_authors VALUES(2, 2);
INSERT INTO game_authors VALUES(3, 3);

/* Table publishers: id, name, country_code */
INSERT INTO publishers VALUES(1, 'Kosmos', 'DE');
INSERT INTO publishers VALUES(2, 'Amigo', 'DE');
INSERT INTO publishers VALUES(3, 'Ludonaute', 'FR');

/* Table game_publishers: game_id, publisher_id */
INSERT INTO game_publishers VALUES(1, 1);
INSERT INTO game_publishers VALUES(2, 2);
INSERT INTO game_publishers VALUES(3, 3);

/* Table awards: id, name, award_year, country_code */
INSERT INTO awards VALUES(1, 'Spiel des Jahres', 'DE');
INSERT INTO awards VALUES(2, 'Deutscher Spiele-Preis', 'DE');
INSERT INTO awards VALUES(3, 'Auswahlliste Spiel des Jahres', 'DE');

/* Table game_awards: game_id, award_id, award_year */
INSERT INTO game_awards VALUES(1, 1, 1995);
INSERT INTO game_awards VALUES(1, 2, 1995);
INSERT INTO game_awards VALUES(2, 3, 1994);
INSERT INTO game_awards VALUES(3, 1, 2015);

/* Table images: id, description, role, width, height, url */
INSERT INTO images VALUES(1, "box", 1067, 1500, "https://images-na.ssl-images-amazon.com/images/I/91ReTacJ0WL._SL1500_.jpg", "Box von 6 Nimmt (Amazon)");

/* Table game_images: game_id, image_id */
INSERT INTO game_images VALUES(2, 1);

/* Table notes: id, added, note */
INSERT INTO notes VALUES(1, '2016-10-24 23:04:00', 'Ein erster Kommentar');

/* Table game_notes: game_id, note_id */
INSERT INTO game_notes VALUES(1, 1);

/*
CREATE USER 'mystuff'@'localhost' IDENTIFIED BY 'mystuff';
GRANT ALL ON mystuff.* TO 'mystuff'@'localhost';
*/