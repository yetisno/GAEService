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
import org.yetiz.ds.KVEntity;
import org.yetiz.gae.Response;
import org.yetiz.ds.Token;

/**
 *
 * @author yeti
 */
@Path("kv")
@Produces({MediaType.APPLICATION_JSON})
public interface KeyValueDataStoreApiSkel {

	@GET
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	public Response get(
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey);

	@POST
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	public Response put(
		String body,
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey,
		@QueryParam(value = KVEntity.PARAM_NAME) final String name);

	@DELETE
	@Path("/{" + KVEntity.PARAM_KEY + "}")
	public Response delete(
		@QueryParam(value = Token.PARAM_KEY) final String tokenKey,
		@PathParam(KVEntity.PARAM_KEY) String entityKey);

}
