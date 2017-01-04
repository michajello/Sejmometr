package agh.lab.zad2;

import org.json.JSONObject;

public class Politician {
	
	private JSONObject politician;
	private JSONObject politicianCostRepair;
	
	public Politician (JSONObject politician ,JSONObject politicianCostRepair){
		this.politician=politician;
		this.politicianCostRepair=politicianCostRepair;
	}

	public JSONObject getPolitician() {
		return politician;
	}

	public JSONObject getPoliticianCostRepair() {
		return politicianCostRepair;
	}
	
}
