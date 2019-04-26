package com.balancedbytes.game.roborally.model.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.balancedbytes.game.roborally.model.Orientation;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public class FactoryElementsTest {

	@Test
	public void testFromJson() {
		JsonValue jsonValue = Json.parse("[\"C1-W\", \"W1-E\"]");
		FactoryElement[] elementArray = new FactoryElements().fromJson(jsonValue).toArray();
		assertNotNull(elementArray);
		assertEquals(2, elementArray.length);
		assertEquals(FactoryElementType.CONVEYOR_BELT_1, elementArray[0].getType());
		assertEquals(Orientation.WEST, elementArray[0].getOrientation());
		assertEquals(FactoryElementType.WALL_1, elementArray[1].getType());
		assertEquals(Orientation.EAST, elementArray[1].getOrientation());
	}
	
	@Test
	public void testFromJsonWith2MainElements() {
		JsonValue jsonValue = Json.parse("[ \"P\", \"C2-W\", \"W1-S\"]");
		FactoryElement[] elementArray = new FactoryElements().fromJson(jsonValue).toArray();
		assertNotNull(elementArray);
		assertEquals(2, elementArray.length);
		assertEquals(FactoryElementType.PIT, elementArray[0].getType());
		assertEquals(FactoryElementType.WALL_1, elementArray[1].getType());
		assertEquals(Orientation.SOUTH, elementArray[1].getOrientation());
	}

}
