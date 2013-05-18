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
		JsonObject transactionJson = request.run().getAsJsonObject();

		PagarMeTransaction transaction = new PagarMeTransaction(transactionJson);
		return transaction;
	}

	public static List<PagarMeTransaction> all(int page, int count) throws PagarMeException {
		PagarMeRequest request = new PagarMeRequest("/transactions/", "GET");
		request.parameters.put("page", String.valueOf(page));
		request.parameters.put("count", String.valueOf(count));
		JsonArray transactionsJson = request.run().getAsJsonArray();

		List<PagarMeTransaction> transactions = new ArrayList<PagarMeTransaction>();
		
		for(int i = 0; i < transactionsJson.size(); i++) {
			PagarMeTransaction currentTransaction = new PagarMeTransaction(transactionsJson.get(i).getAsJsonObject());
			transactions.add(currentTransaction);
		}
		
		return transactions;
	}

	public static List<PagarMeTransaction> all() throws PagarMeException {
		// Default: page = 1, count = 10
		return all(1, 10);
	}

	public void charge() {
	}

	public void chargeback() {
	}
}
