package agh.lab.zad2;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import jdk.nashorn.api.scripting.JSObject;

public class Main {
	public static final Logger LOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException, JSONException {
		
		ArgumentsParser argParser = new ArgumentsParser ();
		Arguments arguments = null;
		try{
		 arguments=argParser.parse(args);
		}catch (IllegalArgumentException e){
			LOGGER.warn(e);
			System.exit(1);
		}
		
		LOGGER.info("Launched app with parameters: " + arguments.numberTerm + " " + arguments.firstname + " " + arguments.lastname );
		
		DataDownloader dataDownloader = new DataDownloader(arguments);
		JsonData jsonData= dataDownloader.downloadAllData();
		ProperInformation information = jsonData.extractInformation();	
		information.print();
		LOGGER.info("Program finished without any interuptions");
		
	}
}
