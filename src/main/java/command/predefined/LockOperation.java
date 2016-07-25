package command.predefined;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.cli.CLI;
import com.hazelcast.core.ILock;

public class LockOperation extends PreDefinedOperations{
	private static Logger logger = LoggerFactory.getLogger(LockOperation.class);
	
	public LockOperation(){
		super();
		setCommandList(createCommandList());
	}
	
	@Override
	public Map<String, Runnable> createCommandList() {
		Map<String, Runnable> map = new HashMap<String, Runnable>();
		
		logger.info("Lock command list is creating");
		
		map.put("lock", () -> lock(CLI.command[1]));
		map.put("trylock", () -> tryLock(CLI.command[1]));
		map.put("unlock", () ->unlock(CLI.command[1]));
		
		return map;
		
	}

	public void lock(String object){
		ILock lock = getInstance().getLock(object);
		if(!lock.isLocked()){
			logger.info("Object is locking");
			lock.lock();
			System.out.println(true);
		}
		else
			System.out.println(false);
	}
	
	public void unlock(String object){
		ILock lock = getInstance().getLock(object);
		if(lock.isLocked() && lock.isLockedByCurrentThread()) {
			logger.info("Object is unlocking");
			lock.unlock();	
			System.out.println(true);
		}
		else{
			System.out.println(false);
		}
	}
	
	public void tryLock(String object){
		ILock lock = getInstance().getLock(object);
		if(lock.tryLock()){
			logger.info("Object is locking");
			System.out.println(true);
		}
		else
			System.out.println(false);
	}
}
