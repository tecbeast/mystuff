package com.balancedbytes.game.roborally.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.junit.Test;

public class ByteUtilTest {

	@Test
	public void testByteArrayToHexToByteArray() throws NoSuchAlgorithmException {
		byte[] byteArray1 = new byte[32];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(byteArray1);
		String hexString1 = ByteUtil.convertByteArrayToHexString(byteArray1);
		byte[] byteArray2 = ByteUtil.convertHexStringToByteArray(hexString1);
		assertArrayEquals(byteArray1, byteArray2);
		String hexString2 = ByteUtil.convertByteArrayToHexString(byteArray2);
		assertEquals(hexString1, hexString2);
	}

}
