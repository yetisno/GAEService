/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.validator;

import org.yetiz.gae.GAEValidator;
import org.yetiz.gae.ds.GAEToken;
import org.yetiz.gae.ds.GAEUser;
import org.yetiz.gae.exception.TokenUNAuthorizedException;
import org.yetiz.gae.exception.UserArgumentException;
import org.yetiz.gae.exception.UserNotFoundException;
import org.yetiz.gae.exception.UserUNAuthorizedException;

/**
 *
 * @author yeti
 */
public class UserValidator extends GAEValidator {

	private GAEUser user = null;

	public static final int EC_USER_ARGUMENT = 21;
	public static final int EC_USER_NOT_FOUND = 22;
	public static final int EC_USER_DUPLICATE = 23;
	public static final int EC_USER_UNAUTHORIZED = 24;

	public UserValidator(GAEUser user) {
		this.user = user;
	}

	public static UserValidator exist(String userID) throws UserNotFoundException, UserArgumentException {
		if (userID == null) {
			throw new UserNotFoundException();
		}
		try {
			return new UserValidator(GAEUser.get(userID));
		} catch (UserNotFoundException e) {
			throw e;
		}
	}

	public static UserValidator exist(String userID, String passphrase) throws UserNotFoundException, UserArgumentException, UserUNAuthorizedException {
		UserValidator userValidator = exist(userID);
		if (!userValidator.fetch().getPassphrase().equals(GAEUser.getPassphrase(passphrase))) {
			throw new UserUNAuthorizedException();
		}
		return userValidator;
	}

	public UserValidator authorize(GAEToken token) throws TokenUNAuthorizedException {
		if (!token.getUserID().equals(user.getUserID())) {
			throw new TokenUNAuthorizedException();
		}
		return this;
	}

	public UserValidator authorize(String passphrase) throws UserUNAuthorizedException {
		if (!user.getPassphrase().equals(GAEUser.getPassphrase(passphrase))) {
			throw new UserUNAuthorizedException();
		}
		return this;
	}

	public GAEUser fetch() {
		return user;
	}

}
