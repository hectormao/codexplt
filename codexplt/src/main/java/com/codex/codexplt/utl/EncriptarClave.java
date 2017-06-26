package com.codex.codexplt.utl;

import java.security.MessageDigest;
import java.util.Formatter;

public class EncriptarClave {
	
	public static String encryptPassword(String password) throws Exception {
		String sha1 = "";
		
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(password.getBytes("UTF-8"));
		sha1 = byteToHex(crypt.digest());
		
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
