/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.ds;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yetiz.ds.Token;
import org.yetiz.ds.User;
import org.yetiz.gae.GAEServiceProvider;
import org.yetiz.gae.exception.TokenArgumentException;
import org.yetiz.gae.exception.TokenExpiredException;
import org.yetiz.gae.exception.TokenNotFoundException;
import org.yetiz.gae.validator.TokenValidator;

/**
 *
 * @author yeti
 */
public class GAEToken extends Token {

	public GAEToken(String userID) {
		this.userID = userID;
		this.key = UUID.randomUUID().toString();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		this.expire = cal.getTime();
	}

	private GAEToken(String key, String userID) {
		this.key = key;
		this.userID = userID;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);
		this.expire = cal.getTime();
	}

	public TokenValidator getValidator() {
		return new TokenValidator(this);
	}

	public Key getKey() {
		return KeyFactory.createKey(Token.class.getCanonicalName(), key);
	}

	public GAEToken validate() throws TokenExpiredException {
		if (isExpire()) {
			throw new TokenExpiredException();
		}
		return this;
	}

	public static boolean validate(String key) throws TokenArgumentException, TokenNotFoundException, TokenExpiredException {
		GAEToken token = get(key);
		if (token.isExpire()) {
			throw new TokenExpiredException();
		}
		return true;
	}

	public static boolean validate(GAEToken token) throws TokenNotFoundException, TokenExpiredException {
		if (token == null) {
			throw new TokenNotFoundException();
		}
		if (token.isExpire()) {
			throw new TokenExpiredException();
		}
		return true;
	}

	public static GAEToken get(String key) throws TokenArgumentException, TokenNotFoundException {
		if (key == null) {
			throw new TokenArgumentException();
		}
		Entity entity;
		try {
			entity = GAEServiceProvider.GetDatastoreService().get(KeyFactory.createKey(Token.class.getCanonicalName(), key));
		} catch (EntityNotFoundException ex) {
			throw new TokenNotFoundException();
		}
		GAEToken token = new GAEToken(entity.getKey().getName(), (String) entity.getProperty(User.PARAM_USERID));
		token.setExpire((Date) entity.getProperty(PARAM_EXPIRE));
		return token;
	}

	public static GAEToken get(Key key) throws TokenArgumentException, TokenNotFoundException {
		return get(key.getName());
	}

	public boolean put() {
		try {
			Entity entity = new Entity(KeyFactory.createKey(Token.class.getCanonicalName(), key));
			entity.setProperty(Token.PARAM_KEY, key);
			entity.setProperty(User.PARAM_USERID, userID);
			entity.setProperty(PARAM_EXPIRE, expire);
			GAEServiceProvider.GetDatastoreService().put(entity);
			return true;
		} catch (Exception ex) {
			Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	public void delete() throws TokenNotFoundException {
		try {
			GAEServiceProvider.GetDatastoreService().delete(KeyFactory.createKey(Token.class.getCanonicalName(), key));
		} catch (Exception e) {
			throw new TokenNotFoundException();
		}
	}
}
