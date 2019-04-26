package com.balancedbytes.game.roborally.tool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public class PasswordHashGenerator {
	
	/**
	 * Generate a 256 bit salt as a 64 character hexString.
	 */
	public static String generateSalt() {
		byte[] salt = new byte[32];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
		} catch (NoSuchAlgorithmException nsa) {
			return null;
		}
		return Hex.encodeHexString(salt);
	}

	/**
	 * Hash message using SHA-256, resulting in a 64 character hexString.
	 */
    public static String hashSha256(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes("UTF-8"));
    		return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    public static String hmacSha256(String key, String message) {
    	byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(message); 
    	return Hex.encodeHexString(hmac);
    }
 
    public static void main(String[] args) {
		if ((args == null) || (args.length < 1) || (args.length > 2)) {
			System.err.println("USAGE: java " + PasswordHashGenerator.class.getName() + " <password>");
			System.err.println("       java " + PasswordHashGenerator.class.getName() + " <password> <salt>");
			return;
		}
		String salt = (args.length > 1) ? args[1] : generateSalt();
		System.out.println("Salt = " + salt);
		String hash = hashSha256(salt + args[0]);
		System.out.println("Hash = " + hash);
		String hmac = hmacSha256("ServerTimestamp", hash);
		System.out.println("Hmac = " + hmac);
	}

}
