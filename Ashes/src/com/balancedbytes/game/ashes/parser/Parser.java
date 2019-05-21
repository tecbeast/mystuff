package com.balancedbytes.game.ashes.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.command.CmdBuild;
import com.balancedbytes.game.ashes.command.CmdDeclare;
import com.balancedbytes.game.ashes.command.CmdHomeplanet;
import com.balancedbytes.game.ashes.command.CmdPlanetname;
import com.balancedbytes.game.ashes.command.CmdPlayername;
import com.balancedbytes.game.ashes.command.CmdResearch;
import com.balancedbytes.game.ashes.command.CmdSend;
import com.balancedbytes.game.ashes.command.CmdSpy;
import com.balancedbytes.game.ashes.command.CmdTurnsecret;
import com.balancedbytes.game.ashes.command.Command;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.model.Improvement;
import com.balancedbytes.game.ashes.model.PoliticalTerm;
import com.balancedbytes.game.ashes.model.Unit;
import com.eclipsesource.json.WriterConfig;

/**
 * Recursive-Descent-Parser for Ashes Commands. Ashes defines a complete
 * Type 2 language for player use. The full grammar is given here in BNF
 * (Backus-Naur-Form):
 * <pre>
 *          parse -> statements
 *     statements -> statement statements | onBlock statements | (0)
 *      statement -> onBlockStmt ON planet | declare | playerName | turnsecret
 *        onBlock -> ON planet DO onBlockStmts DONE
 *   onBlockStmts -> onBlockStmt onBlockStmts | (0)
 *    onBlockStmt -> build | homeplanet | planetName | research | send | spy
 *          build -> BUILD NUMBER buildUnit
 *      buildUnit -> PDU | FP | OP | RP | FY | TY | FI | TR
 *        declare -> DECLARE politicalTerm ON player
 *  politicalTerm -> WAR | PEACE | NEUTRAL
 *     homeplanet -> HOMEPLANET
 *     planetName -> PLANETNAME WORD
 *         planet -> NUMBER | WORD
 *     playerName -> PLAYERNAME WORD
 *         player -> NUMBER | WORD
 *       research -> RESEARCH NUMBER improvement
 *    improvement -> PR | FM | TM
 *           send -> SEND NUMBER sendUnit TO planetNumber
 *       sendUnit -> FI | TR | C0 | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C7 | C8 | C9
 *            spy -> SPY
 *     turnsecret -> TURNSECRET WORD
 * </pre>
 * <b>Note:</b>
 * non-terminal symbols are in small letters and terminals in big,
 * (0) is for an empty set (Epsilon) and (+) indicates 1..N occurences.
 */
public class Parser {

	private static final Log LOG = LogFactory.getLog(Parser.class);
	
	private static final Set<String> TOKENS = new HashSet<String>();
	
	static {
		for (ParserToken token : ParserToken.values()) {
			TOKENS.add(token.toString());
		}
	}

	private StreamTokenizer fTokenizer;
	private String fWord;
	private List<String> fErrors;
	private int fNumber;
	private ParserToken fLookAhead;
	private boolean fBlocked;
	
	private class Planet extends NameOrNumber {
		
		public Planet(int number) {
			super(number);
		}
		
		public Planet(String name) {
			super(name);
		}
		
	}
	
	private class Player extends NameOrNumber {
		
		public Player(int number) {
			super(number);
		}
		
		public Player(String name) {
			super(name);
		}
		
	}
	
	/**
	 * Starts a parserun.
	 */
	public CommandList parse(Reader in, int playerNr) {
		
		LOG.trace("parse()");
		
		// initialize tokenizer
		fTokenizer = new StreamTokenizer(in);
		
		fTokenizer.resetSyntax();
		fTokenizer.wordChars((int) 'a', (int) 'z');
		fTokenizer.wordChars((int) 'A', (int) 'Z');
		fTokenizer.whitespaceChars((int) '\u0000', (int) '\u0020');
		fTokenizer.eolIsSignificant(false);
		fTokenizer.lowerCaseMode(false);
		fTokenizer.parseNumbers();
		fTokenizer.quoteChar('"');
		fTokenizer.slashSlashComments(false);
		fTokenizer.slashStarComments(false);
		fTokenizer.commentChar('#');

		// set defaults
		fNumber = -1;
		fWord = "";
		fBlocked = false;
		fErrors = new ArrayList<String>();

		// get first lookahead token
		fLookAhead = scanner();

		try {
			return statements().setPlayerNr(playerNr);
		} catch (ParserException pe) {
			addError("! error line " + fTokenizer.lineno() + ": " + pe.getMessage());
			return null;
		}
		
	}

