package command.predefined;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.cli.CLI;
import com.hazelcast.core.HazelcastInstance;

public class PreDefinedCommand {
	private static Logger logger = LoggerFactory.getLogger(LockOperation.class);
	private Map<String, Runnable> commandList;
	private HazelcastInstance instance;
	
	public PreDefinedCommand(){
		commandList = new HashMap<String, Runnable>();		
		logger.info("Predefined command list is creating");
		commandList.put("cluster", () -> createPreDefinedOperations(new ClusterOperation()));
		commandList.put("ns", () -> createPreDefinedOperations(new NameSpaceOperation()));
		commandList.put("lock", () -> createPreDefinedOperations(new LockOperation()));
	}

	public boolean controlCommand(String control){
		if(commandList.containsKey(control))
			return true;
		else
			return false;
	}
	
	public void directCommand(HazelcastInstance instance ){
		this.instance = instance;
		logger.info(CLI.command[0] + " is running");
		commandList.get(CLI.command[0]).run();
	}
	
	public void createPreDefinedOperations(PreDefinedOperations pdco){
		pdco.setInstance(instance);
		logger.info("Predefined command is running");
		pdco.run();
	}
}
