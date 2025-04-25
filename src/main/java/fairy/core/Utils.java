package fairy.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Utils {
	static byte[] getCheckSum(InputStream i_InputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest checkSum = MessageDigest.getInstance("MD5");
		
		try (DigestInputStream digestInputStream = new DigestInputStream(i_InputStream, checkSum)) {
		} finally {
		}
		
		return checkSum.digest();
	}
}