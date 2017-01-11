package agh.lab.zad2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {

	private static final int INDEX_COST_REPAIR = 12;

	private ArrayList<JSONObject> politiciansExpensesAndTrips;
	private SelectedPolitician selectedPolitician;

	public JsonData(ArrayList<JSONObject> politiciansExpensesAndTrips, SelectedPolitician selectedPolitician) {
		this.politiciansExpensesAndTrips = politiciansExpensesAndTrips;
		this.selectedPolitician = selectedPolitician;
	}

	public ProperInformation extractInformation() throws JSONException {
		ProperInformation information = null;
		try {
			information = parseAndExtractInformation();
		} catch (Exception e) {
			Main.LOGGER.error(e.getMessage());
		}

		return information;

	}

	private ProperInformation parseAndExtractInformation() throws JSONException {
		double sumExpenses = -1;
		double repairExpenses = -1;
		double average = -1;

		if (selectedPolitician != null) {
			sumExpenses = extractSelectedPoliticianSumExpanses();
			repairExpenses = extractSelectedPoliticianRepairExpanses();
		}

		average = countExpensesAverage();
		PoliticianWithAmountTrips politicianWithAmountTrips = extractMaxAmountOfTrips();
		PolitcianWithAbroadDays politicianMaxTimeAbroad = extractPoliticianWithTheLonMaxTimeAbroad();
		PoliticianWithCostTrips politicianWithTheMostExpensiveTrip = extractPoliticianWithTheMostExpensiveTrip();
		ArrayList<String> politicianVisitedItalia = extractPoliticianVisitedItalia();

		ProperInformation information = new ProperInformation(selectedPolitician, sumExpenses, repairExpenses, average,
				politicianMaxTimeAbroad, politicianWithTheMostExpensiveTrip, politicianWithAmountTrips,
				politicianVisitedItalia);

		return information;
	}

	private double extractSelectedPoliticianSumExpanses() throws JSONException {
		JSONObject politician = selectedPolitician.getPoliticianExpenses();
		JSONArray politicianCostPerYear = getExpensesArray(politician);

		double wholeSum = 0;
		double sumInYear = 0;
		for (int i = 0; i < politicianCostPerYear.length(); i++) {

			JSONArray politicianCost = getExpensesInSpecifiedYearArray(politicianCostPerYear.getJSONObject(i));
			sumInYear = getSumOfCostsInYear(politicianCost);
			wholeSum += sumInYear;
		}

		return wholeSum;
	}

	private double extractSelectedPoliticianRepairExpanses() throws JSONException {

		JSONObject politician = selectedPolitician.getPoliticianExpenses();
		JSONArray politicianCostPerYear = getExpensesArray(politician);
		double costRepairs = 0;

		for (int i = 0; i < politicianCostPerYear.length(); i++) {

			JSONArray politicianCost = getExpensesInSpecifiedYearArray(politicianCostPerYear.getJSONObject(i));
			costRepairs += politicianCost.getDouble(INDEX_COST_REPAIR);
		}

		return costRepairs;
	}

	private JSONArray getExpensesArray(JSONObject politician) throws JSONException {

		JSONObject tmpObject = politician.getJSONObject("layers").getJSONObject("wydatki");
		if (tmpObject.getString("roczniki").equals("{}")) {
			return new JSONArray();
		} else
			return tmpObject.getJSONArray("roczniki");

	}

	private JSONArray getExpensesInSpecifiedYearArray(JSONObject politicianCostPerYear) throws JSONException {
		return politicianCostPerYear.getJSONArray("pola");
	}

	private double countExpensesAverage() throws JSONException {
		double wholeAverage = 0.0;
		double averageByPolitician = 0.0;
		Double sumCostsInYear = 0.0;

		for (JSONObject politician : politiciansExpensesAndTrips) {

			JSONArray politicianCostPerYear = getExpensesArray(politician);
			averageByPolitician = 0;
			for (int i = 0; i < politicianCostPerYear.length(); i++) {

				JSONArray politicianCost = getExpensesInSpecifiedYearArray(politicianCostPerYear.getJSONObject(i));
				sumCostsInYear = getSumOfCostsInYear(politicianCost);
				averageByPolitician += sumCostsInYear;
			}

			wholeAverage += averageByPolitician;

		}
		wholeAverage /= politiciansExpensesAndTrips.size();
		return wholeAverage;
	}

	private double getSumOfCostsInYear(JSONArray politicianCost) throws JSONException {
		double sum = 0;
		for (int i = 0; i < politicianCost.length(); i++) {
			sum += politicianCost.optDouble(i, 0);
		}
		return sum;
	}

	private PoliticianWithAmountTrips extractMaxAmountOfTrips() throws JSONException {
		int maxAmountOfTrips = 0;
		String name = "";
		int tmpAmount;

		for (JSONObject politician : politiciansExpensesAndTrips) {

			JSONArray pTrips = getPoliticianTrips(politician);
			tmpAmount = pTrips.length();
			if (tmpAmount > maxAmountOfTrips) {
				maxAmountOfTrips = tmpAmount;
				name = getName(politician);
			}

		}

		return new PoliticianWithAmountTrips(name, maxAmountOfTrips);
	}

	private PolitcianWithAbroadDays extractPoliticianWithTheLonMaxTimeAbroad() throws JSONException {
		int maxAmountDaysOfTrip = 0;

		String name = "";

		int daysOfTrip, allDaysOfTrips;

		for (JSONObject politician : politiciansExpensesAndTrips) {

			JSONArray pTrips = getPoliticianTrips(politician);
			daysOfTrip = 0;
			allDaysOfTrips = 0;
			for (int i = 0; i < pTrips.length(); i++) {
				daysOfTrip = getAomuntOfDays(pTrips.getJSONObject(i));
				allDaysOfTrips += daysOfTrip;

			}
			if (maxAmountDaysOfTrip < allDaysOfTrips) {
				maxAmountDaysOfTrip = allDaysOfTrips;
				name = getName(politician);
			}

		}

		return new PolitcianWithAbroadDays(name, maxAmountDaysOfTrip);
	}

	private int getAomuntOfDays(JSONObject trip) throws JSONException {
		return trip.getInt("liczba_dni");
	}

	private PoliticianWithCostTrips extractPoliticianWithTheMostExpensiveTrip() throws JSONException {
		double maxCostOfTrips = 0;
		String name = "";
		double costOfTrip;

		for (JSONObject politician : politiciansExpensesAndTrips) {
			JSONArray pTrips = getPoliticianTrips(politician);
			for (int i = 0; i < pTrips.length(); i++) {
				costOfTrip = getCostOfTrip(pTrips.getJSONObject(i));

				if (costOfTrip > maxCostOfTrips) {
					maxCostOfTrips = costOfTrip;
					name = getName(politician);
				}
			}
		}

		return new PoliticianWithCostTrips(name, maxCostOfTrips);
	}

	private double getCostOfTrip(JSONObject trip) throws JSONException {
		return trip.getDouble("koszt_suma");
	}

	private ArrayList<String> extractPoliticianVisitedItalia() throws JSONException {

		ArrayList<String> PoliticiansVisitedItalia = new ArrayList<String>();
		String countryOfTrip;
		String name;
		for (JSONObject politician : politiciansExpensesAndTrips) {
			
			JSONArray pTrips = getPoliticianTrips(politician);
			
			for (int i = 0; i < pTrips.length(); i++) {
				countryOfTrip = getCountryOfTrip(pTrips.getJSONObject(i));

				if (countryOfTrip.equals("Włochy")) {
					name = getName(politician);
					PoliticiansVisitedItalia.add(name);
					break;
				}
			}
		}
		return PoliticiansVisitedItalia;
	}

	private String getCountryOfTrip(JSONObject trip) throws JSONException {
		return trip.getString("kraj");
	}

	private String getName(JSONObject politician) throws JSONException {
		return politician.getJSONObject("data").getString("ludzie.nazwa");
	}

	private JSONArray getPoliticianTrips(JSONObject politician) throws JSONException {
		JSONObject tmpObject = politician.getJSONObject("layers");

		if (tmpObject.getString("wyjazdy").equals("{}")) {
			return new JSONArray(); // return empty array, not null
		} else {
			return tmpObject.getJSONArray("wyjazdy");
		}
	}
}
