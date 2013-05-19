package com.pagarme;

public class PagarMe {
	public static String apiKey;
	public static boolean live = true;

	private static PagarMe instance = null;

	protected PagarMe() {
		// Exists only to defeat instantiation.
	}

	public static PagarMe getInstance() {
		if(instance == null) {
			instance = new PagarMe();
		}

		return instance;
	}
}
