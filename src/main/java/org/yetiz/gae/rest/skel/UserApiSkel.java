/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.rest.skel;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.yetiz.ds.Token;
import org.yetiz.ds.User;
import org.yetiz.gae.RESTfulApplication;
import org.yetiz.gae.Response;

/**
 *
 * @author yeti
 */
@Path("users")
@Produces({MediaType.APPLICATION_JSON})
public interface UserApiSkel {

	@GET
	@Path("/{" + User.PARAM_USERID + "}")
	public Response getUser(
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(User.PARAM_USERID) String userID);

	@POST
	@Path("/{" + User.PARAM_USERID + "}")
	public Response addUser(
		@PathParam(User.PARAM_USERID) String userID,
		@QueryParam(value = RESTfulApplication.PARAM_ADKEY) final String adKey,
		@QueryParam(value = User.PARAM_PASSPHRASE) final String passphrase);

	@DELETE
	@Path("/{" + User.PARAM_USERID + "}")
	public Response deleteUser(
		@QueryParam(value = RESTfulApplication.PARAM_ADKEY) final String adKey,
		@PathParam(User.PARAM_USERID) String userID);
}
