package com.balancedbytes.game.roborally.util;

/**
 * 
 * @author TecBeast
 */
public class ByteUtil {

	public static String convertByteArrayToHexString(byte[] byteArray) {
	    StringBuilder result = new StringBuilder();
	    for (int i = 0; i < byteArray.length; i++) {
	    	result.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return result.toString();
	}
	
	public static byte[] convertHexStringToByteArray(String hexString) {
		byte[] result = new byte[hexString.length() / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
		}
		return result;
	}

}
