package command.predefined;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.cli.CLI;

public class NameSpaceOperation extends PreDefinedOperations{
	private static Logger logger = LoggerFactory.getLogger(NameSpaceOperation.class);
	
	public NameSpaceOperation(){
		setCommandList(createCommandList());
	}
	
	public Map<String, Runnable> createCommandList(){
		Map<String, Runnable> command = new HashMap<String, Runnable>();
		
		logger.info("NameSpace command list is creating");
		
		command.put("set", () -> setNameSpace(CLI.command[2]));
		command.put("get", () -> getNameSpace());
		command.put("reset", () -> reset());
		
		return command;
	}
	
	public void reset(){
		logger.info("Namespace is resetting");
		CLI.nameSpace = null;
	}
	
	public void getNameSpace(){
		System.out.println(CLI.nameSpace);

	}
	
	public void setNameSpace(String nameSpace){
		logger.info("Namespace is setting");
		CLI.nameSpace = nameSpace;
		System.out.println(true);
		findCollectionName(nameSpace);
	}
}
