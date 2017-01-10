package agh.lab.zad2;

import org.json.JSONObject;

public class SelectedPolitician {
	
	private JSONObject politician=null;
	private JSONObject politicianExpenses;
	
	public SelectedPolitician (JSONObject politician ,JSONObject politicianCostRepair){
		this.politician=politician;
		this.politicianExpenses=politicianCostRepair;
	}
	public SelectedPolitician(JSONObject politicianExpenses) {
		this.politicianExpenses=politicianExpenses;
	}

	public JSONObject getPolitician() {
		return politician;
	}

	public JSONObject getPoliticianExpenses() {
		return politicianExpenses;
	}
	
}
