package agh.lab.zad2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataDownloader {

	private static String addres = "https://api-v3.mojepanstwo.pl/dane/poslowie/";
	private static String pageSecond = "&page=2";
	private static String costs="?layers=wydatki";
	private static String trips="?layers=wyjazdy";
	private static String condition = "?limit=500&conditions[poslowie.kadencja]=";
	
	private ProperInformation information = new ProperInformation(); 
	private Arguments arguments ;
	
	private static String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		String cp;
		while ((cp = rd.readLine()) != null) {
			sb.append(cp);
		}
		return sb.toString();
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			// JSONArray jsonArray = json.getJSONArray("Dataobject");
			//System.out.println(jsonText.length());
			// System.out.println(jsonArray.length());
			//is.close();
			return json;
		} finally {
			is.close();
		}
		
	}
	
	public DataDownloader (Arguments arguments){
		this.arguments=arguments;
	}

	public JsonData downloadAllData(Arguments arguments) throws IOException, JSONException {

		ArrayList<JSONObject> allData = readAllDataforTerm();
		/*JSONObject politician =null;
		JSONObject politicianCostRepairs = null;
		*/
		Politician politician = extractPoliticianAndDownloadCostRepair(allData);
		/*try{
		
		politician = extractPolitician(allData);
		
		Integer id = politician.getInt("id");
		politicianCostRepairs = downloadCostRepair (id); 
		}catch (IllegalArgumentException e) {
			System.err.println(e);
		}*/
		
		ArrayList<Integer> politiciansIDs = extractIDs(allData);
		
		ArrayList<JSONObject> politiciansExpenses = downloadExpenses(politiciansIDs);
		ArrayList<JSONObject> politiciansTrips = downloadTrips(politiciansIDs);
		
	/*	jsData = new JsonData(allData);
		jsData.extractPolitician(arguments);
		jsData.extractIDs();*/
		/*
		double averagePoliticiansCost = averageCosts(jsData.getIDs());
		double politicianCostRepair = downloadAndCountCostRepairs(politician);*/
		
		Json
		return jsData;

	}
	
	
	private Politician extractPoliticianAndDownloadCostRepair(ArrayList<JSONObject> allData) throws JSONException, IOException {
		JSONObject politicianObject =null;
		JSONObject politicianCostRepairs = null;
		Politician politician =null;
		try{
		politicianObject = extractPolitician(allData);
		Integer id = politicianObject.getInt("id");
		politicianCostRepairs = downloadCostRepair (id); 
		politician = new Politician(politicianObject, politicianCostRepairs);
		}catch (IllegalArgumentException e) {
			System.err.println(e);
		}
		return politician;
		
		
	}

	private ArrayList<JSONObject> downloadTrips(ArrayList<Integer> politiciansIDs) throws IOException, JSONException {
		ArrayList<JSONObject> politiciansTrips= new ArrayList<JSONObject>(500);
		JSONObject tmpObject = null;
		for (Integer id : politiciansIDs) {
			tmpObject = readJsonFromUrl(addres+id.toString()+trips);
			politiciansTrips.add(tmpObject);
		}
		return politiciansTrips;
	}

	private ArrayList<JSONObject> downloadExpenses(ArrayList<Integer> politiciansIDs) throws IOException, JSONException {
		ArrayList<JSONObject> politiciansExpenses= new ArrayList<JSONObject>(500);
		JSONObject tmpObject = null;
		for (Integer id : politiciansIDs) {
			tmpObject = readJsonFromUrl(addres+id.toString()+costs);
			politiciansExpenses.add(tmpObject);
		}
		return politiciansExpenses;
	}

	private JSONObject downloadCostRepair(Integer id) throws IOException, JSONException {
		JSONObject politicianCost= readJsonFromUrl(addres + id.toString() + costs);
		return politicianCost;
	}

	public JSONObject extractPolitician(ArrayList<JSONObject> allData) throws JSONException {
		JSONObject politician =null;
		
		String firstname,lastname;
		for (int i = 0; i < allData.size(); i++) {
			firstname = (String) allData.get(i).getJSONObject("data").get("poslowie.imie_pierwsze");
			lastname = (String) allData.get(i).getJSONObject("data").get("poslowie.nazwisko");
			
//			System.out.println(firstname+lastname);
			if (arguments.firstname.equals(firstname) && arguments.lastname.equals(lastname )){
				politician = allData.get(i);
				System.out.println(politician);
				break;
			}
			
		}
		if(politician ==null)throw new IllegalArgumentException("Nie znaleziono posła o podanym imieniu i nazwisku");
		return politician;
	}
	
	public ArrayList<Integer> extractIDs(ArrayList<JSONObject> allData) throws JSONException {
		int id;
		ArrayList<Integer> IDs = new ArrayList<Integer>(500);
		for (JSONObject jsonObject : allData) {
			id =  jsonObject.getInt("id");
			IDs.add(id);
		}
		return IDs;
		
	}
	


	private double downloadAndCountCostRepairs(Integer id) throws IOException, JSONException {
		JSONObject politicianCost= readJsonFromUrl(addres + id.toString() + costs);
		JSONArray pCosts = parseCostRepairs(politicianCost);
		
		JSONObject jsObject=null;
		JSONArray jsArray=null;
		
		double a = 0;
		for (int i = 0; i < pCosts.length(); i++) {
			
			jsObject=pCosts.getJSONObject(i);
			jsArray=jsObject.getJSONArray("pola");
			a+=jsArray.getDouble(JsonData.INDEX_COST_REPAIR);
			
		}
		return a;
	}

	private JSONArray parseCostRepairs(JSONObject politicianCost) throws JSONException {
		JSONObject tmpObject = politicianCost.getJSONObject("layers").getJSONObject("wydatki");
		JSONArray costs = tmpObject.getJSONArray("roczniki");
		return null;
	}

	private double averageCosts(ArrayList<Integer> IDs) throws IOException, JSONException {
		JSONObject politicianCost=null; 
		double  average=0;
		for (int i = 0; i < IDs.size(); i++) {
			politicianCost= readJsonFromUrl(addres + IDs.get(i).toString() + costs);
			double a = countAverage (politicianCost);
			average+=a;
		}
		
		average/=IDs.size();
		information.setAllAverageCost(average);
		System.out.println(average);
		return average;
		
	}

	private double countAverage(JSONObject politicianCost) throws JSONException {
		//System.out.println(politicianCost);
		politicianCost=politicianCost.getJSONObject("layers").getJSONObject("wydatki");
		//System.out.println(politicianCost);
		JSONArray jsonArrays = politicianCost.getJSONArray("roczniki");
		JSONObject jsObject=null;
		JSONArray jsArray = null;
		double a;
		double average=0;
		for (int i = 0; i < jsonArrays.length(); i++) {
			a=0;
			jsObject=jsonArrays.getJSONObject(i);
			jsArray=jsObject.getJSONArray("pola");
			
			for (int j = 0; j < jsArray.length(); j++) {
				a+=jsArray.getDouble(j);
			}
			a/=jsArray.length();
			average+=a;
		}
		average/=jsonArrays.length();
		return average;
		
	}

	private ArrayList<JSONObject> readAllDataforTerm() throws IOException, JSONException {
		JSONObject json = readJsonFromUrl(addres + condition + arguments.numberTerm);
		JSONArray jsonArray = json.getJSONArray("Dataobject");
		
		ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>(500);

		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObjects.add(jsonArray.getJSONObject(i));
		}

		if (arguments.numberTerm == "7") {
			json = readJsonFromUrl(addres + condition + arguments.numberTerm + pageSecond);
			jsonArray = json.getJSONArray("Dataobject");

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObjects.add(jsonArray.getJSONObject(i));
			}
		}
		return jsonObjects;
	}
	private ArrayList<JSONObject> readAndParseBusinessTripDate() throws IOException, JSONException {
		ArrayList<Integer> IDs = jsData.getIDs();
		JSONObject tmpObject = null;
		ArrayList <JSONObject> tripData = new ArrayList<JSONObject>(500);
		
		for (int i = 0; i < IDs.size(); i++) {
			tmpObject = readJsonFromUrl(addres + IDs.get(i).toString() + trips );
	
			tripData.add(tmpObject);
		}
		
		
		return tripData;
	}

	

}
