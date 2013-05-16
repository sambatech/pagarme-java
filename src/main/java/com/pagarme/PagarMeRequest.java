package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class PagarMeRequest
{
	String path;
	String method;
	boolean live;
	String apiVersion = "1";

	public PagarMeRequest(String _path, String _method, boolean _live) {
		this.path = _path;
		this.method = _method;
		this.live = _live;
	}

	private String requestURL() {
		return "https://127.0.0.1:3001/" + apiVersion + this.path;
	}

	private static String parametersString() {
		try {
			return "api_key=" + URLEncoder.encode("123123123", "UTF-8");
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
			url = new URL(this.requestURL());
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(this.method);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParameters.getBytes().length));

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

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
