/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import org.yetiz.gae.exception.ServiceException;

/**
 *
 * @author yeti
 */
public class Response implements StreamingOutput {

	private int statusCode = 0;
	private String message = null;
	private final JsonObject root;

	public Response() {
		root = new JsonObject();
	}

	public Response(ServiceException ex) {
		statusCode = ex.getErrorCode();
		message = ex.getMessage();
		root = new JsonObject();
	}

	public boolean isFail() {
		return statusCode != 0;
	}

	public String build() {
		root.addProperty("s", statusCode);
		if (message != null) {
			root.addProperty("m", message);
		}
		return root.toString();
	}

	public Response add(String propertyName, JsonObject obj) {
		root.add(propertyName, obj);
		return this;
	}

	public Response add(String propertyName, String value) {
		root.addProperty(propertyName, value);
		return this;
	}

	public Response add(String propertyName, Boolean value) {
		root.addProperty(propertyName, value);
		return this;
	}

	public Response add(String propertyName, Character value) {
		root.addProperty(propertyName, value);
		return this;
	}

	public Response add(String propertyName, Number value) {
		root.addProperty(propertyName, value);
		return this;
	}

	@Override
	public String toString() {
		return build();
	}

	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		try {
			byte[] data = toString().getBytes();
			output.write(data, 0, data.length);
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}
	}
}
