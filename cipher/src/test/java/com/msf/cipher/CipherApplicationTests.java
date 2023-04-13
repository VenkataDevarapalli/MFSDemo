package com.msf.cipher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.msf.cipher.bean.CipherWebRequest;
import com.msf.cipher.service.MSFService;

public class CipherApplicationTests {

	MSFService msfService;

	@BeforeEach
	void setUp() {
		msfService = new MSFService();
		msfService.clearCache();
	}

	@Test
	void testDecryptAndHashRequest() throws Exception {

		// Test with AES cipher algorithm, SHA-256 hash algorithm, and a 128-bit key
		// size

		String cipherAlgorithm = "AES";
		String hashAlgorithm = "SHA-256";
		int keySize = 128;
		String password = "password";
		String plaintext = "Hello, world!";
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		SecretKey secretKey = keyGen.generateKey();
		byte[] keyBytes = secretKey.getEncoded();
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, cipherAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encrypted = cipher.doFinal(plaintext.getBytes());
		String cipherText = byteArrayToHexString(encrypted);
		// Test first request
		CipherWebRequest cipherWebRequest = new CipherWebRequest();
		cipherWebRequest.setCipherAlgorithm(cipherAlgorithm);
		cipherWebRequest.setCipherText(cipherText);
		cipherWebRequest.setPassword(password);
		cipherWebRequest.setHashAlgorithm(hashAlgorithm);

		String result1 = msfService.decryptAndHashRequest(cipherWebRequest);
		String expected1 = calculateHash(plaintext, hashAlgorithm);
		assertEquals(expected1, result1);
		// Wait for 5 minutes
		TimeUnit.MINUTES.sleep(5);
		// Test same request again
		String result2 = msfService.decryptAndHashRequest(cipherWebRequest);
		assertEquals(expected1, result2);
		// Wait for another 6 minutes
		TimeUnit.MINUTES.sleep(6);
		// Test same request again
		String result3 = msfService.decryptAndHashRequest(cipherWebRequest);
		String expected3 = calculateHash(plaintext, hashAlgorithm);
		assertEquals(expected3, result3);
		// Test with a different password
		String password2 = "password2";
		msfService.clearCache();
		String result4 = msfService.decryptAndHashRequest(cipherWebRequest);
		String expected4 = calculateHash(plaintext, hashAlgorithm);
		assertEquals(expected4, result4);
	}

	private String calculateHash(String plaintext, String hashAlgorithm) throws Exception {
		MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
		byte[] digest = md.digest(plaintext.getBytes());
		return byteArrayToHexString(digest);
	}

	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
