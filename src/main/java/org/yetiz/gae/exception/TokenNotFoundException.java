/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.exception;

import org.yetiz.gae.validator.TokenValidator;

/**
 *
 * @author yeti
 */
public class TokenNotFoundException extends ServiceException {

	public TokenNotFoundException() {
		super("Token Not Found.", TokenValidator.EC_TOKEN_NOT_FOUND);
	}
}
