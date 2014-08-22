/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.exception;

import org.yetiz.gae.validator.AdminServiceValidator;

/**
 *
 * @author yeti
 */
public class ServiceUNAuthorizedException extends ServiceException {

	public ServiceUNAuthorizedException() {
		super("Service UNAuthorized.", AdminServiceValidator.EC_SERVICE_UNAUTHORIZED);
	}
}
