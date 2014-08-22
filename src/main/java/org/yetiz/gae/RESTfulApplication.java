/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.yetiz.gae.exception.ServiceArgumentException;
import org.yetiz.gae.exception.ServiceUNAuthorizedException;
import org.yetiz.gae.rest.KeyValueDataStoreApi;
import org.yetiz.gae.rest.TokenApi;
import org.yetiz.gae.rest.UserApi;

/**
 *
 * @author yeti
 */
public class RESTfulApplication extends Application {

	public static final String PARAM_ADKEY = "adk";
	public static String ADKEY;

	public RESTfulApplication() {
		JsonObject obj = (JsonObject) new JsonParser().parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("config.json")));
		ADKEY = obj.get("ADKey").getAsString().toUpperCase();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<>();
		s.add(KeyValueDataStoreApi.class);
		s.add(TokenApi.class);
		s.add(UserApi.class);
		return s;
	}

	public static final boolean validateADKey(String adkey) throws ServiceArgumentException, ServiceUNAuthorizedException {
		if (adkey == null) {
			throw new ServiceArgumentException();
		} else if (!adkey.toUpperCase().equals(RESTfulApplication.ADKEY)) {
			throw new ServiceUNAuthorizedException();
		}
		return true;
	}
}
