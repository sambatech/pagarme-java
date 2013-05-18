package com.pagarme;

public class PagarMe {
	public static String apiKey;
	public static boolean live;

	private static PagarMe instance = null;

	protected PagarMe() {
		this.live = true;
		// Exists only to defeat instantiation.
	}

	public static PagarMe getInstance() {
		if(instance == null) {
			instance = new PagarMe();
		}

		return instance;
	}
}
