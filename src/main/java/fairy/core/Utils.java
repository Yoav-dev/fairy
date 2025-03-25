package fairy.core;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Utils {
	@SuppressWarnings("resource")
	static byte[] getCheckSum(InputStream i_InputStream) throws NoSuchAlgorithmException {
		MessageDigest checkSum = MessageDigest.getInstance("MD5");
		new DigestInputStream(i_InputStream, checkSum);
		return checkSum.digest();
	}
}