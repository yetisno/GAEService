/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.exception;

import org.yetiz.gae.validator.UserValidator;

/**
 *
 * @author yeti
 */
public class UserNotFoundException extends ServiceException {

	public UserNotFoundException() {
		super("User Not Found.", UserValidator.EC_USER_NOT_FOUND);
	}
}
