package com.balancedbytes.game.roborally.model.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.balancedbytes.game.roborally.model.Coordinate;
import com.eclipsesource.json.Json;

public class FactoryBoardTest {

	@Test
	public void testFromJson() {
		// a factory floor example (size = 4)
		String jsonString = "{ \"tiles\" : " +
			"[" +
			"  [ [          ], [ \"C1-N\" ], [ \"P\"    ], [          ] ]," +
			"  [ [ \"W1-W\" ], [          ], [ \"C1-W\" ], [          ] ]," +
			"  [ [          ], [ \"L1-N\" ], [          ], [          ] ]," +
			"  [ [          ], [          ], [ \"P\"    ], [ \"C2-E\" ] ]" +
			"] }";
		FactoryBoard floor = new FactoryBoard().fromJson(Json.parse(jsonString));
		assertEquals(4, floor.getSize());
		FactoryTile tile20 = floor.get(new Coordinate(2, 0));
		assertEquals(1, tile20.getElements().size());
		assertEquals(FactoryElementType.PIT, tile20.getElements().get(0).getType());
		// System.out.println(floor.toJson().toString(PrettyPrint.singleLine()));
	}

}
