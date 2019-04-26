package com.balancedbytes.game.roborally.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.balancedbytes.game.roborally.json.IJsonReadable;
import com.balancedbytes.game.roborally.json.IJsonWritable;
import com.balancedbytes.game.roborally.model.Orientation;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class FactoryElements implements IJsonReadable, IJsonWritable {
	
	private List<FactoryElement> fElements;
	
	public FactoryElements() {
		fElements = new ArrayList<FactoryElement>();
	}
	
	public FactoryElements add(FactoryElement element) {
		if ((element != null) && (!element.isMainElement() || !hasMainElement()))  {
			fElements.add(element);
		}
		return this;
	}
	
	public FactoryElements clear() {
		fElements.clear();
		return this;
	}
	
	public FactoryElement get(int index) {
		if ((index >= 0) && (index < fElements.size())) {
			return fElements.get(index);
		}
		return null;
	}
	
	public FactoryElement[] toArray() {
		return fElements.toArray(new FactoryElement[fElements.size()]);
	}
	
	public int size() {
		return fElements.size();
	}
	
	public boolean hasMainElement() {
		if (fElements.size() > 0) {
			for (FactoryElement element : fElements) {
				if (element.isMainElement()) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();
		for (FactoryElement element : fElements) {
			jsonArray.add(element.toJson());
		}
		return jsonArray;
	}
	
	// Json example:
	// [ "CB-W", "W1-S" ] = a conveyor belt leading to the west and a single wall to the south 
	@Override
	public FactoryElements fromJson(JsonValue jsonValue) {
		if ((jsonValue != null) && jsonValue.isArray()) {
			clear();
			for (JsonValue elementValue : jsonValue.asArray().values()) {
				String jsonString = elementValue.asString();
				int separatorPosition = jsonString.indexOf(FactoryElement.JSON_SEPARATOR);
				FactoryElementType type = null;
				Orientation orientation = null;
				if ((separatorPosition > 0) && (separatorPosition + 1 < jsonString.length())) {
					orientation = Orientation.findForJsonString(jsonString.substring(separatorPosition + 1));
					type = FactoryElementType.findForJsonString(jsonString.substring(0, separatorPosition));
				} else {
					type = FactoryElementType.findForJsonString(jsonString);
				}
				if (type != null) {
					FactoryElement element = type.createElement();
					if (orientation != null) {
						element.setOrientation(orientation);
					}
					add(element);
				}
			}
		}
		return this;
	}

}