	/**
	 *
	 */
	private void addError(String msg) {
		fErrors.add(msg);
	}

	/**
	 *
	 */
	private CmdBuild build(Planet planet) {
		LOG.trace("build(" + planet + ")");
		try {
			match(ParserToken.BUILD);
			CmdBuild cmd = new CmdBuild();
			cmd.setCount(number());
			cmd.setUnit(buildUnit());
			Planet onPlanet = planet;
			if (onPlanet == null) {
				match(ParserToken.ON);
				onPlanet = planet();
			}
			cmd.setPlanetNr(onPlanet.getNumber());
			cmd.setPlanetName(onPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("BUILD: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Unit buildUnit() {
		LOG.trace("buildUnit()");
		switch (fLookAhead) {
			case PDU:
				match(ParserToken.PDU);
				return Unit.PLANETARY_DEFENSE_UNIT;
			case FP:
				match(ParserToken.FP);
				return Unit.FUEL_PLANT;
			case OP:
				match(ParserToken.OP);
				return Unit.ORE_PLANT;
			case RP:
				match(ParserToken.RP);
				return Unit.RARE_PLANT;
			case FY:
				match(ParserToken.FY);
				return Unit.FIGHTER_YARD;
			case TY:
				match(ParserToken.TY);
				return Unit.TRANSPORTER_YARD;
			case FI:
				match(ParserToken.FI);
				return Unit.FIGHTER;
			case TR:
				match(ParserToken.TR);
				return Unit.TRANSPORTER;
			default:
				throw new ParserException("build unit expected");
		}
	}

	/**
	 *
	 */
	private CmdDeclare declare() {
		LOG.trace("declare()");
		try {
			match(ParserToken.DECLARE);
			CmdDeclare cmd = new CmdDeclare();
			cmd.setPoliticalTerm(politicalTerm());
			match(ParserToken.ON);
			Player otherPlayer = player();
			cmd.setOtherPlayerNr(otherPlayer.getNumber());
			cmd.setOtherPlayerName(otherPlayer.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("DECLARE: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private PoliticalTerm politicalTerm() {
		LOG.trace("politicalTerm()");
		switch (fLookAhead) {
			case WAR:
				match(ParserToken.WAR);
				return PoliticalTerm.WAR;
			case PEACE:
				match(ParserToken.PEACE);
				return PoliticalTerm.PEACE;
			case NEUTRAL:
				match(ParserToken.NEUTRAL);
				return PoliticalTerm.NEUTRAL;
			default:
				throw new ParserException("political term expected");
		}
	}

	/**
	 *
	 */
	private CmdTurnsecret turnsecret() {
		LOG.trace("turnsecret()");
		try {
			match(ParserToken.TURNSECRET);
			CmdTurnsecret cmd = new CmdTurnsecret();
			cmd.setSecret(word());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("TURNSECRET: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	public String[] getErrors() {
		return fErrors.toArray(new String[fErrors.size()]);
	}

	/**
	 * Transforms parser token to text.
	 */
	public static ParserToken getToken(String text) {
		if (text != null) {
			String tokenText = text.toUpperCase();
			if (TOKENS.contains(tokenText)) {
				return ParserToken.valueOf(tokenText);
			}
		}
		return null;
	}

	/**
	 *
	 */
	private CmdHomeplanet homeplanet(Planet planet) {
		LOG.trace("homeplanet(" + planet + ")");
		try {
			match(ParserToken.HOMEPLANET);
			CmdHomeplanet cmd = new CmdHomeplanet();
			Planet onPlanet = planet;
			if (onPlanet == null) {
				match(ParserToken.ON);
				onPlanet = planet();
			}
			cmd.setPlanetNr(onPlanet.getNumber());
			cmd.setPlanetName(onPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("HOMEPLANET: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private ParserToken match(ParserToken token) {
		LOG.trace("match(" + token + ")");
		if (fLookAhead == token) {
			fLookAhead = scanner();
			return token;
		} else {
			throw new ParserException("token " + token + " expected");
		}
	}

	/**
	 *
	 */
	private CommandList onBlock() {
		LOG.trace("onBlock()");
		try {
			match(ParserToken.ON);
			Planet onPlanet = planet();
			match(ParserToken.DO);
			CommandList cmdList = onBlockStmts(onPlanet);
			match(ParserToken.DONE);
			return cmdList;
		} catch (ParserException pe) {
			throw new ParserException("ON BLOCK: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Command onBlockStmt(Planet planet) {
		LOG.trace("onBlockStmt(" + planet + ")");
		switch (fLookAhead) {
			case BUILD:
				return build(planet);
			case HOMEPLANET:
				return homeplanet(planet);
			case PLANETNAME:
				return planetname(planet);
			case RESEARCH:
				return research(planet);
			case SEND:
				return send(planet);
			case SPY:
				return spy(planet);
			default:
				throw new ParserException("unknown command");
		}
	}

	/**
	 *
	 */
	private CommandList onBlockStmts(Planet planet) {
		LOG.trace("onBlockStmts(" + planet + ")");
		try {
			switch (fLookAhead) {
				case BUILD:
				case HOMEPLANET:
				case PLANETNAME:
				case RESEARCH:
				case SEND:
				case SPY:
					CommandList cmdList = new CommandList();
					cmdList.add(onBlockStmt(planet));
					cmdList.add(onBlockStmts(planet));
					return cmdList;
				case DONE:
					return null;
				default:
					throw new ParserException("unknown command");
			}
		} catch (ParserException pe) {
			addError("! error line " + fTokenizer.lineno() + ": " + pe.getMessage());
			return null;
		}
	}

	/**
	 *
	 */
	private int number() {
		LOG.trace("number()");
		if (fLookAhead == ParserToken.NUMBER) {
			int aNumber = fNumber;
			match(ParserToken.NUMBER);
			return aNumber;
		}
		throw new ParserException("number expected");
	}
	
	/**
	 *
	 */
	private String word() {
		LOG.trace("word()");
		if (fLookAhead == ParserToken.WORD) {
			String aWord = fWord;
			match(ParserToken.WORD);
			return aWord;
		} else {
			throw new ParserException("word expected");
		}
	}

	/**
	 *
	 */
	private CmdPlanetname planetname(Planet planet) {
		LOG.trace("planetname(" + planet + ")");
		try {
			match(ParserToken.PLANETNAME);
			CmdPlanetname cmd = new CmdPlanetname();
			cmd.setName(word());
			Planet onPlanet = planet;
			if (onPlanet == null) {
				match(ParserToken.ON);
				onPlanet = planet();
			}
			cmd.setPlanetNr(onPlanet.getNumber());
			cmd.setPlanetName(onPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("PLANETNAME: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private CmdPlayername playername() {
		LOG.trace("playername()");
		try {
			match(ParserToken.PLAYERNAME);
			CmdPlayername cmd = new CmdPlayername();
			cmd.setPlayerName(word());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("PLAYERNAME: " + pe.getMessage());
		}
	}
	
	private Planet planet() {
		if (fLookAhead == ParserToken.NUMBER) {
			return new Planet(number());
		} else {
			return new Planet(word());
		}
	}

	private Player player() {
		if (fLookAhead == ParserToken.NUMBER) {
			return new Player(number());
		} else {
			return new Player(word());
		}
	}

	/**
	 *
	 */
	private CmdResearch research(Planet planet) {
		LOG.trace("research(" + planet + ")");
		try {
			match(ParserToken.RESEARCH);
			CmdResearch cmd = new CmdResearch();
			cmd.setCount(number());
			cmd.setImprovement(improvement());
			Planet onPlanet = planet;
			if (onPlanet == null) {
				match(ParserToken.ON);
				onPlanet = planet();
			}
			cmd.setPlanetNr(onPlanet.getNumber());
			cmd.setPlanetName(onPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("RESEARCH: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Improvement improvement() {
		LOG.trace("improvement()");
		switch (fLookAhead) {
			case PR:
				match(ParserToken.PR);
				return Improvement.PRODUCTION_RATE;
			case FM:
				match(ParserToken.FM);
				return Improvement.FIGHTER_MORALE;
			case TM:
				match(ParserToken.TM);
				return Improvement.TRANSPORTER_MORALE;
			default:
				throw new ParserException("research field expected");
		}
	}

	/**
	 *
	 */
	private ParserToken scanner() {
		ParserToken token;
		// do not read over EOF (lineno() will be increased otherwise)
		if (fBlocked) {
			return ParserToken.EOF;
		}
		try {
			// get next token from stream
			switch (fTokenizer.nextToken()) {
				// end of file or stream
				case StreamTokenizer.TT_EOF:
					fBlocked = true;
					return ParserToken.EOF;
				// end of line
				case StreamTokenizer.TT_EOL:
					return ParserToken.EOL;
				// a number (double value)
				case StreamTokenizer.TT_NUMBER:
					fNumber = (new Double(fTokenizer.nval)).intValue();
					return ParserToken.NUMBER;
				// a word or string
				// look up in symboltable (keys in lowercase)
				case StreamTokenizer.TT_WORD: // a word (string)
					fWord = fTokenizer.sval;
					token = getToken(fWord);
					return (token != null) ? token : ParserToken.WORD;
				// a single character - convert it to a string
				// look up in symboltable (keys in lowercase)
				default:
					if ((char) fTokenizer.ttype == '"') {
						fWord = fTokenizer.sval;
						return ParserToken.WORD;
					} else {
						fWord = String.valueOf((char) fTokenizer.ttype);
						token = getToken(fWord);
						return (token != null) ? token : ParserToken.WORD;
					}
			}
		// this shouldn't happen - return error token
		} catch (IOException e) {
			return ParserToken.ERROR;
		}
	}

	/**
	 *
	 */
	private CmdSend send(Planet planet) {
		LOG.trace("send(" + planet + ")");
		try {
			match(ParserToken.SEND);
			CmdSend cmd = new CmdSend();
			cmd.setCount(number());
			cmd.setUnit(sendUnit());
			match(ParserToken.TO);
			Planet toPlanet = planet();
			cmd.setToPlanetNr(toPlanet.getNumber());
			cmd.setToPlanetName(toPlanet.getName());
			Planet fromPlanet = planet;
			if (fromPlanet == null) {
				match(ParserToken.ON);
				fromPlanet = planet();
			}
			cmd.setFromPlanetNr(fromPlanet.getNumber());
			cmd.setFromPlanetName(fromPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("SEND: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Unit sendUnit() {
		LOG.trace("sendType()");
		switch (fLookAhead) {
			case FI:
				match(ParserToken.FI);
				return Unit.FIGHTER;
			case TR:
				match(ParserToken.TR);
				return Unit.TRANSPORTER;
			case C0:
				match(ParserToken.C0);
				return Unit.CARGO_0;
			case C1:
				match(ParserToken.C1);
				return Unit.CARGO_1;
			case C2:
				match(ParserToken.C2);
				return Unit.CARGO_2;
			case C3:
				match(ParserToken.C3);
				return Unit.CARGO_3;
			case C4:
				match(ParserToken.C4);
				return Unit.CARGO_4;
			case C5:
				match(ParserToken.C5);
				return Unit.CARGO_5;
			case C6:
				match(ParserToken.C6);
				return Unit.CARGO_6;
			case C7:
				match(ParserToken.C7);
				return Unit.CARGO_7;
			case C8:
				match(ParserToken.C8);
				return Unit.CARGO_8;
			case C9:
				match(ParserToken.C9);
				return Unit.CARGO_9;
			default:
				throw new ParserException("send unit expected");
		}
	}

	
	/**
	 *
	 */
	private CmdSpy spy(Planet planet) {
		LOG.trace("spy(" + planet + ")");
		try {
			match(ParserToken.SPY);
			CmdSpy cmd = new CmdSpy();
			Planet onPlanet = planet;
			if (onPlanet == null) {
				match(ParserToken.ON);
				onPlanet = planet();
			}
			cmd.setPlanetNr(onPlanet.getNumber());
			cmd.setPlanetName(onPlanet.getName());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("SPY: " + pe.getMessage());
		}
	}

	
	/**
	 *
	 */
	private Command statement() {
		LOG.trace("statement()");
		switch (fLookAhead) {
			case DECLARE:
				return declare();
			case PLAYERNAME:
				return playername();
			case TURNSECRET:
				return turnsecret();
			default:
				return onBlockStmt(null);
		}
	}

	/**
	 *
	 */
	private CommandList statements() {
		LOG.trace("statements()");
		CommandList cmdList = null;
		try {
			switch (fLookAhead) {
				case ANNOUNCE:
				case BUILD:
				case DECLARE:
				case HOMEPLANET:
				case PLANETNAME:
				case PLAYERNAME:
				case RESEARCH:
				case SEND:
				case SPY:
				case TURNSECRET:
					cmdList = new CommandList().add(statement());
					return cmdList.add(statements());
				case ON:
					cmdList = onBlock();
					return cmdList.add(statements());
				default:
					throw new ParserException("unknown command");
			}
		} catch (ParserException pe) {
			addError("! error line " + fTokenizer.lineno() + ": " + pe.getMessage());
			return null;
		}
	}
	
	/**
	 * Test for Parser. Takes name of a textfile as first commandline parameter and
	 * parses its contents. Prints String representation of the resulting
	 * CommandSet.
	 */
	public static void main(String args[]) {
		CommandList cmdList = null;
		if ((args == null) || (args.length < 1)) {
			System.err.println("USAGE: java " + Parser.class.getName() + " <inputfile>");
			return;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			Parser parser = new Parser();
			cmdList = parser.parse(br, 1);
			if (cmdList == null) {
				System.out.println("[ Parser reports errors ]");
				for (String error: parser.getErrors()) {
					System.out.println(error);
				}
			} else {
				System.out.println(cmdList.toJson().toString(WriterConfig.PRETTY_PRINT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
}