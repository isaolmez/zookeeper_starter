package com.isa.zk.operations;

import java.util.List;

import org.apache.zookeeper.KeeperException;

public class DeleteGroup extends ConnectionWatcher {
	
	public void delete(String groupName) throws InterruptedException, KeeperException {
		String path = "/" + groupName;
		try {
			List<String> children = zk.getChildren(path, false);
			for (String child : children) {
				zk.delete(path + "/" + child, -1);
			}

			zk.delete(path, -1);
		} catch (KeeperException.NoNodeException e) {
			System.out.printf("Group %s does not exist\n", groupName);
		}
	}

	public static void main(String[] args) throws Exception{
		DeleteGroup deleteGroup = new DeleteGroup();
		deleteGroup.connect("localhost");
		deleteGroup.delete("zoo");
		deleteGroup.close();
	}
}
