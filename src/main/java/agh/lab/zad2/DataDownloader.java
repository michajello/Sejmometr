package agh.lab.zad2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataDownloader {

	private static final String ADDRES = "https://api-v3.mojepanstwo.pl/dane/poslowie/";
	private static final String PAGE_SECOND = "&page=2";
	private static final String COSTS = "?layers[]=wydatki";
	private static final String TRIPS = "&layers[]=wyjazdy";
	private static final String CONDITION = "?limit=500&conditions[poslowie.kadencja]=";
	private static final String PATH_TO_FILE = "./src/main/resources/";
	private Arguments arguments;

	private String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		String cp;
		while ((cp = rd.readLine()) != null) {
			sb.append(cp);
		}
		return sb.toString();
	}

	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			is.close();
			return json;
		} finally {
			is.close();
		}

	}

	public DataDownloader(Arguments arguments) {
		this.arguments = arguments;
	}

	public JsonData downloadAllData() throws IOException, JSONException {
		JsonData jsonData = null;

		try {
			jsonData = downloadData();
		} catch (Exception e) {
			Main.LOGGER.error(e.getMessage());
		}

		return jsonData;
	}

	private JsonData downloadData() throws IOException, JSONException {
		ArrayList<JSONObject> politicianExpensesAndTrips = null;
		SelectedPolitician selectedPolitician = null;

		if (arguments.readingFromFile) {
			politicianExpensesAndTrips = readFromFile();
			selectedPolitician = getPolitician(politicianExpensesAndTrips);
		} else {

			politicianExpensesAndTrips = readFromFile();
			// selectedPolitician = getPolitician(politicianExpensesAndTrips);

			ArrayList<JSONObject> allData = readAllDataforTerm();
			selectedPolitician = extractPoliticianAndDownloadCostRepair(allData);

			ArrayList<Integer> politiciansIDs = extractIDs(allData);
			politicianExpensesAndTrips = downloadExpensesAndTrips(politiciansIDs);
			saveData(politicianExpensesAndTrips);
		}

		JsonData jsData = new JsonData(politicianExpensesAndTrips, selectedPolitician);
		return jsData;

	}

	private ArrayList<JSONObject> readAllDataforTerm() throws IOException, JSONException {
		JSONObject json = readJsonFromUrl(ADDRES + CONDITION + arguments.numberTerm);
		JSONArray jsonArray = json.getJSONArray("Dataobject");

		ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>(500);

		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObjects.add(jsonArray.getJSONObject(i));
		}

		if (arguments.numberTerm == "7") {
			json = readJsonFromUrl(ADDRES + CONDITION + arguments.numberTerm + PAGE_SECOND);
			jsonArray = json.getJSONArray("Dataobject");

			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObjects.add(jsonArray.getJSONObject(i));
			}
		}
		return jsonObjects;
	}

	private SelectedPolitician extractPoliticianAndDownloadCostRepair(ArrayList<JSONObject> allData)
			throws JSONException, IOException {
		Main.LOGGER.info("Started downloading data about selected politician");

		JSONObject politicianObject = null;
		JSONObject politicianCostRepairs = null;
		SelectedPolitician SelectedPolitician = null;
		try {
			politicianObject = extractPolitician(allData);
			Integer id = politicianObject.getInt("id");
			politicianCostRepairs = downloadCostRepair(id);
			SelectedPolitician = new SelectedPolitician(politicianObject, politicianCostRepairs);
		} catch (IllegalArgumentException e) {
			Main.LOGGER.warn(e);
		}
		Main.LOGGER.info("Finished downloading data about selected politician");
		return SelectedPolitician;

	}

	private JSONObject downloadCostRepair(Integer id) throws IOException, JSONException {
		JSONObject politicianCost = readJsonFromUrl(ADDRES + id.toString() + COSTS);
		return politicianCost;
	}

	public JSONObject extractPolitician(ArrayList<JSONObject> allData) throws JSONException {
		JSONObject politician = null;

		String firstname, lastname;
		for (int i = 0; i < allData.size(); i++) {
			firstname = (String) allData.get(i).getJSONObject("data").get("poslowie.imie_pierwsze");
			lastname = (String) allData.get(i).getJSONObject("data").get("poslowie.nazwisko");

			if (arguments.firstname.equals(firstname) && arguments.lastname.equals(lastname)) {
				politician = allData.get(i);
				Main.LOGGER.debug("Founded politician" + politician);
				break;
			}

		}
		if (politician == null)
			throw new IllegalArgumentException("No founded information about selected politician");
		return politician;
	}

	private ArrayList<JSONObject> downloadExpensesAndTrips(ArrayList<Integer> politiciansIDs)
			throws IOException, JSONException {
		Main.LOGGER.info("Started dowloading data about politician trips and expenses");

		ArrayList<JSONObject> politiciansExpensesAndTrips = new ArrayList<JSONObject>(500);
		JSONObject tmpObject = null;

		for (int i = 0; i < politiciansIDs.size(); i++) {
			tmpObject = readJsonFromUrl(ADDRES + politiciansIDs.get(i).toString() + COSTS + TRIPS);
			politiciansExpensesAndTrips.add(tmpObject);
			Main.LOGGER.debug("Downloaded " + (i + 1) + " politician");
		}

		Main.LOGGER.info("Finished dowloading data about politician trips and expenses");
		return politiciansExpensesAndTrips;
	}

	private ArrayList<JSONObject> downloadTrips(ArrayList<Integer> politiciansIDs) throws IOException, JSONException {
		Main.LOGGER.info("Start downloading data about politicians TRIPS");
		ArrayList<JSONObject> politiciansTrips = new ArrayList<JSONObject>(500);
		JSONObject tmpObject = null;
		for (Integer id : politiciansIDs) {
			tmpObject = readJsonFromUrl(ADDRES + id.toString() + TRIPS);
			politiciansTrips.add(tmpObject);
		}
		Main.LOGGER.info("Finished downloading data about politicians TRIPS");
		return politiciansTrips;
	}

	private SelectedPolitician getPolitician(ArrayList<JSONObject> politicianData) throws JSONException {
		JSONObject jsObject = null;
		for (JSONObject politician : politicianData) {

			jsObject = politician.getJSONObject("data");
			String name = jsObject.getString("poslowie.imie_pierwsze");
			String lastname = jsObject.getString("poslowie.nazwisko");
			if (arguments.firstname.equalsIgnoreCase(name) && arguments.lastname.equalsIgnoreCase(lastname)) {
				Main.LOGGER.debug("Downloaded politician = " + politician.toString());
				return new SelectedPolitician(politician);
			}

		}
		Main.LOGGER.warn(new IllegalArgumentException("No founded information about selected politician"));
		return null;
	}

	private ArrayList<JSONObject> readFromFile() throws IOException, JSONException {
		BufferedReader br = new BufferedReader(new FileReader(PATH_TO_FILE + "data" + arguments.numberTerm + ".txt"));
		ArrayList<JSONObject> politicianData = readJSONObject(br);
		return politicianData;

	}

	private ArrayList<JSONObject> readJSONObject(BufferedReader br) throws IOException, JSONException {
		ArrayList<JSONObject> politicianData = new ArrayList<JSONObject>(500);
		String jsonText;
		while ((jsonText = br.readLine()) != null) {
			JSONObject jsObject = new JSONObject(jsonText);
			politicianData.add(jsObject);
		}
		return politicianData;
	}

	private void saveData(ArrayList<JSONObject> politiciansExpensesAndTrips) throws IOException {
		FileWriter fw = new FileWriter(PATH_TO_FILE + "data" + arguments.numberTerm + ".txt");

		for (JSONObject politician : politiciansExpensesAndTrips) {
			fw.write(politician.toString());
			fw.write("\n");
		}
		fw.close();

	}

	public ArrayList<Integer> extractIDs(ArrayList<JSONObject> allData) throws JSONException {
		int id;
		ArrayList<Integer> IDs = new ArrayList<Integer>(500);
		for (JSONObject jsonObject : allData) {
			id = jsonObject.getInt("id");
			IDs.add(id);
		}
		return IDs;

	}

}
