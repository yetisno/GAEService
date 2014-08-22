/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae;

/**
 *
 * @author yeti
 */
public class Util {

	public static String wrapPathParam(String param, boolean withSlash) {
		return (withSlash ? "/" : "").concat("{").concat(param).concat("}");
	}

	/**
	 * With slash
	 * @param param
	 * @return "/{param}"
	 */
	public static String wrapPathParam(String param) {
		return wrapPathParam(param, true);
	}

}
