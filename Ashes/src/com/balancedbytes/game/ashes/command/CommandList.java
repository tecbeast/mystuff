package com.balancedbytes.game.ashes.command;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of Ashes Commands. Allows manipulation of contained Commands.
 */
public class CommandList {

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

}