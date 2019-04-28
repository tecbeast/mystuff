package com.balancedbytes.game.ashes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of Ashes Commands. Allows manipulation of contained Commands.
 */
public class CommandList {

	private List list;

	/**
	 * Default constructor.
	 */
	public CommandList() {
		list = new ArrayList();
	}

	/**
	 * Adds a Command to this list.
	 */
	public void add(Command cmd) {
		if (cmd != null) {
			list.add(cmd);
		}
	}

	/**
	 * Adds the elements of a given CommandSet to this list.
	 */
	public void add(CommandList addList) {
		if (addList != null) {
			list.addAll(addList.list);
		}
	}

	/**
	 * Gets all Commands that have a token equal to the given token.
	 */
	public CommandList getToken(int token) {
		Command cmd = null;
		CommandList result = new CommandList();

		Iterator iterator = list.listIterator();
		while (iterator.hasNext()) {
			cmd = (Command) iterator.next();
			if (cmd.getToken() == token) {
				result.add(cmd);
			}
		}
		return result;
	}

	/**
	 * Iterator over the elements of this list.
	 */
	public Iterator iterator() {
		return list.listIterator();
	}

	/**
	 * Sets the player of all Commands in this set to the given player.
	 */
	public void setPlayer(int player) {
		Command cmd = null;
		Iterator iterator = list.listIterator();
		while (iterator.hasNext()) {
			cmd = (Command) iterator.next();
			cmd.setPlayer(player);
		}
	}

	/**
	 * Sets the source of all Commands in this set to the given source.
	 */
	public void setSource(int source) {
		Command cmd = null;
		Iterator iterator = list.listIterator();
		while (iterator.hasNext()) {
			cmd = (Command) iterator.next();
			cmd.setSource(source);
		}
	}

	/**
	 * Returns String representation of Commands in this list.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		Iterator iterator = list.listIterator();
		while (iterator.hasNext()) {
			if (first) {
				first = false;
			} else {
				buffer.append('\n');
			}
			buffer.append(((Command) iterator.next()).toString());
		}
		return buffer.toString();
	}

}