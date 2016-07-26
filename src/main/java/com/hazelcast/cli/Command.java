package com.hazelcast.cli;

import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;

import command.collection.CollectionOperation;
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
		m("map", new MapOperation()),
		q("queue", new QueueOperation()),
		l("list", new ListOperation()),
		s("set", new SetOperation());
		
		private String type;
		private CollectionOperation cOperation;
		
		private CollectionType(String type, CollectionOperation cOperation){
			this.type = type;
			this.cOperation = cOperation;
		}
		
		public CollectionOperation getCollectionOperation(){
			return cOperation;
		}
		public String getType(){
			return type;
		}
	}
	
	public void process(String input){
		CLI.command = input.split(" ");
		logger.info("Input has split");
		
		PreDefinedCommand pdc = new PreDefinedCommand();
		
		if(pdc.controlCommand(CLI.command[0])){//predefined command
			logger.info("It is a predefined command");
			pdc.directCommand(instance);
		}
		else if(CLI.nameSpace == null){
			logger.info("Namespace is null");
			System.out.println("Lütfen name space tanımlayın");
			System.out.println("ns set <isim>");
		}
		else
			collectionCommand();
	}
	
	public void collectionCommand(){
		try{
			CollectionType type = CollectionType.valueOf(CLI.command[0]);
			logger.info("It is a " + type.getType() + " command");
			
			if(isExistCollectionName(type.getType()) || createDecision(type.getType())){
				CollectionOperation co = type.getCollectionOperation();
				co.setInstance(instance);
				co.setCollection();
				co.runDefined();
			}
			else{
				System.out.println(type.getType() + " is not created");
			}
		}
		catch(IllegalArgumentException e){
			logger.info("Command is invalid");
			System.out.println("command is invalid");
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
		Collection<DistributedObject> distributedObject = instance.getDistributedObjects();
		int i = 0;
		logger.info("Collection name is searching");

		Iterator<DistributedObject> iterator = distributedObject.iterator();
		
		while(iterator.hasNext() && !(iterator.next().getName().equalsIgnoreCase(CLI.nameSpace)) 
				&& iterator.next().getClass().getName().contains(type)){
		}
		
		if(iterator.hasNext())
			return true;
		else
			return false;
	}
}
