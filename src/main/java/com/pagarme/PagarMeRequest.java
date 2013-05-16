package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class PagarMeRequest
{
	public String path;
	public String method;
	public boolean live;
	public String apiVersion = "1";
	public HashMap parameters;

	public PagarMeRequest(String _path, String _method, boolean _live) {
		this.path = _path;
		this.method = _method;
		this.live = _live;
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

	public HashMap run() throws PagarMeException {
		URL url;
		HttpURLConnection connection = null;
		String requestParameters = this.parametersString();
		System.out.println(requestParameters);

		String responseString;

		try {
			System.out.println(this.requestURL());
			url = new URL(this.requestURL());
			connection = (HttpURLConnection)url.openConnection();

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParameters.getBytes().length));
			connection.setRequestMethod(this.method);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(requestParameters);
			outputStream.flush();
			outputStream.close();

			Scanner responseScanner;

			if (connection.getResponseCode() != 200) {
				responseScanner = new Scanner(connection.getErrorStream());
			} else {
				responseScanner = new Scanner(connection.getInputStream());
			}

			responseScanner.useDelimiter("\\Z");
			responseString = responseScanner.next();
		} catch (Exception e) {
			throw new PagarMeConnectionException("Could not connect to server.");
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}

		HashMap responseObject;

		try {
			Gson gson = new Gson();
			responseObject = gson.fromJson(responseString, HashMap.class);
		} catch (Exception e) {
			throw new PagarMeResponseException("Invalid JSON response.");
		}

		if(responseObject.containsKey("error")) {
			throw new PagarMeResponseException(responseObject.get("error").toString());
		}

		System.out.println(responseObject);

		return responseObject;
	}
}
