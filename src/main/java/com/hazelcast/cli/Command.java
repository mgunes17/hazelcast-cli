package com.hazelcast.cli;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;

import command.collection.ListOperation;
import command.collection.MapOperation;
import command.collection.QueueOperation;
import command.collection.SetOperation;
import command.predefined.PreDefinedCommand;

public class Command {
	private static Logger logger = LoggerFactory.getLogger(Command.class);
    private HazelcastInstance instance;
	
	public Command(HazelcastInstance instance){
		this.instance = instance;
	}
	
	private enum CollectionType{
		m, q, l, s;
	}
	
	public void process(String input){
		CLI.command = input.split(" ");
		logger.info("Input has split");
		
		PreDefinedCommand pdc = new PreDefinedCommand();
		
		if(pdc.controlCommand(CLI.command[0])){//predefined command
			logger.info("It is a predefined command");
			pdc.directCommand(instance);
			
		}
		else{ //no predefined command
			logger.info("It is not a predefined command");
			if(CLI.nameSpace == null){
				logger.info("Namespace is null");
				System.out.println("Lütfen name space tanımlayın");
				System.out.println("ns set <isim>");
			}
			else{
				//find collection type
				try{
					CollectionType type = CollectionType.valueOf(CLI.command[0]);
					switch(type){
						case m:
							logger.info("It is a map command");
							if(isExistCollectionName("map") || createDecision("map")){ 
								MapOperation mo = new MapOperation(instance);
								mo.runDefined();
							}
							else
								System.out.println("map is not created");	
							break;
						case q:
							logger.info("It is a queue command");
							if(isExistCollectionName("queue") || createDecision("queue")){ 
								QueueOperation qo = new QueueOperation(instance);
								qo.runDefined();
							}
							else
								System.out.println("queue is not created");
							break;
						case l:
							logger.info("It is a list command");
							if(isExistCollectionName("list") || createDecision("list")){ 
								ListOperation lo = new ListOperation(instance);
								lo.runDefined();
							}
							else
								System.out.println("list is not created");							
							break;
						case s:
							logger.info("It is a set command");
							if(isExistCollectionName("set") || createDecision("set")){
								SetOperation lo = new SetOperation(instance);
								lo.runDefined();
							}
							else
								System.out.println("set is not created");
							break;

					}
				}
				catch(IllegalArgumentException e){
					logger.info("Command is invalid");
					System.out.println("command is invalid");
				}
			}			
		}
	}
	
	public boolean createDecision(String collectionType){
		System.out.println("There is no "+ collectionType + " named "+ CLI.nameSpace);
		System.out.println("Would you like to create? (y/n)");
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String decision = in.nextLine();
		
		if(decision.equalsIgnoreCase("y"))
			return true;
		else
			return false;
	}
	
	public boolean isExistCollectionName(String type){
		//Collection<DistributedObject> distributedObject = instance.getDistributedObjects();
		int i = 0;
		logger.info("Collection name is searching");
		for(DistributedObject object : instance.getDistributedObjects()){
			if(object.getName().equalsIgnoreCase(CLI.nameSpace)){
				if(object.getClass().getName().contains(type)){
					i++;
				}
			}
		}
		
		if(i == 0){
			logger.info("Collection name not found");
			return false;
		}
			
		else{
			logger.info("Connection name found");
			return true;
		}
			
		//int i = 0;
		/*Iterator<DistributedObject> iterator = distributedObject.iterator();
		
		while(iterator.hasNext() && !iterator.next().getName().equalsIgnoreCase(Client.nameSpace) 
				&& iterator.next().getClass().getName().contains(type)){
			System.out.println(iterator.next().getName() + " " + iterator.next().getClass().getName());
			i++;
		}
		
		if(i == distributedObject.size())
			return false;
		else
			return true;*/
	}
}
