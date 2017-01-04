package agh.lab.zad2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {

	private static String addres = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?limit=550";

	private static String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		String cp;
		while ((cp = rd.readLine()) != null) {
			sb.append(cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			JSONArray jsonArray = json.getJSONArray("Dataobject");
			System.out.println(jsonText.length());
			System.out.println(jsonArray.length());
			return jsonArray;
		} finally {
			is.close();
		}
	}

	public static void main(String[] args) throws IOException, JSONException {
		JSONArray json = readJsonFromUrl("https://api-v3.mojepanstwo.pl/dane/poslowie.json?limit=550");
		JSONArray json1 = readJsonFromUrl(
				"https://api-v3.mojepanstwo.pl/dane/poslowie?page=2&limit=744");
		
		
		for (int i = 0; i < json.length(); i++) {
			// System.out.println(json.get(i));
		}

		System.out.println(json.getJSONObject(0).getJSONObject("data"));
		System.out.println();
		// System.out.println(json.toString());
		// System.out.println(json.get("8"));
	}
}