package com.msf.cipher.bean;

public class CipherRequest {
	private final long creationTime;
	private final String cipherAlgorithm;
	private final String password;
	private static final long TIMEOUT = 10 * 60 * 1000; // 10 minutes in milliseconds

	public CipherRequest(String cipherAlgorithm, String password) {
		this.creationTime = System.currentTimeMillis();
		this.cipherAlgorithm = cipherAlgorithm;
		this.password = password;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() - creationTime > TIMEOUT;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	public String getPassword() {
		return password;
	}
}
