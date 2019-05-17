package com.balancedbytes.game.ashes.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class JsonObjectWrapper {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private JsonObject fJsonObject;

	public JsonObjectWrapper(JsonObject jsonObject) {
		if (jsonObject == null) {
			throw new NullPointerException("Parameter jsonObject must not be null.");
		}
		fJsonObject = jsonObject;
	}
	
	public JsonObject toJsonObject() {
		return fJsonObject;
	}

	public Date getDate(String key) {
		if (key != null) {
			String dateString = fJsonObject.getString(key, null);
			if (dateString != null) {
				try {
					return DATE_FORMAT.parse(dateString);
				} catch (ParseException pe) {
					// fall through
				}
			}
		}
		return null;
	}
	
	public String getString(String key) {
		if (key != null) {
			return fJsonObject.getString(key, null);
		}
		return null;
	}

	public int getInt(String key) {
		if (key != null) {
			return fJsonObject.getInt(key, 0);
		}
		return 0;
	}
	
	public JsonArray getArray(String key) {
		if (key != null) {
			return fJsonObject.get(key).asArray();
		}
		return null;
	}

	public JsonObjectWrapper add(String key, Date value) {
		if ((key != null) && (value != null)) {
			String dateString = (value != null) ? DATE_FORMAT.format(value) : null;
			fJsonObject.add(key, dateString);
		}
		return this;
	}

	public JsonObjectWrapper add(String key, String value) {
		if ((key != null) && (value != null)) {
			fJsonObject.add(key, value);
		}
		return this;
	}

	public JsonObjectWrapper add(String key, int value) {
		if (key != null) {
			fJsonObject.add(key, value);
		}
		return this;
	}
	
	public JsonObjectWrapper add(String key, JsonArray value) {
		if ((key != null) && (value != null)) {
			fJsonObject.add(key, value);
		}
		return this;
	}

}
