/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.rest;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.yetiz.ds.Token;
import org.yetiz.ds.User;
import org.yetiz.gae.RESTfulApplication;
import org.yetiz.gae.Response;
import org.yetiz.gae.ds.GAEToken;
import org.yetiz.gae.ds.GAEUser;
import org.yetiz.gae.exception.ServiceArgumentException;
import org.yetiz.gae.exception.ServiceUNAuthorizedException;
import org.yetiz.gae.exception.TokenArgumentException;
import org.yetiz.gae.exception.TokenExpiredException;
import org.yetiz.gae.exception.TokenNotFoundException;
import org.yetiz.gae.exception.TokenUNAuthorizedException;
import org.yetiz.gae.exception.UserArgumentException;
import org.yetiz.gae.exception.UserDuplicateException;
import org.yetiz.gae.exception.UserNotFoundException;
import org.yetiz.gae.rest.skel.UserApiSkel;
import org.yetiz.gae.validator.AdminServiceValidator;

/**
 *
 * @author yeti
 */
@Singleton
public class UserApi implements UserApiSkel {

	public UserApi() {
	}

	@GET
	@Path("")
	public String getList() {
		return "";
	}

	@GET
	@Path("/{" + User.PARAM_USERID + "}")
	@Override
	public Response getUser(
		@QueryParam(value = Token.PARAM_KEY)
		final String tokenKey,
		@PathParam(User.PARAM_USERID) String userID) {
		try {
			return new Response().add(User.PARAM_USERID,
				GAEToken.get(tokenKey)
				.getValidator()
				.expire()
				.authorize(GAEUser.get(userID)).fetch().getUserID());
		} catch (TokenArgumentException | TokenNotFoundException | TokenExpiredException | UserNotFoundException | UserArgumentException | TokenUNAuthorizedException ex) {
			return new Response(ex);
		}
	}

	@POST
	@Path("/{" + User.PARAM_USERID + "}")
	@Override
	public Response addUser(
		@PathParam(User.PARAM_USERID) String userID,
		@QueryParam(value = RESTfulApplication.PARAM_ADKEY) final String adKey,
		@QueryParam(value = User.PARAM_PASSPHRASE) final String passphrase) {
		try {
			AdminServiceValidator.adKey(adKey);
			try {
				GAEUser.get(userID);
			} catch (UserNotFoundException | UserArgumentException e) {
				GAEUser user = new GAEUser(userID, passphrase);
				user.put();
				GAEToken token = new GAEToken(userID);
				token.put();
				return new Response()
					.add(Token.PARAM_KEY, token.getKey().getName())
					.add(Token.PARAM_EXPIRE, token.getExpire());
			}
			throw new UserDuplicateException();
		} catch (ServiceUNAuthorizedException | UserDuplicateException | ServiceArgumentException e) {
			return new Response(e);
		}
	}

	@DELETE
	@Path("/{" + User.PARAM_USERID + "}")
	@Override
	public Response deleteUser(
		@QueryParam(value = RESTfulApplication.PARAM_ADKEY) final String adKey,
		@PathParam(User.PARAM_USERID) String userID) {
		//require adk, ui
		try {
			AdminServiceValidator.adKey(adKey);
			GAEUser.get(userID)
				.delete();
			return new Response();
		} catch (ServiceUNAuthorizedException | UserNotFoundException | UserArgumentException | ServiceArgumentException e) {
			return new Response(e);
		}
	}
}
