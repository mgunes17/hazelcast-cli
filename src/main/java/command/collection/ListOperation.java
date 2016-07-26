package command.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.cli.CLI;
import com.hazelcast.core.HazelcastInstance;

public class ListOperation extends CollectionOperation {
	private static Logger logger = LoggerFactory.getLogger(ListOperation.class);
	private List<Object> list;
	
	public ListOperation() { 
		super();
	}
	
	public void setCollection(){
		list = getInstance().getList(CLI.nameSpace);
	}
	
	@Override
	public void createCommandList(){
		Map<String, Runnable> map = new HashMap<String, Runnable>();
		
		logger.info("List command list is creating");
		
		map.put("add", () -> addNew(CLI.command[2]));
		map.put("set", () -> set(Integer.parseInt(CLI.command[2]), CLI.command[2]));
		map.put("get", () -> getValue(Integer.parseInt(CLI.command[2])));
		map.put("getall", () -> getAllValue());
		map.put("getmany", () -> getMany(CLI.command[2]));
		map.put("size", () -> getSize());
		map.put("contains", () -> contains(CLI.command[2]));
		map.put("remove", () -> remove(Integer.parseInt(CLI.command[2])));
		map.put("clear", () -> clear());
		
		setCommandList(map);
	}
	
	public void getMany(String parameter){
		String[] index = parameter.split(",");
		
		try{
			for(int i=0; i<index.length; i++){
				getValue(Integer.parseInt(index[i]));
			}
		}
		catch(Exception e){
			logger.error(e.getMessage());
			System.out.println("Command not found");
		}
	}
	
	public void clear(){
		System.out.println("Are you sure (y/n)");
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String decision = in.nextLine();
		
		if(decision.equalsIgnoreCase("y")){
			list.clear();
			System.out.println("Clear OK");
			logger.info(CLI.nameSpace + " list was cleared");
		}
		else{
			System.out.println("It has not cleared");
		}
		
	}
	
	public void remove(int index){
		try{
			list.remove(index);
			System.out.println(true);
			logger.info("Index " + index + " was removed from" + CLI.nameSpace + " list" );
		}
		catch(ArrayIndexOutOfBoundsException e){
			logger.error(e.getMessage());
			System.out.println("Index is invalid");
		}
	}
	
	public void contains(Object value){
		if(list.contains(value))
			System.out.println(true);
		else
			System.out.println(false);
	}
	
	public void set(int index, Object value){
		try{
			list.add(index, value);
			System.out.println(true);
			logger.info("Method set was called for index " + index + " and " + " object " + value.toString());
		}
		catch(ArrayIndexOutOfBoundsException e){
			logger.error(e.getMessage());
			System.out.println("Index is invalid");
		}
	}	
	
	public void getAllValue(){
		Iterator<?> iterator = list.iterator();
		
		while(iterator.hasNext()){
			try {
				displayObjectFields(getObjectMapper().writeValueAsString(iterator.next()));
			} 
			catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				System.out.println("JSONProcessiong Exception");
			}
		}
	}
	
	public void getSize(){
		System.out.println(list.size());
	}
	
	public void addNew(Object value){
		list.add(value);
		System.out.println(true);
		logger.info("Method addNew was called for object " + value.toString());
	}
	
	public void getValue(int index) {
		try {
			displayObjectFields(getObjectMapper().writeValueAsString(list.get(index)));
		} 
		catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			System.out.println("JSON processing exception");
		}
		catch (IndexOutOfBoundsException e) {
			logger.error(e.getMessage());
			System.out.println("Geçersiz index");
		}
	}
}
