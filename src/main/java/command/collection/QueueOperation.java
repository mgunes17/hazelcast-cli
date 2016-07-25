package command.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.cli.CLI;
import com.hazelcast.core.HazelcastInstance;

public class QueueOperation extends CollectionOperation{
	private static Logger logger = LoggerFactory.getLogger(QueueOperation.class);
	private Queue<Object> queue;

	public QueueOperation(HazelcastInstance instance){
		super(instance);
		setCommandList(createCommandList());
		this.queue = instance.getQueue(CLI.nameSpace);
	}
	
	@Override
	public Map<String, Runnable> createCommandList() {
		Map<String, Runnable> map = new HashMap<String, Runnable>();
		
		logger.info("Queue command list is creating");
		
		map.put("poll", () -> pollValue());
		map.put("size", () -> size());
		map.put("offer", () -> offerValue(CLI.command[2]));
		map.put("offermany", () -> offerMany(Integer.parseInt(CLI.command[1]), CLI.command[2]));
		map.put("pollmany", () -> pollMany(Integer.parseInt(CLI.command[2])));
		map.put("peek", () -> peekValue());
		map.put("clear", () -> clear() );
		
		return map;
	}
	
	public void clear(){
		System.out.println("Are you sure(y/n)");
		Scanner in = new Scanner(System.in);
		String decision = in.nextLine();
		
		if(decision.equalsIgnoreCase("y")){
			logger.info("Queue is clearing");
			queue.clear();
			System.out.println("Clear OK");
		}
		else{
			System.out.println("It has not cleared");
		}
		in.close();
	}
	
	public void peekValue(){
		logger.info("Queue is being peeked");
		System.out.println(queue.peek());
	}
	
	public void pollMany(int many){
		for(int i=0; i<many; i++){
			System.out.println(queue.poll());
			logger.info("Queue is being polling");
		}
	}
	
	public void offerValue(Object object){
		logger.info("Queue is being offering");
		queue.offer(object);
		System.out.println(true);
	}
	
	public void offerMany(int many, String offerValue){
		try{
			String[] values = offerValue.split(",");
			
			for(int i=0; i<many; i++){
				logger.info("Queue is being offering");
				queue.offer(values[i]);
				System.out.println(true);
			}
		}
		catch(Exception e){
			logger.warn(e.getMessage());
		}
	}
	
	public void offer(String value){
		try{
			logger.info("Queue is being offering");
			queue.offer(value);
		}
		catch(NullPointerException e ){
			logger.warn(e.getMessage());
			System.out.println("queue is empty");
		}
	}
	
	public void pollValue(){
		try {
			logger.info("Queue is being polling");
			displayObjectFields(getObjectMapper().writeValueAsString(queue.poll()));
		} 
		catch (JsonProcessingException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void size(){
		System.out.println(queue.size());
	}
}
