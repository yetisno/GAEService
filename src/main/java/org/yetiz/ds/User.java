/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.ds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author yeti
 */
public class User {

	public static final String PARAM_USERID = "ui";
	public static final String PARAM_PASSPHRASE = "pa";

	protected String userID; //PARAM_USERID
	protected String passphrase; //PARAM_PASSPHRASE

	public String getUserID() {
		return userID;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = getPassphrase(passphrase);
	}

	public String getPassphrase() {
		return passphrase;
	}

	public static String getPassphrase(String passphrase) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(passphrase.getBytes());
			return Hex.encodeHexString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new NullPointerException();
		}
	}

}
