package com.hazelcast.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesFile {
	private static Logger logger = LoggerFactory.getLogger(PropertiesFile.class);
	private String propertiesFileName;
	private Properties properties;

	public PropertiesFile(String propertiesFileName){
		this.propertiesFileName = propertiesFileName;
		readProperties();
	}
	
	public void readProperties(){
		properties = new Properties();
		
		try{
			logger.info("Properties file is loading");
			properties.load(new FileInputStream(propertiesFileName));
		}
		catch(IOException e){
			logger.warn(e.getMessage());
			e.getMessage();
		}

	}
	
	public ClassLoader loadClasses() {
		try{
			logger.info("Jar is loading");
			URL[] urls = {
					new URL("jar:file:" + properties.get("jarpath") + "!/")
			};
			
			URLClassLoader ucl = URLClassLoader.newInstance(urls);
			return ucl;
		}
		catch(MalformedURLException e){
			logger.warn(e.getMessage());
			e.getMessage();
		}
		
		return null;
		
	}
}
