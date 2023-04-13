package com.msf.cipher.service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.msf.cipher.bean.CipherRequest;
import com.msf.cipher.bean.CipherWebRequest;

@Service
public class MSFService {

	private static Map<String, CipherRequest> requestCache = new HashMap<>();

	public String decryptAndHashRequest(CipherWebRequest cipherWebRequest) throws Exception {
		String result;
		CipherRequest request = requestCache
				.get(cipherWebRequest.getCipherAlgorithm() + cipherWebRequest.getPassword());
		if (request == null || request.isExpired()) {
			request = new CipherRequest(cipherWebRequest.getCipherAlgorithm(), cipherWebRequest.getPassword());
			requestCache.put(cipherWebRequest.getCipherAlgorithm() + cipherWebRequest.getPassword(), request);
		}
		result = decryptAndHash(cipherWebRequest.getCipherAlgorithm(), cipherWebRequest.getCipherText(),
				cipherWebRequest.getPassword(), cipherWebRequest.getHashAlgorithm());
		return result;
	}

	public static String decryptAndHash(String cipherAlgorithm, String cipherText, String password,
			String hashAlgorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), cipherAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decrypted = cipher.doFinal(hexStringToByteArray(cipherText));
		MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
		byte[] digest = md.digest(decrypted);
		return byteArrayToHexString(digest);
	}

	public static void clearCache() {
		requestCache.clear();
	}

	private static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

}
