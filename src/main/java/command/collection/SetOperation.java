package command.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.cli.CLI;

public class SetOperation extends CollectionOperation{
	private static Logger logger = LoggerFactory.getLogger(SetOperation.class);
	private Set<Object> set;
	
	public SetOperation() {
		super();
	}

	public void setCollection(){
		set = getInstance().getSet(CLI.nameSpace);
	}
	
	@Override
	public void createCommandList() {
		Map<String, Runnable> map = new HashMap<String, Runnable>();
		
		logger.info("Set command list is creating");
		
		map.put("add", () -> add(CLI.command[2]));
		map.put("getall", () -> getAllValue());
	    map.put("remove", () -> removeValue(CLI.command[2]));
		map.put("size", () -> getSize());
		map.put("clear", () -> clearSet());
		
		setCommandList(map);
	}
	
	public void clearSet(){
		System.out.println("Are you sure (y/n)");
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String decision = in.nextLine();
		
		if(decision.equalsIgnoreCase("y")){
			set.clear();
			System.out.println(true);
			logger.info(CLI.nameSpace + " list was cleared");
		}
		else{
			System.out.println("It has not cleared");
		}
	}
	
	public void removeValue(Object object){
		if(set.contains(object)){
			logger.warn("Object is removing");
			set.remove(object);
			System.out.println(true);
		}
		else{
			System.out.println("Obje bulunamadı");
		}
	}
	
	public void add(Object value){
		logger.info("Object is being added");
		set.add(value);
		System.out.println(true);
	}
	
	public void getAllValue(){
		Iterator<?> iterator = set.iterator();
		while ( iterator.hasNext() ) { 
			try {
				displayObjectFields(getObjectMapper().writeValueAsString(iterator.next()));
			} 
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	public void getSize(){
		System.out.println(set.size());
	}
}
