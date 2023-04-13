package com.msf.cipher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.msf.cipher.bean.CipherWebRequest;
import com.msf.cipher.service.MSFService;

@RestController
public class MSFController {
	@Autowired
	MSFService msfService;

	@PostMapping("/decrypt")
	public String decryptAndHash(@RequestBody CipherWebRequest cipherWebRequest) throws Exception {

		return msfService.decryptAndHashRequest(cipherWebRequest);

	}

}
