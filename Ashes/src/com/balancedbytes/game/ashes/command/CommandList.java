package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.balancedbytes.game.ashes.AshesUtil;
import com.balancedbytes.game.ashes.json.IJsonSerializable;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * A list of Ashes Commands. Allows manipulation of contained Commands.
 */
public class CommandList implements IJsonSerializable, Iterable<Command> {
	
	private List<Command> fCommands;

	/**
	 * Default constructor.
	 */
	public CommandList() {
		fCommands = new ArrayList<Command>();
	}

	/**
	 * 
	 */
	public void clear() {
		fCommands.clear();
	}
	
	/**
	 * 
	 */
	public int size() {
		return fCommands.size();
	}
	
	/**
	 * 
	 */
	public Command get(int index) {
		if ((index >= 0) && (index < fCommands.size())) { 
			return fCommands.get(index);
		}
		return null;
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
	 * Sort commands with the given Comparator.
	 */
	public CommandList sort(Comparator<Command> comparator) {
		fCommands.sort(comparator);
		return this;
	}
	
	@Override
	public Iterator<Command> iterator() {
		return fCommands.iterator();
	}
	
	/**
	 * 
	 */
	public CommandList filter(ICommandFilter filter) {
		CommandList result = new CommandList();
		for (Command command : fCommands) {
			if ((filter != null) && filter.filter(command)) {
				result.add(command);
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	public CommandList extract(CommandType type) {
		List<Command> newCommands = new ArrayList<Command>();
		CommandList result = new CommandList();
		for (Command command : fCommands) {
			if (command.getType() == type) {
				result.add(command);
			} else {
				newCommands.add(command);
			}
		}
		fCommands = newCommands;
		return result;
	}
	
	/**
	 * 
	 */
	public CommandList forPlayerNr(int playerNr) {
		CommandList result = new CommandList();
		for (Command command : fCommands) {
			if (command.getPlayerNr() == playerNr) {
				result.add(command);
			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	public CommandList setPlayerNr(int playerNr) {
		for (Command command : fCommands) {
			command.setPlayerNr(playerNr);
		}
		return this;
	}
		
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (Command command : fCommands) {
			jsonArray.add(command.toJson());
		}
		return jsonArray;
	}

	@Override
	public CommandList fromJson(JsonValue jsonValue) {
		clear();
		JsonArray jsonArray = jsonValue.asArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject cmdObject = jsonArray.get(i).asObject();
			String typeString = cmdObject.getString(Command.TYPE, null);
			if (AshesUtil.isProvided(typeString)) {
				switch (CommandType.valueOf(typeString)) {
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
					case TURNSECRET:
						add(new CmdTurnsecret().fromJson(cmdObject));
						break;
					default:
						break;
				}
				
			}
		}
		return this;
	}

}