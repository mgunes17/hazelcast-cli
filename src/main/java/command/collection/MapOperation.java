package command.collection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.cli.CLI;
import com.hazelcast.core.IMap;

public class MapOperation extends CollectionOperation{
	private static Logger logger = LoggerFactory.getLogger("ListOperation");
	private IMap<Object, Object> map;
	
	public MapOperation() {
		super();
	}
	
	public void setCollection(){
		map = getInstance().getMap(CLI.nameSpace);
	}
	
	@Override
	public void createCommandList() {
        Map<String, Runnable> map = new HashMap<String, Runnable>();
        
        logger.info("Map Collection command list is creating");
		
		map.put("put", () -> addNew(CLI.command[2], CLI.command[3]));
		map.put("get", () -> getValue(CLI.command[2]));
		map.put("getall", () -> getAllValue());
		map.put("remove", () -> remove(CLI.command[2]));
		map.put("size", () -> getSize());
		map.put("keys", () -> getKeys());
		map.put("values", () -> getValues());
		map.put("destroy", () -> destroyMap());
		map.put("clear", () -> clearMap());
		map.put("lock", () -> lockKey(CLI.command[2]));
		map.put("trylock", () -> tryLockKey(CLI.command[2]));
		map.put("unlock", () -> unlockKey(CLI.command[2]));
		
		setCommandList(map);
	}
	
	public void tryLockKey(Object key){
		map.tryLock(key);
		System.out.println(true);
		logger.info("Method tryLockKey was called. Key:" + key + " namespace:" + CLI.nameSpace);
	}
	
	public void unlockKey(Object key){
		map.unlock(key);
		System.out.println(true);
		logger.info("Method unlockKey was called. Key:" + key + " namespace:" + CLI.nameSpace);
	}
	
	public void lockKey(Object key){
		map.lock(key);
		System.out.println(true);
		logger.info("Method lockKey was called. Key:" + key + " namespace:" + CLI.nameSpace);
	}
	
	public void destroyMap(){
		map.destroy();
		System.out.println(true);
		logger.info("Map was destroyed. Namespace:" + CLI.nameSpace);
	}
	
	public void clearMap(){
		map.clear();
		System.out.println(true);
		logger.info("Map was cleared. Namespace:" + CLI.nameSpace);
	}
	
	public void getValues(){
		for(Object key : map.keySet()){
			System.out.println(map.get(key));
		}
	}
	
	public void getKeys(){
		for(Object key : map.keySet()){
			System.out.println(key);
		}
	}
	
	public void getAllValue(){
		for(Object key : map.keySet()){
			try {
				displayObjectFields(getObjectMapper().writeValueAsString(map.get(key)));
			} 
			catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
	
	public void getSize(){
		System.out.println(map.size());
	}
	
	public void addNew(Object key, Object value){
		map.put(key, value);
		System.out.println(true);
		logger.info("Method addNew was called for map. Key:" + key + " value:" + value + " namespace:" + CLI.nameSpace);
	}
	
	public void getValue(String key){
		try {
			displayObjectFields(getObjectMapper().writeValueAsString(map.get(key)));
		} 
		catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public void remove(Object key){
		if(map.isLocked(key)){
			System.out.println(false);
			logger.info("Method remove was called for map BUT KEY IS LOCKED. Key:" + key +" namespace:" + CLI.nameSpace);
		}
		else{
			map.remove(key);
			System.out.println(true);
			logger.info("Method remove was called for map. ey:" + key +" namespace:" + CLI.nameSpace);
		}	
	}
}
