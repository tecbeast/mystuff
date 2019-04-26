package com.balancedbytes.game.roborally.model.factory;

import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public enum FactoryElementType implements IJsonWritable {
	
	CONVEYOR_BELT_1("C1"),
	CONVEYOR_BELT_2("C2"),
	FLOOR("F"),
	FLOOR_1("F1"),
	FLOOR_2("F2"),
	FLOOR_3("F3"),
	GEAR_LEFT("GL"),
	GEAR_RIGHT("GR"),
	LASER_1("L1"),
	LASER_2("L2"),
	LASER_3("L3"),
	PIT("P"),
	REPAIR_SHOP_1("R1"),
	REPAIR_SHOP_2("R2"),
	WALL_1("W1"),
	WALL_2("W2"),
	WALL_3("W3");
	
	private String fJsonString;

	private FactoryElementType(String jsonString) {
		fJsonString = jsonString;
	}
	
	@Override
	public JsonValue toJson() {
		return Json.value(fJsonString);
	}

	public FactoryElement createElement() {
		switch (this) {
			case CONVEYOR_BELT_1:
				return new ConveyorBelt(1);
			case CONVEYOR_BELT_2:
				return new ConveyorBelt(2);
			case FLOOR:
				return new Floor(0);
			case FLOOR_1:
				return new Floor(1);
			case FLOOR_2:
				return new Floor(2);
			case FLOOR_3:
				return new Floor(3);
			case GEAR_LEFT:
				return new Gear(false);
			case GEAR_RIGHT:
				return new Gear(true);
			case LASER_1:
				return new Laser(1);
			case LASER_2:
				return new Laser(2);
			case LASER_3:
				return new Laser(3);
			case PIT:
				return new Pit();
			case WALL_1:
				return new Wall(1);
			case WALL_2:
				return new Wall(2);
			case WALL_3:
				return new Wall(3);
			default:
				return null;
		}
	}

	public static FactoryElementType findForJsonString(String jsonString) {
		if (jsonString != null) {
			for (FactoryElementType element : FactoryElementType.values()) {
				if (element.fJsonString.equalsIgnoreCase(jsonString)) {
					return element;
				}
			}
		}
		return null;
	}
	
}
