package com.balancedbytes.game.ashes;

import java.io.Serializable;

import com.balancedbytes.game.ashes.parser.Parser;
import com.balancedbytes.game.ashes.parser.ParserToken;

/**
 * A regular Ashes Command. Usually parsed from player input. Consists of:
 * <ul>
 * <li>token: the command itself, taken from Parser</li>
 * <li>nr: amount of something (e.g. number of fighters)</li>
 * <li>type: type of something (e.g. send fighter or transporter)</li>
 * <li>source: planet the command is issued on</li>
 * <li>dest: destination for the command (e.g. send x from here to
 * <em>there</em>)</li>
 * <li>player: player giving the command</li>
 * <li>list: list of associated objects</li>
 * <li>text: a text attribute for the command (e.g. a message)</li>
 * </ul>
 * 
 * @see Parser
 */
public class OldCommand implements Serializable {

	private static final long serialVersionUID = -4845328538079114057L;
	
	private ParserToken fToken;
	private ParserToken fType;
	private int fPlayerNr;
	private int number, source, destination;
	private String text;

	/**
	 * Default constructor. Token must be specified at creation time.
	 */
	public OldCommand(ParserToken token) {
		fToken = token;
	}

	/**
	 * Returns destination (planet).
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * Returns number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns player.
	 */
	public int getPlayerNr() {
		return fPlayerNr;
	}

	/**
	 * Returns source (planet).
	 */
	public int getSource() {
		return source;
	}

	/**
	 * Returns text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns token (command).
	 */
	public ParserToken getToken() {
		return fToken;
	}

	/**
	 * Returns type.
	 */
	public ParserToken getType() {
		return fType;
	}

	/**
	 * Specifies destination (planet).
	 */
	public void setDestination(int destination) {
		this.destination = destination;
	}

	/**
	 * Specifies number.
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Specifies executing player.
	 */
	public void setPlayerNr(int playerNr) {
		fPlayerNr = playerNr;
	}

	/**
	 * Specifies source (planet).
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * Specifies text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Specifies type.
	 */
	public void setType(ParserToken type) {
		fType = type;
	}

	/**
	 * Returns String representation of this Command.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("player ");
		builder.append(fPlayerNr);
		builder.append(": ");
		builder.append(fToken);
		builder.append(" ");
		switch (fToken) {
			case ANNOUNCE:
			case PLAYERNAME:
			case TURNTOKEN:
				builder.append(text);
				break;
			case BUILD:
				builder.append(number);
				builder.append(" ");
				builder.append(fType);
				builder.append(" on ");
				builder.append(source);
				break;
			case DECLARE:
				builder.append(fType);
				builder.append(" with ");
				builder.append(destination);
				break;
			case HOMEPLANET:
				builder.append("on ");
				builder.append(source);
				break;
			case PLANETNAME:
				builder.append(text);
				builder.append(" on ");
				builder.append(source);
				break;
			case RESEARCH:
				builder.append(fType);
				builder.append(" on ");
				builder.append(source);
				break;
			case SEND:
				builder.append(number);
				builder.append(" ");
				builder.append(fType);
				builder.append(" to ");
				builder.append(destination);
				break;
			case SPY:
				builder.append("on ");
				builder.append(source);
				break;
			default:
				break;
		}
		return builder.toString();
	}

}