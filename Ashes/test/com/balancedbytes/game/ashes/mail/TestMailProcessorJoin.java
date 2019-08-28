package com.balancedbytes.game.ashes.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.balancedbytes.game.ashes.model.Join;

public class TestMailProcessorJoin {

	@Test
	public void testBuildListOfUserNamesOrdered() {
		List<Join> joins = new ArrayList<Join>();
		joins.add(createJoin("join1", new int[] { 1,2,3,4,5,6,7,8 }));
		joins.add(createJoin("join4", new int[] { 4,5,6,7,8,1,2,3 }));
		joins.add(createJoin("join3", new int[] { 3,4,5,6,7,8,1,2 }));
		joins.add(createJoin("join2", new int[] { 2,3,4,5,6,7,8,1 }));
		joins.add(createJoin("join6", new int[] { 6,7,8,1,2,3,4,5 }));
		joins.add(createJoin("join5", new int[] { 5,6,7,8,1,2,3,4 }));
		joins.add(createJoin("join8", new int[] { 8,1,2,3,4,5,6,7 }));
		joins.add(createJoin("join7", new int[] { 7,8,1,2,3,4,5,6 }));
		List<String> userNames = new MailProcessorJoin().buildListOfUserNames(joins);
		assertNotNull(userNames);
		assertEquals(8, userNames.size());
		assertEquals("join1", userNames.get(0));
		assertEquals("join2", userNames.get(1));
		assertEquals("join3", userNames.get(2));
		assertEquals("join4", userNames.get(3));
		assertEquals("join5", userNames.get(4));
		assertEquals("join6", userNames.get(5));
		assertEquals("join7", userNames.get(6));
		assertEquals("join8", userNames.get(7));
	}
	
	@Test
	public void testBuildListOfUserNamesRandomized() {
		List<Join> joins = new ArrayList<Join>();
		joins.add(createJoin("join6", new int[] { 6,7,8,1,2,3,4,5 }));
		joins.add(createJoin("join7", new int[] { 6,7,8,1,2,3,4,5 }));
		joins.add(createJoin("join8", new int[] { 6,7,8,1,2,3,4,5 }));
		joins.add(createJoin("join1", new int[] { 1,2,3,4,5,6,7,8 }));
		joins.add(createJoin("join2", new int[] { 2,3,4,5,6,7,8,1 }));
		joins.add(createJoin("join3", new int[] { 3,4,5,6,7,8,1,2 }));
		joins.add(createJoin("join4", new int[] { 4,5,6,7,8,1,2,3 }));
		joins.add(createJoin("join5", new int[] { 5,6,7,8,1,2,3,4 }));
		List<String> userNames = new MailProcessorJoin().buildListOfUserNames(joins);
		assertNotNull(userNames);
		assertEquals(8, userNames.size());
		assertEquals("join1", userNames.get(0));
		assertEquals("join2", userNames.get(1));
		assertEquals("join3", userNames.get(2));
		assertEquals("join4", userNames.get(3));
		assertEquals("join5", userNames.get(4));
		assertTrue("userName[5] = " + userNames.get(5), "join6".equals(userNames.get(5)) || "join7".equals(userNames.get(5)) || "join8".equals(userNames.get(5)));
		assertTrue("userName[6] = " + userNames.get(6), "join6".equals(userNames.get(6)) || "join7".equals(userNames.get(6)) || "join8".equals(userNames.get(6)));
		assertTrue("userName[7] = " + userNames.get(7), "join6".equals(userNames.get(7)) || "join7".equals(userNames.get(7)) || "join8".equals(userNames.get(7)));
	}
	
	private Join createJoin(String userName, int[] homePlanetArray) {
		Join join = new Join();
		join.setUserName(userName);
		List<Integer> homePlanets = new ArrayList<Integer>();
		for (int i = 0; i < homePlanetArray.length; i++) {
			homePlanets.add(homePlanetArray[i]);
		}
		join.setHomePlanets(homePlanets);
		return join;
	}

}
