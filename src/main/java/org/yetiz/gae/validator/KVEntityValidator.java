/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.validator;

import org.yetiz.gae.GAEValidator;
import org.yetiz.gae.ds.GAEKVEntity;
import org.yetiz.gae.exception.KVEntityArgumentException;
import org.yetiz.gae.exception.KVEntityNotFoundException;

/**
 *
 * @author yeti
 */
public class KVEntityValidator extends GAEValidator {

	public static final int EC_KVENTITY_NOT_FOUND = 31;
	public static final int EC_KVENTITY_ARGUMENT = 32;

	private GAEKVEntity kVEntity = null;

	public KVEntityValidator(GAEKVEntity kVEntity) {
		this.kVEntity = kVEntity;
	}

	public static KVEntityValidator exist(String key, String userID) throws KVEntityNotFoundException, KVEntityArgumentException {
		if (key == null || userID == null) {
			throw new KVEntityArgumentException();
		}
		try {
			return new KVEntityValidator(GAEKVEntity.get(key, userID));
		} catch (KVEntityNotFoundException e) {
			throw e;
		}
	}

}
