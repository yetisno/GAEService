/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 *
 * @author yeti
 */
public class GAEServiceProvider {

	private static final DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
	
	public static DatastoreService GetDatastoreService(){
		return dss;
	}
}
