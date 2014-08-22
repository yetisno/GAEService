/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.rest;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.yetiz.ds.Token;
import org.yetiz.ds.User;
import org.yetiz.gae.Response;
import org.yetiz.gae.ds.GAEToken;
import org.yetiz.gae.ds.GAEUser;
import org.yetiz.gae.exception.TokenArgumentException;
import org.yetiz.gae.exception.TokenExpiredException;
import org.yetiz.gae.exception.TokenNotFoundException;
import org.yetiz.gae.exception.UserArgumentException;
import org.yetiz.gae.exception.UserNotFoundException;
import org.yetiz.gae.exception.UserUNAuthorizedException;
import org.yetiz.gae.rest.skel.TokenApiSkel;
import org.yetiz.gae.validator.UserValidator;

/**
 *
 * @author yeti
 */
@Singleton
public class TokenApi implements TokenApiSkel {

	@GET
	@Path("/")
	@Override
	public Response getToken(
		@QueryParam(value = User.PARAM_USERID) final String userID,
		@QueryParam(value = User.PARAM_PASSPHRASE) final String passphrase) {
		try {
			GAEUser.get(userID)
				.getValidator()
				.authorize(passphrase);
			GAEToken token = new GAEToken(userID);
			token.put();
			return new Response()
				.add(Token.PARAM_KEY, token.getKey().getName())
				.add(Token.PARAM_EXPIRE, token.getExpire());
		} catch (UserNotFoundException | UserArgumentException | UserUNAuthorizedException e) {
			return new Response(e);
		}
	}

	@POST
	@Path("/{" + Token.PARAM_KEY + "}")
	@Override
	public Response renewToken(
		@PathParam(Token.PARAM_KEY) String tokenKey) {
		try {
			GAEToken token = GAEToken.get(tokenKey).getValidator().expireOneMonth().fetch();
			UserValidator.exist(token.getUserID());
			token.delete();
			token = new GAEToken(token.getUserID());
			token.put();
			return new Response()
				.add(Token.PARAM_KEY, token.getKey().getName())
				.add(Token.PARAM_EXPIRE, token.getExpire());
		} catch (TokenArgumentException | TokenNotFoundException | TokenExpiredException | UserNotFoundException | UserArgumentException e) {
			return new Response(e);
		}
	}
}
