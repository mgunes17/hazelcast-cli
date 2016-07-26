package command.collection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.cli.CLI;
import com.hazelcast.core.HazelcastInstance;

public abstract class CollectionOperation {
	private static Logger logger = LoggerFactory.getLogger(CollectionOperation.class);
	private Map<String, Runnable> commandList;
	private HazelcastInstance instance;
	private Class<?> classType;
	private ObjectMapper objectMapper;
	
	public abstract void createCommandList();
	public abstract void setCollection();
	
	public CollectionOperation() { 
		objectMapper = new ObjectMapper();
		createCommandList();
	}
	
	public void runDefined(){
		if(getCommandList().containsKey(CLI.command[1])){
			logger.info(CLI.command[1] + " is running");
			getCommandList().get(CLI.command[1]).run();
		}
		else{
			logger.info("Command " + CLI.command[1] + " not found");
			System.out.println("command not found");
		}
	}
	
	public void displayObjectFields(String objectAsString){
		try{
			logger.info("Object's fields are splitting");
			String[] fields = objectAsString.split(",");
			
			for(String s : fields ){
				System.out.println(s);
			}	
		}
		catch(IndexOutOfBoundsException e){
			logger.warn(e.getMessage());
			System.out.println("Index is invalid");
		}
	}
	
	//getter-setter
	public Class<?> getClassType() {
		return classType;
	}
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	public Map<String, Runnable> getCommandList() {
		return commandList;
	}
	public void setCommandList(Map<String, Runnable> commandList) {
		this.commandList = commandList;
	}
	public HazelcastInstance getInstance() {
		return instance;
	}
	public void setInstance(HazelcastInstance instance) {
		this.instance = instance;
	}
}
