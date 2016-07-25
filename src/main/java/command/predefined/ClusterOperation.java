package command.predefined;

import java.util.HashMap;
import java.util.Map;

import org.github.jamm.MemoryMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Member;

public class ClusterOperation extends PreDefinedOperations{
	private static Logger logger = LoggerFactory.getLogger(ClusterOperation.class);
	
	public ClusterOperation() {
		super();
		setCommandList(createCommandList());
	}
	
	@Override
	public Map<String, Runnable> createCommandList(){
		Map<String, Runnable> map = new HashMap<String, Runnable>();
		
		logger.info("Cluster command list is creating");
		
		map.put("membercount", () -> getMembersCount());
		map.put("members", () -> printMembers());
		map.put("info", () -> getClusterInfo());
		map.put("collections", () -> printCollections(1, "B"));
		map.put("maps", () -> printCollections("map"));
		map.put("lists", () -> printCollections("list"));
		map.put("queues", () -> printCollections("queue"));
		map.put("sets", () -> printCollections("set"));
		map.put("collections b", () -> printCollections(1, "B"));
		map.put("collections kb", () -> printCollections(1024, "KB"));
		map.put("collections mb", () -> printCollections(1024*1024, "MB"));
		map.put("collections gb", () -> printCollections(1024*1024*1024, "GB"));
		
		return map;
	}

	public void getClusterInfo(){
		System.out.print("Node sayısı:");
		getMembersCount();
		printMembers();
		printCollections(1, "B");
	}
	
	public void getMembersCount(){
		System.out.println(getInstance().getCluster().getMembers().size());
	}
	
	public void printMembers(){
		//System.out.println("------------Members--------------");
		
		for(Member member : getInstance().getCluster().getMembers()){
			System.out.println(member.toString());
		}
	}
	
	public void printCollections(int sizeType, String type){;
		//System.out.println("------------Collections--------------");
		System.out.printf("%-20.20s %-20.20s %-20.20s\n","Type","Name","Size("+type+")" );
		System.out.printf("%-20.20s %-20.20s %-20.20s\n","-----","-----","-----" );
		
		for(DistributedObject c : getInstance().getDistributedObjects()){
        	System.out.printf("%-20.20s %-20.20s", c.getServiceName(), c.getName());
        	MemoryMeter meter = new MemoryMeter();
            System.out.printf((double)meter.measureDeep(c)/sizeType +"\n");
		}
	}

	public void printCollections(String type){
		//System.out.println("------------Collections--------------");
		System.out.printf("%-20.20s %-20.20s %-20.20s\n","Type","Name","Size("+type+")" );
		
		for(DistributedObject c : getInstance().getDistributedObjects()){
			if(c.getServiceName().contains(type)){
				System.out.printf("%-20.20s, %-20.20s", c.getServiceName(), c.getName());
	        	MemoryMeter meter = new MemoryMeter();
	            System.out.printf((double)meter.measureDeep(c) +"\n");
			}
		}
	}

}
