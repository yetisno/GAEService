/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.validator;

import java.util.Calendar;
import java.util.Date;
import org.yetiz.gae.GAEValidator;
import org.yetiz.gae.ds.GAEToken;
import org.yetiz.gae.ds.GAEUser;
import org.yetiz.gae.exception.TokenArgumentException;
import org.yetiz.gae.exception.TokenExpiredException;
import org.yetiz.gae.exception.TokenNotFoundException;
import org.yetiz.gae.exception.TokenUNAuthorizedException;

/**
 *
 * @author yeti
 */
public class TokenValidator extends GAEValidator {

	public static final int EC_TOKEN_ARGUMENT = 11;
	public static final int EC_TOKEN_NOT_FOUND = 12;
	public static final int EC_TOKEN_UNAUTHORIZED = 13;
	public static final int EC_TOKEN_EXPIRED = 14;

	private GAEToken token = null;

	public TokenValidator(GAEToken token) {
		this.token = token;
	}

	public static TokenValidator exist(String tokenKey) throws TokenNotFoundException, TokenArgumentException {
		if (tokenKey == null) {
			throw new TokenNotFoundException();
		}
		return new TokenValidator(GAEToken.get(tokenKey));
	}

	public TokenValidator expire() throws TokenExpiredException {
		if (token.isExpire()) {
			throw new TokenExpiredException();
		}
		return this;
	}

	public TokenValidator expireOneMonth() throws TokenExpiredException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(token.getExpire()));
		cal.add(Calendar.MONTH, 1);
		if (cal.getTime().getTime() < new Date().getTime()) {
			throw new TokenExpiredException();
		}
		return this;
	}

	public TokenValidator authorize(GAEUser user) throws TokenUNAuthorizedException {
		if (!token.getUserID().equals(user.getUserID())) {
			throw new TokenUNAuthorizedException();
		}
		return this;
	}

	public GAEToken fetch() {
		return token;
	}

}
