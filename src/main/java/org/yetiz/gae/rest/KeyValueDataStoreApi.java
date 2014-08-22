/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.rest;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.codec.binary.Base64;
import org.yetiz.ds.KVEntity;
import org.yetiz.gae.Response;
import org.yetiz.ds.Token;
import org.yetiz.gae.ds.GAEKVEntity;
import org.yetiz.gae.ds.GAEToken;
import org.yetiz.gae.exception.KVEntityNotFoundException;
import org.yetiz.gae.exception.TokenArgumentException;
import org.yetiz.gae.exception.TokenExpiredException;
import org.yetiz.gae.exception.TokenNotFoundException;
import org.yetiz.gae.rest.skel.KeyValueDataStoreApiSkel;

/**
 *
 * @author yeti
 */
@Singleton
public class KeyValueDataStoreApi implements KeyValueDataStoreApiSkel {

	@GET
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	@Override
	public Response get(
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey) {
		try {
			GAEKVEntity entity = GAEKVEntity.get(entityKey,
				GAEToken.get(tokenKey)
				.getValidator()
				.expire()
				.fetch()
				.getUserID()
			);
			return new Response()
				.add(KVEntity.PARAM_KEY, entity.getKey())
				.add(KVEntity.PARAM_NAME, entity.getName())
				.add(KVEntity.PARAM_DATE, entity.getDate().getTime())
				.add(KVEntity.PARAM_VALUE, Base64.encodeBase64String(entity.getValue()));
		} catch (TokenArgumentException | TokenNotFoundException | TokenExpiredException | KVEntityNotFoundException ex) {
			return new Response(ex);
		}
	}

	@POST
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	@Override
	public Response put(
		String body,
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey,
		@QueryParam(value = KVEntity.PARAM_NAME) final String name) {
		try {
			GAEToken token = GAEToken
				.get(tokenKey)
				.getValidator()
				.expire()
				.fetch();
			new GAEKVEntity(entityKey, token.getUserID(), name, Base64.decodeBase64(body))
				.put();
			return new Response();
		} catch (TokenArgumentException | TokenNotFoundException | TokenExpiredException e) {
			return new Response(e);
		}
	}

	@DELETE
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	@Override
	public Response delete(
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey) {
		try {
			GAEKVEntity.delete(entityKey, GAEToken
				.get(tokenKey)
				.getValidator()
				.expire()
				.fetch()
				.getUserID()
			);
			return new Response();
		} catch (TokenArgumentException | TokenNotFoundException | TokenExpiredException | KVEntityNotFoundException e) {
			return new Response(e);
		}
	}

}
