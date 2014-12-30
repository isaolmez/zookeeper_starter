package configurationservice;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigWatcher implements Watcher{

	private ActiveKeyValueStore store;
	
	public ConfigWatcher(String host) throws InterruptedException, IOException {
		store = new ActiveKeyValueStore();
		store.connect(host);
	}
	
	public void displayConfig() throws KeeperException, InterruptedException{
		String value = store.read(ConfigUpdater.PATH, this);
		System.out.printf("Read %s as %s \n", ConfigUpdater.PATH, value);
	}

	public void process(WatchedEvent event) {
		if(event.getType() == EventType.NodeDataChanged){
			try{
				displayConfig();
			}catch(InterruptedException e){
				System.err.println("Interrupted. Exiting.");
			}catch (KeeperException e) {
				System.err.printf("Keeper exception occured: %s. Exiting. \n", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ConfigWatcher configWatcher = new ConfigWatcher("localhost");
		configWatcher.displayConfig();
		
		Thread.sleep(Long.MAX_VALUE);
	}
	
}
