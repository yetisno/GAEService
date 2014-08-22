/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.ds;

import org.yetiz.ds.*;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.Date;
import org.yetiz.gae.GAEServiceProvider;
import org.yetiz.gae.exception.KVEntityNotFoundException;

/**
 *
 * @author yeti
 */
public class GAEKVEntity extends KVEntity {

	protected GAEKVEntity(String key, String userID) {
		this.key = key;
		this.userID = userID;
	}

	public GAEKVEntity(String key, String userID, String name, byte[] value) {
		this.key = key;
		this.userID = userID;
		this.value = value;
		this.name = name;
	}

	private String getPrimaryKey() {
		return getPrimaryKey(userID, key);
	}

	private static String getPrimaryKey(String userID, String key) {
		return userID + "\n" + key;
	}

	public static GAEKVEntity get(String key, String userID) throws KVEntityNotFoundException {
		try {
			Entity entity = GAEServiceProvider.GetDatastoreService().get(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey(userID, key)));
			GAEKVEntity ge = new GAEKVEntity(key, userID);
			ge.key = (String) entity.getProperty(PARAM_KEY);
			ge.name = (String) entity.getProperty(PARAM_NAME);
			ge.userID = (String) entity.getProperty(User.PARAM_USERID);
			ge.value = ((Blob) entity.getProperty(PARAM_VALUE)).getBytes();
			ge.date = (Date) entity.getProperty(PARAM_DATE);
			return ge;
		} catch (EntityNotFoundException e) {
			throw new KVEntityNotFoundException();
		}
	}

	public boolean put() {
		try {
			Entity entity = new Entity(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey()));
			entity.setProperty(PARAM_KEY, key);
			entity.setProperty(PARAM_NAME, name);
			entity.setProperty(User.PARAM_USERID, userID);
			entity.setProperty(PARAM_VALUE, new Blob(value));
			entity.setProperty(PARAM_DATE, new Date());
			GAEServiceProvider.GetDatastoreService().put(entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void delete(String key, String userID) throws KVEntityNotFoundException {
		try {
			GAEServiceProvider.GetDatastoreService().delete(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey(userID, key)));
		} catch (Exception e) {
			throw new KVEntityNotFoundException();
		}
	}
}
