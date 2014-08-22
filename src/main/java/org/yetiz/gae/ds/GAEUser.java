/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.ds;

import org.yetiz.ds.*;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yetiz.gae.GAEServiceProvider;
import org.yetiz.gae.exception.UserArgumentException;
import org.yetiz.gae.exception.UserNotFoundException;
import org.yetiz.gae.validator.UserValidator;

/**
 *
 * @author yeti
 */
public class GAEUser extends User {

	public GAEUser(String userID) {
		this.userID = userID;
	}

	public GAEUser(String userID, String passphrase) {
		this.userID = userID;
		this.setPassphrase(passphrase);
	}

	public UserValidator getValidator() {
		return new UserValidator(this);
	}

	public static GAEUser get(String userID) throws UserNotFoundException, UserArgumentException {
		if (userID == null) {
			throw new UserArgumentException();
		}
		Entity entity;
		try {
			entity = GAEServiceProvider.GetDatastoreService().get(KeyFactory.createKey(User.class.getCanonicalName(), userID));
		} catch (EntityNotFoundException ex) {
			throw new UserNotFoundException();
		}
		try {
			GAEUser user = new GAEUser(userID);
			Field pa = User.class.getDeclaredField("passphrase");
			pa.setAccessible(true);
			pa.set(user, (String) entity.getProperty(PARAM_PASSPHRASE));
			return user;
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public boolean put() {
		try {
			Entity entity = new Entity(User.class.getCanonicalName(), userID);
			entity.setProperty(PARAM_USERID, userID);
			entity.setProperty(PARAM_PASSPHRASE, getPassphrase());
			GAEServiceProvider.GetDatastoreService().put(entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean delete() throws UserNotFoundException {
		try {
			GAEServiceProvider.GetDatastoreService().delete(KeyFactory.createKey(User.class.getCanonicalName(), getUserID()));
			return true;
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
	}

}
