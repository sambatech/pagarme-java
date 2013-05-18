package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

/* @statuses_codes = { :local => 0, :approved => 1, :processing => 2, :refused => 3, :chargebacked => 4 } */

public class PagarMeTransaction
{
	public enum PagarMeTransactionStatus {
		local,
		processing,
		approved,
		refused,
		chargebacked,
	}

	public PagarMeTransactionStatus status;
	public Date dateCreated;
	public int amount;
	public int installments;
	public String id;
	public boolean live;
	public String costumerName;
	public String cardLastDigits;

	public PagarMeTransaction() {
		this.installments = 1;
		this.live = PagarMe.getInstance().live;
		this.status = PagarMeTransactionStatus.local;
	}

	public PagarMeTransaction(JsonObject jsonResponse) {
		super();
		this.amount = jsonResponse.get("amount").getAsInt();
		this.status = PagarMeTransactionStatus.valueOf(jsonResponse.get("status").getAsString());
		this.installments = jsonResponse.get("installments").getAsInt();
		this.id = jsonResponse.get("id").getAsString();
		this.live = jsonResponse.get("live").getAsBoolean();
		this.costumerName = jsonResponse.get("costumer_name").getAsString();
		this.cardLastDigits = jsonResponse.get("card_last_digits").getAsString();
	}

	public static PagarMeTransaction findById(String id) throws PagarMeException {
		PagarMeRequest request = new PagarMeRequest("/transactions/" + id, "GET");
		JsonObject jsonObject = request.run();

		PagarMeTransaction transaction = new PagarMeTransaction(jsonObject);
		return transaction;
	}

	public static Object all(int page, int count) {
		return null;
	}

	public void charge() {
	}

	public void chargeback() {
	}
}
