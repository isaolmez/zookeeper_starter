package com.isa.zk.operations;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class CreateGroup implements Watcher {
	private static final int SESSION_TIMEOUT = 50000;
	private ZooKeeper zk;
	private CountDownLatch connectedSignal = new CountDownLatch(1);

	public void connect(String host) throws InterruptedException, IOException {
		zk = new ZooKeeper(host, SESSION_TIMEOUT, this);
		connectedSignal.await();
	}

	public void create(String znodeName) throws KeeperException, InterruptedException {
		String path = "/" + znodeName;
		String createdPath = zk.create(path, "isa".getBytes(Charset.forName("UTF8")), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("Created node: " + createdPath);
	}

	public void close() throws InterruptedException {
		zk.close();
	}

	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			connectedSignal.countDown();
		}
	}

	public static void main(String[] args) throws Exception {
		CreateGroup createGroup = new CreateGroup();
		createGroup.connect("localhost");
		createGroup.create("zoo");
		createGroup.close();
	}

}
