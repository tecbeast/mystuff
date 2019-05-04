package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * A list of Ashes Commands. Allows manipulation of contained Commands.
 */
public class CommandList implements IJsonSerializable {
	
	private static final String COMMANDS = "commands";

	private List<Command> fCommands;

	/**
	 * Default constructor.
	 */
	public CommandList() {
		fCommands = new ArrayList<Command>();
	}

	/**
	 * Adds a Command to this list.
	 */
	public CommandList add(Command cmd) {
		if (cmd != null) {
			fCommands.add(cmd);
		}
		return this;
	}

	/**
	 * Adds the elements of a given CommandSet to this list.
	 */
	public CommandList add(CommandList addList) {
		if (addList != null) {
			fCommands.addAll(addList.fCommands);
		}
		return this;
	}
	
	/**
	 * 
	 */
	public void clear() {
		fCommands.clear();
	}
	
	/**
	 * Returns all commands as an array.
	 */
	public Command[] getCommands() {
		return fCommands.toArray(new Command[fCommands.size()]);
	}
	
	/**
	 * Returns String representation of Commands in this list.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Command command : fCommands) {
			if (first) {
				first = false;
			} else {
				builder.append(System.lineSeparator());
			}
			builder.append(command.toString());
		}
		return builder.toString();
	}
	
	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		for (Command command : fCommands) {
			jsonArray.add(command.toJson());
		}
		jsonObject.add(COMMANDS, jsonArray);
		return jsonObject;
	}

	@Override
	public CommandList fromJson(JsonValue jsonValue) {
		clear();
		JsonObject jsonObject = jsonValue.asObject();
		JsonArray jsonArray = jsonObject.get(COMMANDS).asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject cmdObject = jsonArray.get(i).asObject();
			String typeString = cmdObject.getString(Command.TYPE, null);
			if (AshesUtil.isProvided(typeString)) {
				switch (CommandType.valueOf(typeString)) {
					case ANNOUNCE:
						add(new CmdAnnounce().fromJson(cmdObject));
						break;
					case BUILD:
						add(new CmdBuild().fromJson(cmdObject));
						break;
					case DECLARE:
						add(new CmdDeclare().fromJson(cmdObject));
						break;
					case HOMEPLANET:
						add(new CmdHomeplanet().fromJson(cmdObject));
						break;
					case PLANETNAME:
						add(new CmdPlanetname().fromJson(cmdObject));
						break;						
					case PLAYERNAME:
						add(new CmdPlayername().fromJson(cmdObject));
						break;
					case RESEARCH:
						add(new CmdResearch().fromJson(cmdObject));
						break;
					case SEND:
						add(new CmdSend().fromJson(cmdObject));
						break;
					case SPY:
						add(new CmdSpy().fromJson(cmdObject));
						break;
					case TURNTOKEN:
						add(new CmdTurntoken().fromJson(cmdObject));
						break;
					default:
						break;
				}
				
			}
		}
		return this;
	}

}