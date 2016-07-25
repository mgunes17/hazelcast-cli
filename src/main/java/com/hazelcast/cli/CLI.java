package com.hazelcast.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Created by emrah on 12/06/15.
 */
public class CLI {
	public static String nameSpace;
	public static String[] command;
	private static Logger logger = LoggerFactory.getLogger(CLI.class);
	private HazelcastInstance instance;
		
    public static void main(String[] args) throws Exception {

        String userHome=System.getProperty("user.home");
        OptionParser optionParser = new OptionParser();
        OptionSpec install = optionParser.accepts("install", "Install hz");
        OptionSpec start = optionParser.accepts("start", "Start hz");
        OptionSpec startMC = optionParser.accepts("startMC", "Start hz mancenter");
        OptionSpec hostName = optionParser.accepts("hostname").withRequiredArg().ofType(String.class).required();
        OptionSpec version = optionParser.accepts("version").withRequiredArg().ofType(String.class);
        OptionSpec optionClusterName = optionParser.accepts("clustername").withRequiredArg().ofType(String.class);
        OptionSpec optionNodeName = optionParser.accepts("nodename").withRequiredArg().ofType(String.class);
        OptionSpec optionConfigFile = optionParser.accepts("configfile").withRequiredArg().ofType(String.class);

        OptionSet result = optionParser.parse(args);

        String host = (String) result.valueOf(hostName);
        Properties properties = getProperties(userHome);
        String user = properties.getProperty(host + ".user");
        String hostIp = properties.getProperty(host + ".ip");
        int port = Integer.parseInt(properties.getProperty(host + ".port"));

        CommandBuilder commandBuilder = new CommandBuilder();

        if (result.has(install)) {
            if (!result.has(version)) {
                System.out.println("--version required");
                System.exit(-1);
            }
            String strVersion = (String) result.valueOf(version);
            String command = commandBuilder.wget(strVersion);
            System.out.println("Download started...");
            String output = SshExecutor.exec(user, hostIp,
                    port, command, false);
            System.out.println("Extracting...");
            String extractCommand = commandBuilder.extract();
            SshExecutor.exec(user, hostIp, port, extractCommand, false);

            String move = commandBuilder.move("hazelcast-" + strVersion, "hazelcast");
            SshExecutor.exec(user, hostIp, port, move, false);
            System.out.println("Installation completed...");

        } else if (result.has(start)) {
            String clusterName = (String) result.valueOf(optionClusterName);
            String nodeName = (String) result.valueOf(optionNodeName);
            String configFile = (String) result.valueOf(optionConfigFile);
            String cmd = commandBuilder.upload(user, hostIp, nodeName, configFile);
            System.out.println("Uploading config file...");
            Runtime.getRuntime().exec(cmd);
            System.out.println("Upload completed.");
            System.out.println("Starting instance...");
            String startCmd = commandBuilder.start("hazelcast-" + nodeName + ".xml");
            String pid = SshExecutor.exec(user, hostIp, port, startCmd, true);
            System.out.println("Instance started : " + pid);

        } else if(result.has(startMC)) {
            SshExecutor.exec(user, hostIp, port, commandBuilder.startMC(), false);
        }
        
       	CLI client = new CLI();
       	
		try{
			Config config = new Config();
			PropertiesFile po = new PropertiesFile("cli.properties");
			ClassLoader classLoader = po.loadClasses();
			
			config.setClassLoader(classLoader);
			client.instance = Hazelcast.newHazelcastInstance(config);
			
			System.out.print("hazelcast-cli>");
			Scanner in = new Scanner(System.in);
			CLI.logger.trace("Input is reading");
			String inputCommand = in.nextLine();
			
			Command command = new Command(client.instance);
			
			while(!inputCommand.equalsIgnoreCase("exit")){
				CLI.logger.info("Input is parsing");
				command.process(inputCommand);
				
				System.out.print("hazelcast-cli>");
				in = new Scanner(System.in);
				CLI.logger.info("Input is reading");
				inputCommand = in.nextLine();
				
			}
			
			in.close();
		}
		catch(Exception e){
			CLI.logger.error(e.getMessage());
		}
		finally{
			client.instance.shutdown();
			CLI.logger.info("Hazelcast instance is shutdown");
		}
    }

    private static Properties getProperties(String userHome) {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            String filename = "cli.properties";
            //input = CLI.class.getClassLoader().getResourceAsStream(filename);
            //load a properties file from class path, inside static method
            CLI.logger.info("cli.properties file is loading");
            prop.load(new InputStreamReader(new FileInputStream(userHome+"/cli.properties")));
            
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    CLI.logger.warn(e.getMessage());
                }
            }
        }
    }


}
