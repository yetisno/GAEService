/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.validator;

import org.yetiz.gae.GAEValidator;
import org.yetiz.gae.RESTfulApplication;
import org.yetiz.gae.exception.ServiceArgumentException;
import org.yetiz.gae.exception.ServiceUNAuthorizedException;

/**
 *
 * @author yeti
 */
public class AdminServiceValidator extends GAEValidator {

	public static final int EC_SERVICE_OK = 0;
	public static final int EC_SERVICE_ARGUMENT = 1;
	public static final int EC_SERVICE_UNAUTHORIZED = 2;

	private static AdminServiceValidator asv = null;

	protected static AdminServiceValidator getInstance() {
		if (asv == null) {
			return createInstance();
		}
		return asv;
	}

	private static synchronized AdminServiceValidator createInstance() {
		if (asv == null) {
			asv = new AdminServiceValidator();
		}
		return asv;
	}

	public static AdminServiceValidator adKey(String adkey) throws ServiceUNAuthorizedException, ServiceArgumentException {
		if (adkey == null) {
			throw new ServiceArgumentException();
		}
		if (!RESTfulApplication.ADKEY.equals(adkey.toUpperCase())) {
			throw new ServiceUNAuthorizedException();
		}
		return getInstance();
	}
}
