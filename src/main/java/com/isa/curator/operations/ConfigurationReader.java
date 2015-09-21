package com.isa.curator.operations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigurationReader {

	public static void main(String[] args) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		final CuratorFramework client = CuratorFrameworkFactory.builder()
											.connectString("localhost:2181")
											.retryPolicy(retryPolicy)
											.connectionTimeoutMs(100000)
											.sessionTimeoutMs(100000)
											.namespace("logformat").build();
		
		client.start();
		
		byte[] dataBytes = client.getData().watched().forPath("/click");
//		String data = new String(dataBytes);
//		final Gson gson = new Gson();
//		String[] fromJson = gson.fromJson(data, String[].class);
//		for (String str : fromJson) {
//			System.out.println(str);
//		}
		
		Map map = (Map<String, Object>)deserialize(dataBytes);
		
		Object fieldsObj = map.get("fields");
		String[] fields = null;
		if(fieldsObj instanceof String[]){
			fields = (String[])fieldsObj;
		}
		
		Object datesObj = map.get("dates");
		FieldCountElement[] dates = null;
		if(datesObj instanceof FieldCountElement[]){
			dates = (FieldCountElement[])datesObj;
		}
		System.out.println(Arrays.asList(fields));
		System.out.println(Arrays.asList(dates));
		
		client.getCuratorListenable().addListener(new CuratorListener() {

			public void eventReceived(CuratorFramework arg0, CuratorEvent event) throws Exception {
				System.out.println(event.getWatchedEvent().getType() == EventType.NodeDataChanged);
				if (event.getWatchedEvent().getType() == EventType.NodeDataChanged) {
					byte[] dataBytes = client.getData().watched().forPath("/click");
//					String data = new String(dataBytes);
//					System.out.println("Data changed: " + data);
//					String[] fromJson = gson.fromJson(data, String[].class);
//					for (String str : fromJson) {
//						System.out.println(str);
//					}
					
					Map map = (Map<String, Object>)deserialize(dataBytes);
					
					Object fieldsObj = map.get("fields");
					String[] fields = null;
					if(fieldsObj instanceof String[]){
						fields = (String[])fieldsObj;
					}
					
					Object datesObj = map.get("dates");
					FieldCountElement[] dates = null;
					if(datesObj instanceof FieldCountElement[]){
						dates = (FieldCountElement[])datesObj;
					}
					
					System.out.println(Arrays.asList(fields));
					System.out.println(Arrays.asList(dates));
				}
			}
		});
		
		Thread.sleep(1000000);
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
