package agh.lab.zad2;

import java.io.IOException;

import org.json.JSONException;

import jdk.nashorn.api.scripting.JSObject;

public class Main {
	public static void main(String[] args) throws IOException, JSONException {
		ArgumentsParser argParser = new ArgumentsParser ();
		Arguments arguments = null;
		try{
		 arguments=argParser.parse(args);
		}catch (IllegalArgumentException e){
			System.out.println(e);
			System.exit(1);
			
			
		}
		DataDownloader dataDownloader = new DataDownloader(arguments);
		JsonData jsonData= dataDownloader.downloadAllData(arguments);
		//properInformation information = jsonData.extractInformation();
	//	information.printInformation();
	}
}
