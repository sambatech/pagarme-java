package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

public class PagarMeRequest
{
	public String path;
	public String method;
	public boolean live;
	public String apiVersion = "1";
	public HashMap parameters;

	public PagarMeRequest(String _path, String _method) {
		this.path = _path;
		this.method = _method;
		this.live = PagarMe.getInstance().live;
		this.parameters = new HashMap();
	}

	private HashMap requestParameters() {
		HashMap params = new HashMap();
		params.put("api_key", PagarMe.getInstance().apiKey);
		params.putAll(parameters);
		return params;
	}

	private String requestURL() {
		return "https://127.0.0.1:3001/" + apiVersion + this.path;
	}

	private String joinList(List<?> list, char delimiter) {
		StringBuilder result = new StringBuilder();
		for (Iterator<?> i = list.iterator(); i.hasNext();) {
			result.append(i.next());
			if (i.hasNext()) {
				result.append(delimiter);
			}
		}
		return result.toString();
	}

	private String parametersString() {
		try {
			List<String> formattedParameters = new ArrayList<String> ();

			HashMap params = requestParameters();
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				formattedParameters.add(pairs.getKey() + "=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8"));
				it.remove(); // avoids a ConcurrentModificationException
			}

			return joinList(formattedParameters, '&');
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private HttpURLConnection performWriteBodyRequest(String requestURL, String parameters, String method) throws Exception {
		URL url = new URL(requestURL);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();

		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
		connection.setRequestMethod(method);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(10000);

		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(parameters);
		outputStream.flush();
		outputStream.close();
		
		return connection;
	}
	
	private HttpURLConnection performGetRequest(String requestURL, String parameters) throws Exception {
		URL url = new URL(requestURL + "?" + parameters);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoInput(true); // true if we want to read server's response
		connection.setDoOutput(false); // false indicates this is a GET request
		connection.setUseCaches(false);
		connection.setReadTimeout(10000);

		return connection;
	}

	public JsonElement run() throws PagarMeException {
		String requestURL = this.requestURL();
		String requestParameters = this.parametersString();
		System.out.println(requestParameters);

		HttpURLConnection connection;

		try {
			if(this.method.toUpperCase().equals("GET")) {
				connection = performGetRequest(requestURL, requestParameters);
			} else {
				connection = performWriteBodyRequest(requestURL, requestParameters, this.method);
			}
		} catch (Exception e) {
			throw new PagarMeConnectionException("Could not connect to server.");
		}

		String responseString;

		try {
			Scanner responseScanner;

			if (connection.getResponseCode() != 200) {
				responseScanner = new Scanner(connection.getErrorStream());
			} else {
				responseScanner = new Scanner(connection.getInputStream());
			}

			responseScanner.useDelimiter("\\Z");
			responseString = responseScanner.next();

			System.out.println(responseString);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PagarMeConnectionException("Could not connect to server.");
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}

		JsonElement returnObject;

		try {
			returnObject = new JsonParser().parse(responseString);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PagarMeResponseException("Invalid JSON response.");
		}

		if(returnObject.isJsonObject()) {
			JsonObject errorObject = returnObject.getAsJsonObject();
			if(errorObject.get("error") != null) {
				throw new PagarMeResponseException(errorObject.get("error").getAsString());
			}
		}

		return returnObject;
	}
}
