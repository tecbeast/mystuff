package com.balancedbytes.tool.kodi;

import com.eclipsesource.json.JsonValue;

public final class KodiTools {

	public static boolean isProvided(String... values) {
		if ((values == null) || (values.length == 0)) {
			return false;
		}
		for (int i = 0; i < values.length; i++) {
			if ((values[i] == null) || (values[i].length() == 0)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotNull(JsonValue... jsonValues) {
		if ((jsonValues == null) || (jsonValues.length == 0)) {
			return false;
		}
		for (int i = 0; i < jsonValues.length; i++) {
			if ((jsonValues[i] == null) || jsonValues[i].isNull()) {
				return false;
			}
		}
		return true;
	}
	
	public static String formatWithLeadingZeroes(int number) {
		return (number < 10) ? "0" + Integer.toString(number) : Integer.toString(number);
	}

	/**
	 * Entfernt führende Nullen von einer DezimalZahl. Wenn die gesamte Zahl oder
	 * der Vorkommateil nur aus 0-en besteht, bleibt eine 0 erhalten.
	 *
	 * @param aNumberString
	 *            formatierender String
	 * @return String ohne führende Nullen, Leerstring wenn Parameter leer oder
	 *         <code>null</code> ist
	 */
	public static String removeLeadingZeroes(String aNumberString) {
		String result = "";
		if (aNumberString != null && aNumberString.length() > 0) {
			// Anzahl führender Nullen ermitteln
			int anzNullen = 0;
			while (anzNullen < aNumberString.length() - 1 && aNumberString.charAt(anzNullen) == '0') {
				anzNullen++;
			}
			// Sonderfall: wenn Index jetzt auf einem Komma steht, muß eine Null
			// erhalten bleiben
			if (anzNullen > 0 && aNumberString.charAt(anzNullen) == ',') {
				anzNullen--;
			}
			result = aNumberString.substring(anzNullen);
		}
		return result;
	}

}
