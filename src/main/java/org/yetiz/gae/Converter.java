/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae;

import org.yetiz.ds.KVEntity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yeti
 */
public class Converter {

	public static byte[] objectToByteArray(Object obj) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			new ObjectOutputStream(bos).writeObject(obj);
			return bos.toByteArray();
		} catch (IOException ex) {
			Logger.getLogger(KVEntity.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static Object byteArrayToObject(byte[] data) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			return new ObjectInputStream(bis).readObject();
		} catch (Exception ex) {
			Logger.getLogger(KVEntity.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
