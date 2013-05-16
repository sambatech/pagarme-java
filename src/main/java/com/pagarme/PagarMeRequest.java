package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;

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

	public void run() {
		URL url;
		HttpURLConnection connection = null;
		String requestParameters = this.parametersString();
		System.out.println(requestParameters);

		try {
			url = new URL(this.requestURL());
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(this.method);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(requestParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(requestParameters);
			outputStream.flush();
			outputStream.close();

			Scanner responseScanner;

			if (connection.getResponseCode() != 200) {
				responseScanner = new Scanner(connection.getErrorStream());
				System.out.println("error");
			} else {
				responseScanner = new Scanner(connection.getInputStream());
			}

			responseScanner.useDelimiter("\\Z");
			String response = responseScanner.next();
			
			System.out.println(response);

		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("FAIL");
			/* return null; */

		} finally {
			System.out.println("disconnect");
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
}
