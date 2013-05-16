package com.pagarme;

import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.io.*;

import javax.net.ssl.*;

/**
 * Hello world!
 *
 */
public class App 
{
	// TODO: remove this method (trust invalid HTTPS certificates!!!)
	public static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
			new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() { 
					return new X509Certificate[0]; 
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			}};

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) { return true; }
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {}
	}

	public static void main( String[] args )
	{
		System.out.println( "Hello World!" );

		disableCertificateValidation();

		PagarMeRequest request = new PagarMeRequest("/transactions", "GET", false);

		try {
			request.run();
		} catch (PagarMeException e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}
	}
}
