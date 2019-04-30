package com.balancedbytes.game.ashes.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.balancedbytes.game.ashes.Command;
import com.balancedbytes.game.ashes.CommandList;

/**
 * Recursive-Descent-Parser for Ashes Commands. Ashes defines a complete
 * Type 2 language for player use. The full grammar is given here in BNF
 * (Backus-Naur-Form):
 * <pre>
 *         parse -> BEGIN authorization statements END
 * authorization -> AUTHORIZATION WORD
 *    statements -> statement statements | onBlock statements | (0)
 *     statement -> onBlockStmt ON NUMBER | declare | message | playername
 *       onBlock -> ON NUMBER DO onBlockStmts DONE
 *  onBlockStmts -> onBlockStmt onBlockStmts | (0)
 *   onBlockStmt -> build | homeplanet | planetname | research | send | spy
 *         build -> BUILD NUMBER buildType
 *     buildType -> PDU | FP | OP | RP | FY | TY | FI | TR
 *       declare -> DECLARE declareType ON NUMBER
 *   declareType -> WAR | PEACE | NEUTRAL
 *    homeplanet -> HOMEPLANET
 *    planetname -> PLANETNAME WORD
 *    playername -> PLAYERNAME WORD
 *      research -> RESEARCH researchType
 *  researchType -> PR | FM | TM
 *          send -> SEND NUMBER sendType TO NUMBER
 *      sendType -> FI | TR | C0 | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C7 | C8 | C9
 *           spy -> SPY
 * </pre>
 * <b>Note:</b>
 * non-terminal symbols are in small letters and terminals in big,
 * (0) is for an empty set (Epsilon) and (+) indicates 1..N occurences.
 */
public class Parser {

	public static final int ERROR  = -1;
	public static final int EOF    =  0;
	public static final int EOL    =  1;
	public static final int NUMBER =  2;
	public static final int WORD   =  3;

	public static final int BEGIN         = 100;
	public static final int END           = 103;
	public static final int AUTHORIZATION = 104;
	public static final int ON            = 106;
	public static final int DO            = 107;
	public static final int DONE          = 108;
	public static final int BUILD         = 109;
	public static final int PLANETNAME    = 110;
	public static final int PLAYERNAME    = 111;
	public static final int SEND          = 112;
	public static final int RESEARCH      = 113;
	public static final int TO            = 115;
	public static final int DECLARE       = 116;
	public static final int HOMEPLANET    = 118;
	public static final int SPY           = 119;

	public static final int PDU = 200;
	public static final int FP  = 201;
	public static final int OP  = 202;
	public static final int RP  = 203;
	public static final int FY  = 204;
	public static final int TY  = 205;
	public static final int FI  = 206;
	public static final int TR  = 207;
	public static final int PR  = 208;
	public static final int FM  = 209;
	public static final int TM  = 210;
	public static final int C0  = 211;
	public static final int C1  = 212;
	public static final int C2  = 213;
	public static final int C3  = 214;
	public static final int C4  = 215;
	public static final int C5  = 216;
	public static final int C6  = 217;
	public static final int C7  = 218;
	public static final int C8  = 219;
	public static final int C9  = 220;

	public static final int WAR     = 300;
	public static final int PEACE   = 302;
	public static final int NEUTRAL = 303;

	public static final String UNDEFINED = "undefined";
	
	private static final Logger LOG = LogManager.getLogger(Parser.class);

	private static final Map<String, Integer> sTextToToken = new HashMap<String, Integer>();
	private static final Map<Integer, String> sTokenToText = new HashMap<Integer, String>();

