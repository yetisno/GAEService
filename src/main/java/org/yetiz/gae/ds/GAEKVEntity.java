/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.ds;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import org.yetiz.ds.*;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Date;
import org.yetiz.gae.GAEServiceProvider;
import org.yetiz.gae.exception.KVEntityNotFoundException;

/**
 *
 * @author yeti
 */
public class GAEKVEntity extends KVEntity {

	private static Integer datastoreSizeLimit = 1048576;

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

	private boolean outOfValueLengthLimit() {
		return value.length >= datastoreSizeLimit;
	}

	public static GAEKVEntity get(String key, String userID) throws KVEntityNotFoundException {
		try {
			Entity entity = GAEServiceProvider.GetDatastoreService().get(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey(userID, key)));
			Long filesize = (Long) entity.getProperty(PARAM_LENGTH);
			GAEKVEntity ge = new GAEKVEntity(key, userID);
			ge.key = (String) entity.getProperty(PARAM_KEY);
			ge.name = (String) entity.getProperty(PARAM_NAME);
			ge.userID = (String) entity.getProperty(User.PARAM_USERID);
			if (filesize < datastoreSizeLimit) {
				ge.value = ((Blob) entity.getProperty(PARAM_VALUE)).getBytes();
			} else {
				ByteBuffer result = ByteBuffer.allocate(filesize.intValue());
				GcsInputChannel gic = GcsServiceFactory
					.createGcsService(RetryParams.getDefaultInstance())
					.openReadChannel(
						new GcsFilename(AppIdentityServiceFactory
							.getAppIdentityService()
							.getDefaultGcsBucketName(), getPrimaryKey(userID, key)), 0);
				gic.read(result);
				ge.value = result.array();
				result.clear();
				gic.close();
			}
			ge.date = (Date) entity.getProperty(PARAM_DATE);
			return ge;
		} catch (IOException | EntityNotFoundException e) {
			throw new KVEntityNotFoundException();
		}
	}

	public boolean put() {
		try {
			Entity entity = new Entity(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey()));
			entity.setProperty(PARAM_KEY, key);
			entity.setProperty(PARAM_NAME, name);
			entity.setProperty(User.PARAM_USERID, userID);
			if (!outOfValueLengthLimit()) {
				entity.setProperty(PARAM_VALUE, new Blob(value));
			} else {
				entity.setProperty(PARAM_VALUE, new Blob(getPrimaryKey().getBytes()));
				GcsFilename filename = new GcsFilename(AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName(), getPrimaryKey());
				OutputStream os = Channels.newOutputStream(GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance()).createOrReplace(filename, GcsFileOptions.getDefaultInstance()));
				os.write(value);
				os.close();
			}
			entity.setProperty(PARAM_DATE, new Date());
			entity.setProperty(PARAM_LENGTH, new Long(value.length));
			GAEServiceProvider.GetDatastoreService().put(entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void delete(String key, String userID) throws KVEntityNotFoundException {
		try {
			GAEServiceProvider.GetDatastoreService().delete(KeyFactory.createKey(KVEntity.class.getCanonicalName(), getPrimaryKey(userID, key)));
			GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance()).delete(new GcsFilename(AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName(), getPrimaryKey(userID, key)));
		} catch (Exception e) {
			throw new KVEntityNotFoundException();
		}
	}
}
