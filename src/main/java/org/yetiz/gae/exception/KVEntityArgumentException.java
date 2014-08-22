/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.exception;

import org.yetiz.gae.validator.KVEntityValidator;

/**
 *
 * @author yeti
 */
public class KVEntityArgumentException extends ServiceException {

	public KVEntityArgumentException() {
		super("KVEntity Argument Missed.", KVEntityValidator.EC_KVENTITY_ARGUMENT);
	}
}
