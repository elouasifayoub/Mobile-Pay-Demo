package com.solution.issacq.Demo.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BouncyCastleAesCbcEncryptor {
	
	Logger logger = LoggerFactory.getLogger(BouncyCastleAesCbcEncryptor.class);
	public static final String KEY_ALGORITHM = "AES";
	private static final String ALGORITHM = "AES/CBC/PKCS7Padding";
	
	public String decrypt(String ciphertext, String key){
        
		if (ciphertext == null) {
			return null;
		}
		try {
			Security.addProvider(new BouncyCastleProvider());
			byte[] decodedKey = Hex.decode(key);
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(decodedKey, KEY_ALGORITHM); 
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			 
			// decode the base64 encoded string
	        byte[] decodedciphertext = Base64.getDecoder().decode(ciphertext);
			
			Cipher cipher;
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(iv));
	        return new String(cipher.doFinal(decodedciphertext));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			logger.info ("Exception {} " , e.getMessage()) ;
			return null ;
		}
        
    }
	
	public String encrypt(String text, String key){
        
		try {
			Security.addProvider(new BouncyCastleProvider());
			byte[] decodedKey = Hex.decode(key);
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(decodedKey, KEY_ALGORITHM); 
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			
			Cipher cipher;
			cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, originalKey, new IvParameterSpec(iv));
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
        	logger.info ("Exception {} " , e.getMessage()) ;
			return null ;
        }
    }

}
