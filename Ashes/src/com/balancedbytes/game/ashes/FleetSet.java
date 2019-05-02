package com.balancedbytes.game.ashes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class FleetSet {

	private List<Fleet> list = null;

	/**
	 *
	 */
	public FleetSet() {
		list = new ArrayList<Fleet>();
	}

	/**
	 * Adds a Fleet to this list. Checks for player number and adds to according
	 * fleet.
	 */
	public void add(Fleet anotherFleet) {
		if (anotherFleet != null) {
			Fleet fleet = get(anotherFleet.getPlayer());
			if (fleet != null) {
				fleet.add(anotherFleet);
			} else {
				list.add(anotherFleet);
			}
		}
	}

	/**
	 * Adds the elements of a given BattleFleetList to this list.
	 */
	public void add(FleetSet anotherSet) {
		if (anotherSet != null) {
			Iterator<Fleet> iterator = anotherSet.iterator();
			while (iterator.hasNext()) {
				add((Fleet) iterator.next());
			}
		}
	}

	/**
	 *
	 */
	public Fleet get(int index) {
		return (Fleet) list.get(index);
	}

	/**
	 * Gets Fleet of given player from this list.
	 */
	public Fleet get(Player player) {
		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				Fleet currentFleet = (Fleet) iterator.next();
				if (currentFleet.getPlayer() == player) {
					return currentFleet;
				}
			}
		}
		return null;
	}

	/**
	 * Gets number of transporter for whole list.
	 */
	public int getNrFighter() {
		int result = 0;

		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				result += ((Fleet) iterator.next()).getFighter();
			}
		}
		return result;
	}

	/**
	 * Gets number of total ships for whole list.
	 */
	public int getNrShips() {
		int result = 0;

		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				Fleet fleet = (Fleet) iterator.next();
				result += fleet.getFighter() + fleet.getTransporter();
			}
		}
		return result;
	}

	/**
	 * Gets number of transporter for whole list.
	 */
	public int getNrTransporter() {
		int result = 0;

		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				result += ((Fleet) iterator.next()).getTransporter();
			}
		}
		return result;
	}

	/**
	 * Iterator over the elements of this list.
	 */
	public Iterator<Fleet> iterator() {
		return list.listIterator();
	}

	/**
	 * Removes a Fleet from this set.
	 */
	public boolean remove(Fleet fleet) {
		return list.remove(fleet);
	}

	/**
	 * Sets number of transporter for whole set.
	 */
	public void setTransporter(int tr) {
		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				((Fleet) iterator.next()).setTransporter(tr);
			}
		}
	}

	/**
	 * Randomizes list order.
	 */
	public void shuffle() {
		Collections.shuffle(list);
	}

	/**
	 *
	 */
	public int size() {
		return list.size();
	}

	/**
	 *
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		if (list.size() > 0) {
			Iterator<Fleet> iterator = list.iterator();
			while (iterator.hasNext()) {
				if (first) {
					first = false;
				} else {
					buffer.append('\n');
				}
				Fleet fleet = (Fleet) iterator.next();
				buffer.append(fleet);
			}
		} else {
			buffer.append("no Fleets");
		}

		return buffer.toString();
	}

}
