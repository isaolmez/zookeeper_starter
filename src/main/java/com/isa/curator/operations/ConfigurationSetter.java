package com.isa.curator.operations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.google.gson.Gson;

public class ConfigurationSetter {

	public static void main(String[] args) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		final CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("localhost:2181")
				.retryPolicy(retryPolicy)
				.connectionTimeoutMs(100000)
				.sessionTimeoutMs(100000)
				.namespace("logformat").build();

		client.start();
		
//		client.create().forPath("/click", "deneme2".getBytes());
		String[] fields = { "field1", "field2" };
		
		Map<String, Object> clickLogFormat = new HashMap<>();
		FieldCountElement date1 = new FieldCountElement();
		date1.setDatetime(System.currentTimeMillis());
		date1.setMaxFieldCount(60);
		date1.setMinFieldCount(65);
		FieldCountElement date2 = new FieldCountElement();
		date2.setDatetime(System.currentTimeMillis());
		date2.setMaxFieldCount(65);
		date2.setMinFieldCount(67);
		FieldCountElement[] elements = {date1, date2};
		
		clickLogFormat.put("fields", fields);
		clickLogFormat.put("dates", elements );
		
		
		byte[] serialize = serialize(clickLogFormat);
//		Gson gson = new Gson();
//		String json = gson.toJson(clickLogFormat);
		client.create().forPath("/impressions", serialize);
	}
	
	public static byte[] serialize(Object obj) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] outputBytes = bos.toByteArray();
			return outputBytes;

		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
			
			try {
				bos.close();
			} catch (IOException ex) {
				throw ex;
			}
		}
	}
	
	public static Object deserialize(byte[] inputBytes) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Object obj = in.readObject();
			return obj;
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				throw ex;
			}
		}
	}
	
}
