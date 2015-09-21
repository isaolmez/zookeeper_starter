package com.isa.zk.operations;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ConnectionWatcher implements Watcher{
	protected ZooKeeper zk;
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	private static final int SESSION_TIMEOUT = 5000;
	
	public void connect(String host) throws InterruptedException, IOException {
		zk = new ZooKeeper(host, SESSION_TIMEOUT, this);
		connectedSignal.await();
	}
	
	public void process(WatchedEvent event) {
		if(event.getState() == KeeperState.SyncConnected){
			connectedSignal.countDown();
			System.out.println("Connected");
		}
	}
	
	public void close() throws InterruptedException {
		zk.close();
	}

}