 	static {

		sTextToToken.put("ERROR",  ERROR);
		sTextToToken.put("EOF",    EOF);
		sTextToToken.put("EOL",    EOL);
		sTextToToken.put("NUMBER", NUMBER);
		sTextToToken.put("WORD",   WORD);
		
		sTextToToken.put("begin",         BEGIN);
		sTextToToken.put("end",           END);
		sTextToToken.put("authorization", AUTHORIZATION);
		sTextToToken.put("on",            ON);
		sTextToToken.put("do",            DO);
		sTextToToken.put("done",          DONE);
		sTextToToken.put("build",         BUILD);
		sTextToToken.put("planetname",    PLANETNAME);
		sTextToToken.put("playername",    PLAYERNAME);
		sTextToToken.put("send",          SEND);
		sTextToToken.put("research",      RESEARCH);
		sTextToToken.put("to",            TO);
		sTextToToken.put("declare",       DECLARE);
		sTextToToken.put("homeplanet",    HOMEPLANET);
		sTextToToken.put("spy",           SPY);

		sTextToToken.put("pdu", PDU);
		sTextToToken.put("fp",  FP);
		sTextToToken.put("op",  OP);
		sTextToToken.put("rp",  RP);
		sTextToToken.put("fy",  FY);
		sTextToToken.put("ty",  TY);
		sTextToToken.put("fi",  FI);
		sTextToToken.put("tr",  TR);
		sTextToToken.put("pr",  PR);
		sTextToToken.put("fm",  FM);
		sTextToToken.put("tm",  TM);
		sTextToToken.put("c0",  C0);
		sTextToToken.put("c1",  C1);
		sTextToToken.put("c2",  C2);
		sTextToToken.put("c3",  C3);
		sTextToToken.put("c4",  C4);
		sTextToToken.put("c5",  C5);
		sTextToToken.put("c6",  C6);
		sTextToToken.put("c7",  C7);
		sTextToToken.put("c8",  C8);
		sTextToToken.put("c9",  C9);

		sTextToToken.put("war",     WAR);
		sTextToToken.put("peace",   PEACE);
		sTextToToken.put("neutral", NEUTRAL);

		// build the inverted Hashtable tokenToText
		Iterator<String> iterator = sTextToToken.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			sTokenToText.put(sTextToToken.get(key), key);
		}
    
  	}

	private StreamTokenizer fTokenizer;
	private String fWord;
	private StringBuffer fErrorBuffer;
	private int fNumber;
	private int fLookAhead;
	private boolean fBlocked;
	private boolean fErrorOccured;

	/**
	 * Starts a parserun.
	 */
	public CommandList parse(BufferedReader in) {
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
		fErrorOccured = false;
		fErrorBuffer = new StringBuffer();

		// get first lookahead token
		fLookAhead = scanner();

		CommandList cmdList = new CommandList();
		try {
			match(BEGIN);
			cmdList.add(authorization());
			cmdList.add(statements());
			match(END);
			return cmdList;
		} catch (ParserException pe) {
			fErrorOccured = true;
			addError(pe.getMessage());
			return null;
		}
		
	}

	/**
	 *
	 */
	private void addError(String msg) {
		if (fErrorOccured) {
			fErrorBuffer.append('\n');
		}
		fErrorBuffer.append(msg);
		fErrorOccured = true;
	}

	/**
	 *
	 */
	private Command build() {
		LOG.trace("build()");
		try {
			match(BUILD);
			Command cmd = new Command(BUILD);
			cmd.setNumber(parseNumber());
			cmd.setType(buildType());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("BUILD: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private int buildType() {
		LOG.trace("buildType()");
		switch (fLookAhead) {
			case PDU:
				return match(PDU);
			case FP:
				return match(FP);
			case OP:
				return match(OP);
			case RP:
				return match(RP);
			case FY:
				return match(FY);
			case TY:
				return match(TY);
			case FI:
				return match(FI);
			case TR:
				return match(TR);
			default:
				throw new ParserException("production type expected");
		}
	}

	/**
	 *
	 */
	private Command declare() {
		LOG.trace("declare()");
		try {
			match(DECLARE);
			Command cmd = new Command(DECLARE);
			cmd.setType(declareType());
			match(ON);
			cmd.setDestination(parseNumber());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("DECLARE: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private int declareType() {
		LOG.trace("declareType()");
		switch (fLookAhead) {
			case WAR:
				return match(WAR);
			case PEACE:
				return match(PEACE);
			case NEUTRAL:
				return match(NEUTRAL);
			default:
				throw new ParserException("political status expected");
		}
	}

	/**
	 *
	 */
	private int error(String msg, int[] begin, int[] end) {
		LOG.trace("error()");
		int startLine = fTokenizer.lineno();
		int endLine = startLine;
		int result = -1;
		addError("! error line " + fTokenizer.lineno() + ": " + msg);
		do {
			fLookAhead = scanner();
			if (inArray(fLookAhead, begin)) {
				result = 1;
				break;
			} else if (inArray(fLookAhead, end)) {
				result = 0;
				break;
			}
			endLine = fTokenizer.lineno();
		} while (fLookAhead != EOF);
		if (endLine > startLine) {
			if (endLine > startLine + 1) {
				addError("skipping lines " + (startLine + 1) + " - " + endLine + " ...");
			} else {
				addError("skipping line " + endLine + " ...");
			}
		}
		return result;
	}

	/**
	 *
	 */
	private Command authorization() {
		LOG.trace("authorization()");
		try {
			Command authorizationCmd = new Command(match(AUTHORIZATION));
			authorizationCmd.setText(parseWord());
			return authorizationCmd;
		} catch (ParserException pe) {
			throw new ParserException("AUTHORIZATION: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	public String getErrors() {
		return fErrorBuffer.toString();
	}

	/**
	 * Transforms text to parser token.
	 */
	public static String getText(int token) {
		if (sTokenToText.containsKey(token)) {
			return (String) sTokenToText.get(token);
		} else {
			return UNDEFINED;
		}
	}

	/**
	 * Transforms parser token to text.
	 */
	public static int getToken(String text) {
		String lookup = text.toLowerCase();
		if (sTextToToken.containsKey(lookup)) {
			return ((Integer) sTextToToken.get(lookup)).intValue();
		} else {
			return ERROR;
		}
	}

	/**
	 *
	 */
	private Command homeplanet() {
		LOG.trace("homeplanet()");
		match(HOMEPLANET);
		return new Command(HOMEPLANET);
	}

	/**
	 *
	 */
	private boolean inArray(int test, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (test == array[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 */
	private int match(int token) {
		LOG.trace("match(" + getText(token) + ")");
		if (fLookAhead == token) {
			fLookAhead = scanner();
			return token;
		} else {
			throw new ParserException("token " + getText(token).toUpperCase() + " expected");
		}
	}

	/**
	 *
	 */
	private CommandList onBlock() {
		LOG.trace("onBlock()");
		try {
			match(ON);
			int planetNr = parseNumber();
			match(DO);
			CommandList cmdList = onBlockStmts();
			cmdList.setSource(planetNr);
			match(DONE);
			return cmdList;
		} catch (ParserException pe) {
			throw new ParserException("ON BLOCK: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Command onBlockStmt() {
		LOG.trace("onBlockStmt()");
		switch (fLookAhead) {
			case BUILD:
				return build();
			case HOMEPLANET:
				return homeplanet();
			case PLANETNAME:
				return planetname();
			case RESEARCH:
				return research();
			case SEND:
				return send();
			case SPY:
				return spy();
			default:
				throw new ParserException("unknown command");
		}
	}

	/**
	 *
	 */
	private CommandList onBlockStmts() {
		LOG.trace("onBlockStmts()");
		final int[] first = { BUILD, HOMEPLANET, PLANETNAME, RESEARCH, SEND, SPY };
		final int[] follow = { DONE };
		try {
			switch (fLookAhead) {
				case BUILD:
				case HOMEPLANET:
				case PLANETNAME:
				case RESEARCH:
				case SEND:
				case SPY:
					CommandList cmdList = new CommandList();
					cmdList.add(onBlockStmt());
					cmdList.add(onBlockStmts());
					return cmdList;
				case DONE:
					return null;
				default:
					throw new ParserException("unknown command");
			}
		} catch (ParserException pe) {
			if (error(pe.getMessage(), first, follow) > 0) {
				return onBlockStmts();
			}
			return null;
		}
	}

	/**
	 *
	 */
	private int parseNumber() {
		LOG.trace("parseNumber()");
		if (fLookAhead == NUMBER) {
			int aNumber = fNumber;
			match(NUMBER);
			return aNumber;
		} else {
			throw new ParserException("number expected");
		}
	}

	/**
	 *
	 */
	private String parseWord() {
		LOG.trace("parseWord()");
		if (fLookAhead == WORD) {
			String aWord = fWord;
			match(WORD);
			return aWord;
		} else {
			throw new ParserException("word expected");
		}
	}

	/**
	 *
	 */
	private Command planetname() {
		LOG.trace("planetname()");
		try {
			match(PLANETNAME);
			Command cmd = new Command(PLANETNAME);
			cmd.setText(parseWord());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("PLANETNAME: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Command playername() {
		LOG.trace("playername()");
		try {
			match(PLAYERNAME);
			Command cmd = new Command(PLAYERNAME);
			cmd.setText(parseWord());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("PLAYERNAME: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private Command research() {
		LOG.trace("research()");
		try {
			match(RESEARCH);
			Command cmd = new Command(RESEARCH);
			cmd.setType(researchType());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("RESEARCH: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private int researchType() {
		LOG.trace("researchType()");
		switch (fLookAhead) {
			case PR:
				return match(PR);
			case FM:
				return match(FM);
			case TM:
				return match(TM);
			default:
				throw new ParserException("improvement expected");
		}
	}

	/**
	 *
	 */
	private int scanner() {
		int token;
		// do not read over EOF (lineno() will be increased otherwise)
		if (fBlocked) {
			return EOF;
		}
		try {
			// get next token from stream
			switch (fTokenizer.nextToken()) {
				// end of file or stream
				case StreamTokenizer.TT_EOF:
					fBlocked = true;
					return EOF;
				// end of line
				case StreamTokenizer.TT_EOL:
					return EOL;
				// a number (double value)
				case StreamTokenizer.TT_NUMBER:
					fNumber = (new Double(fTokenizer.nval)).intValue();
					return NUMBER;
				// a word or string
				// look up in symboltable (keys in lowercase)
				case StreamTokenizer.TT_WORD: // a word (string)
					fWord = fTokenizer.sval;
					token = getToken(fWord);
					if (token < 0) {
						return WORD;
					}
					return token;
				// a single character - convert it to a string
				// look up in symboltable (keys in lowercase)
				default:
					if ((char) fTokenizer.ttype == '"') {
						fWord = fTokenizer.sval;
						return WORD;
					} else {
						fWord = String.valueOf((char) fTokenizer.ttype);
						token = getToken(fWord);
						if (token < 0) {
							return WORD;
						}
						return token;
					}
			}
		// this shouldn't happen - return error token
		} catch (IOException e) {
			return ERROR;
		}
	}

	/**
	 *
	 */
	private Command send() {
		LOG.trace("send()");
		try {
			match(SEND);
			Command cmd = new Command(SEND);
			cmd.setNumber(parseNumber());
			cmd.setType(sendType());
			match(TO);
			cmd.setSource(parseNumber());
			return cmd;
		} catch (ParserException pe) {
			throw new ParserException("SEND: " + pe.getMessage());
		}
	}

	/**
	 *
	 */
	private int sendType() {
		LOG.trace("sendType()");
		switch (fLookAhead) {
			case FI:
				return match(FI);
			case TR:
				return match(TR);
			case C0:
				return match(C0);
			case C1:
				return match(C1);
			case C2:
				return match(C2);
			case C3:
				return match(C3);
			case C4:
				return match(C4);
			case C5:
				return match(C5);
			case C6:
				return match(C6);
			case C7:
				return match(C7);
			case C8:
				return match(C8);
			case C9:
				return match(C9);
			default:
				throw new ParserException("ship type expected");
		}
	}

	/**
	 *
	 */
	private Command spy() {
		LOG.trace("spy()");
		match(SPY);
		return new Command(SPY);
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
			default:
				Command cmd = onBlockStmt();
				match(ON);
				cmd.setSource(parseNumber());
				return cmd;
		}
	}

	/**
	 *
	 */
	private CommandList statements() {
		LOG.trace("statements()");
		final int[] first = { BUILD, DECLARE, HOMEPLANET, ON, PLANETNAME, PLAYERNAME, RESEARCH, SEND, SPY };
		final int[] follow = { END };
		CommandList cmdList = null;
		try {
			switch (fLookAhead) {
				case BUILD:
				case DECLARE:
				case HOMEPLANET:
				case PLANETNAME:
				case PLAYERNAME:
				case RESEARCH:
				case SEND:
				case SPY:
					cmdList = new CommandList();
					cmdList.add(statement());
					cmdList.add(statements());
					return cmdList;
				case ON:
					cmdList = onBlock();
					cmdList.add(statements());
					return cmdList;
				case END:
					return null;
				default:
					throw new ParserException("unknown command");
			}
		} catch (ParserException pe) {
			if (error(pe.getMessage(), first, follow) > 0) {
				return statements();
			}
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
			cmdList = parser.parse(br);
			if (cmdList == null) {
				System.out.println("[ Parser reports errors ]");
				System.out.println(parser.getErrors());
			} else {
				System.out.println(cmdList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
}