package command.predefined;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.cli.CLI;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;

public abstract class PreDefinedOperations {
	private static Logger logger = LoggerFactory.getLogger(PreDefinedOperations.class);
	private HazelcastInstance instance;
	private Map<String, Runnable> commandList;
	
	public abstract Map<String, Runnable> createCommandList();
	
	public void run(){
		if(CLI.command.length == 3 && commandList.containsKey(CLI.command[1]+" "+CLI.command[2])){
			logger.info("command length is 3");
			commandList.get(CLI.command[1]+" "+CLI.command[2]).run();
			System.out.println("--");
		}
		else if(commandList.get(CLI.command[1]) != null){
			logger.info("command length is 2");
			commandList.get(CLI.command[1]).run();
		}
		else{
			logger.info("Command not found");
			System.out.println("command not found");
		}
	}
	
	public void findCollectionName(String name){
		for(DistributedObject distributedObject : instance.getDistributedObjects()){
			if(distributedObject.getName().equalsIgnoreCase(name)){
				System.out.println(distributedObject.getServiceName() + " " + distributedObject.getName());
			}
		}
	}
	
	//getter-setter
	public HazelcastInstance getInstance(){
		return instance;
	}
	public void setInstance(HazelcastInstance instance){
		this.instance = instance;
	}
	public Map<String, Runnable> getCommandList() {
		return commandList;
	}
	public void setCommandList(Map<String, Runnable> commandList) {
		this.commandList = commandList;
	}

}
