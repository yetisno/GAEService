/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.rest.skel;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.yetiz.ds.Token;
import org.yetiz.ds.User;
import org.yetiz.gae.Response;

/**
 *
 * @author yeti
 */
@Path("tkn")
	@Produces({MediaType.APPLICATION_JSON})
public interface TokenApiSkel {

	@GET
	@Path("/")
	public Response getToken(
		@QueryParam(value = User.PARAM_USERID) final String userID,
		@QueryParam(value = User.PARAM_PASSPHRASE) final String passphrase);

	@POST
	@Path("/{" + Token.PARAM_KEY + "}")
	public Response renewToken(
		@PathParam(Token.PARAM_KEY) String tokenKey);
}
