package com.balancedbytes.game.ashes.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.balancedbytes.game.ashes.command.CmdAnnounce;
import com.balancedbytes.game.ashes.command.CmdBuild;
import com.balancedbytes.game.ashes.command.CmdDeclare;
import com.balancedbytes.game.ashes.command.CmdHomeplanet;
import com.balancedbytes.game.ashes.command.CmdPlanetname;
import com.balancedbytes.game.ashes.command.CmdPlayername;
import com.balancedbytes.game.ashes.command.CmdResearch;
import com.balancedbytes.game.ashes.command.CmdSend;
import com.balancedbytes.game.ashes.command.CmdSpy;
import com.balancedbytes.game.ashes.command.CmdTurntoken;
import com.balancedbytes.game.ashes.command.Command;
import com.balancedbytes.game.ashes.command.CommandList;
import com.balancedbytes.game.ashes.model.Improvement;
import com.balancedbytes.game.ashes.model.PoliticalTerm;
import com.balancedbytes.game.ashes.model.Unit;

/**
 * Recursive-Descent-Parser for Ashes Commands. Ashes defines a complete
 * Type 2 language for player use. The full grammar is given here in BNF
 * (Backus-Naur-Form):
 * <pre>
 *          parse -> statements
 *     statements -> statement statements | onBlock statements | (0)
 *      statement -> onBlockStmt ON NUMBER | declare | playerName | turntoken | announce
 *        onBlock -> ON planetNumber DO onBlockStmts DONE
 *   onBlockStmts -> onBlockStmt onBlockStmts | (0)
 *    onBlockStmt -> build | homeplanet | planetName | research | send | spy
 *          build -> BUILD NUMBER buildUnit
 *      buildUnit -> PDU | FP | OP | RP | FY | TY | FI | TR
 *        declare -> DECLARE politicalTerm ON playerNumber
 *  politicalTerm -> WAR | PEACE | NEUTRAL
 *     homeplanet -> HOMEPLANET
 *     planetName -> PLANETNAME WORD
 *   planetNumber -> NUMBER [1-40]
 *     playerName -> PLAYERNAME WORD
 *   playerNumber -> NUMBER [1-8]
 *       research -> RESEARCH NUMBER improvement
 *    improvement -> PR | FM | TM
 *           send -> SEND NUMBER sendUnit TO planetNumber
 *       sendUnit -> FI | TR | C0 | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C7 | C8 | C9
 *            spy -> SPY
 *      turntoken -> TURNTOKEN WORD
 *       announce -> ANNOUNCE WORD
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
	private int fPlayerNr;

	/**
	 * Starts a parserun.
	 */
	public CommandList parse(BufferedReader in, int playerNr) {
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
		fPlayerNr = playerNr;

		// get first lookahead token
		fLookAhead = scanner();

		try {
			return statements();
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
	private CmdBuild build(int onPlanetNr) {
		LOG.trace("build(" + onPlanetNr + ")");
		try {
			match(ParserToken.BUILD);
			int count = number(1, 9999);
			Unit unit = buildUnit();
			if (onPlanetNr > 0) {
				return new CmdBuild(fPlayerNr, count, unit, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdBuild(fPlayerNr, count, unit, planetNumber());
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
				return Unit.PDU;
			case FP:
				match(ParserToken.FP);
				return Unit.FP;
			case OP:
				match(ParserToken.OP);
				return Unit.OP;
			case RP:
				match(ParserToken.RP);
				return Unit.RP;
			case FY:
				match(ParserToken.FY);
				return Unit.FY;
			case TY:
				match(ParserToken.TY);
				return Unit.TY;
			case FI:
				match(ParserToken.FI);
				return Unit.FI;
			case TR:
				match(ParserToken.TR);
				return Unit.TR;
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
			PoliticalTerm politicalTerm = politicalTerm();
			match(ParserToken.ON);
			return new CmdDeclare(fPlayerNr, politicalTerm, playerNumber());
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
	private CmdTurntoken turntoken() {
		LOG.trace("turntoken()");
		try {
			match(ParserToken.TURNTOKEN);
			return new CmdTurntoken(fPlayerNr, word());
		} catch (ParserException pe) {
			throw new ParserException("TURNTOKEN: " + pe.getMessage());
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
	private CmdHomeplanet homeplanet(int onPlanetNr) {
		LOG.trace("homeplanet(" + onPlanetNr + ")");
		try {
			match(ParserToken.HOMEPLANET);
			if (onPlanetNr > 0) {
				return new CmdHomeplanet(fPlayerNr, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdHomeplanet(fPlayerNr, planetNumber());
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
			int onPlanetNr = planetNumber();
			match(ParserToken.DO);
			CommandList cmdList = onBlockStmts(onPlanetNr);
			match(ParserToken.DONE);
			return cmdList;
		} catch (ParserException pe) {
			throw new ParserException("ON BLOCK: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Command onBlockStmt(int onPlanetNr) {
		LOG.trace("onBlockStmt(" + onPlanetNr + ")");
		switch (fLookAhead) {
			case BUILD:
				return build(onPlanetNr);
			case HOMEPLANET:
				return homeplanet(onPlanetNr);
			case PLANETNAME:
				return planetName(onPlanetNr);
			case RESEARCH:
				return research(onPlanetNr);
			case SEND:
				return send(onPlanetNr);
			case SPY:
				return spy(onPlanetNr);
			default:
				throw new ParserException("unknown command");
		}
	}

	/**
	 *
	 */
	private CommandList onBlockStmts(int planetNr) {
		LOG.trace("onBlockStmts(" + planetNr + ")");
		try {
			switch (fLookAhead) {
				case BUILD:
				case HOMEPLANET:
				case PLANETNAME:
				case RESEARCH:
				case SEND:
				case SPY:
					CommandList cmdList = new CommandList();
					cmdList.add(onBlockStmt(planetNr));
					cmdList.add(onBlockStmts(planetNr));
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
	private int number(int min, int max) {
		LOG.trace("number(" + min + "," + max + ")");
		if (fLookAhead == ParserToken.NUMBER) {
			int aNumber = fNumber;
			match(ParserToken.NUMBER);
			if ((aNumber >= min) && (aNumber <= max)) {
				return aNumber;
			}
		}
		throw new ParserException("number between " + min + " and " + max + " expected");
	}
	
	/**
	 * 
	 */
	private int playerNumber() {
		return number(1, 8);
	}

	/**
	 * 
	 */
	private int planetNumber() {
		return number(1, 40);
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
	private CmdAnnounce announce() {
		LOG.trace("announce()");
		try {
			match(ParserToken.ANNOUNCE);
			return new CmdAnnounce(fPlayerNr, word());
		} catch (ParserException pe) {
			throw new ParserException("ANNOUNCE: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private CmdPlanetname planetName(int onPlanetNr) {
		LOG.trace("planetName(" + onPlanetNr + ")");
		try {
			match(ParserToken.PLANETNAME);
			String name = word();
			if (onPlanetNr > 0) {
				return new CmdPlanetname(fPlayerNr, name, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdPlanetname(fPlayerNr, name, planetNumber());
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
			return new CmdPlayername(fPlayerNr, word());
		} catch (ParserException pe) {
			throw new ParserException("PLAYERNAME: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private CmdResearch research(int onPlanetNr) {
		LOG.trace("research(" + onPlanetNr + ")");
		try {
			match(ParserToken.RESEARCH);
			int count = number(1, 999);
			Improvement improvement = improvement();
			if (onPlanetNr > 0) {
				return new CmdResearch(fPlayerNr, count, improvement, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdResearch(fPlayerNr, count, improvement, planetNumber());
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
				return Improvement.PR;
			case FM:
				match(ParserToken.FM);
				return Improvement.FM;
			case TM:
				match(ParserToken.TM);
				return Improvement.TM;
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
	private CmdSend send(int onPlanetNr) {
		LOG.trace("send(" + onPlanetNr + ")");
		try {
			match(ParserToken.SEND);
			int count = number(1, 9999);
			Unit unit = sendUnit();
			match(ParserToken.TO);
			int toPlanetNr = planetNumber();
			if (onPlanetNr > 0) {
				return new CmdSend(fPlayerNr, count, unit, toPlanetNr, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdSend(fPlayerNr, count, unit, toPlanetNr, planetNumber());
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
				return Unit.FI;
			case TR:
				match(ParserToken.TR);
				return Unit.TR;
			case C0:
				match(ParserToken.C0);
				return Unit.C0;
			case C1:
				match(ParserToken.C1);
				return Unit.C1;
			case C2:
				match(ParserToken.C2);
				return Unit.C2;
			case C3:
				match(ParserToken.C3);
				return Unit.C3;
			case C4:
				match(ParserToken.C4);
				return Unit.C4;
			case C5:
				match(ParserToken.C5);
				return Unit.C5;
			case C6:
				match(ParserToken.C6);
				return Unit.C6;
			case C7:
				match(ParserToken.C7);
				return Unit.C7;
			case C8:
				match(ParserToken.C8);
				return Unit.C8;
			case C9:
				match(ParserToken.C9);
				return Unit.C9;
			default:
				throw new ParserException("send unit expected");
		}
	}

	
	/**
	 *
	 */
	private CmdSpy spy(int onPlanetNr) {
		LOG.trace("spy(" + onPlanetNr + ")");
		try {
			match(ParserToken.SPY);
			if (onPlanetNr > 0) {
				return new CmdSpy(fPlayerNr, onPlanetNr);
			}
			match(ParserToken.ON);
			return new CmdSpy(fPlayerNr, planetNumber());
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
			case ANNOUNCE:
				return announce();
			case DECLARE:
				return declare();
			case PLAYERNAME:
				return playername();
			case TURNTOKEN:
				return turntoken();
			default:
				return onBlockStmt(0);
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
				case TURNTOKEN:
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
				System.out.println(cmdList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
}