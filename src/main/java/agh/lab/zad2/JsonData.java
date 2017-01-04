package agh.lab.zad2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {

	public static final int  INDEX_COST_REPAIR = 12;
	
	private ArrayList<JSONObject> allData = null; ;
//	private ArrayList<JSONObject> 
	
	
	private JSONObject costsById = null;
	private JSONObject costsRepair = null;
	private JSONObject averageCosts = null;
	private JSONObject mostTravellingPolitician = null;
	private JSONObject largestPeriodTravel = null;
	private ArrayList <JSONObject> politicianVisitedItalia = new ArrayList<JSONObject>(); 
	private ArrayList<Integer> IDs = new ArrayList<Integer>(500);
	
	private ArrayList<JSONObject> politiciansExpenses;
	private ArrayList<JSONObject> politiciansTrips;
	private Politician politician;
	
	
	
	
	public ArrayList<Integer> getIDs() {
		return IDs;
	}

	public JsonData (ArrayList<JSONObject> allData){
		this.allData=allData;
	}
	
	public JsonData(ArrayList<JSONObject> politiciansExpenses, ArrayList<JSONObject> politiciansTrips,
			Politician politician) {
		this.politiciansExpenses=politiciansExpenses;
		this.politiciansTrips=politiciansTrips;
		this.politician=politician;
	}

	public ProperInformation extractInformation() {
		
		return null;
	}

	

	

}
