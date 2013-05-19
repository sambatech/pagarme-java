package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

/* import org.bouncycastle.crypto.AsymmetricBlockCipher; */
/* import org.bouncycastle.crypto.engines.RSAEngine; */
/* import org.bouncycastle.crypto.params.AsymmetricKeyParameter; */
/* import org.bouncycastle.crypto.util.PublicKeyFactory; */
/* import org.bouncycastle.*; */
/* import java.security.Security; */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.*;
import javax.crypto.*;
 
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.io.pem.*;

import org.apache.commons.codec.binary.*;
 
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

	public PagarMeTransactionStatus status = PagarMeTransactionStatus.local;
	public Date dateCreated;
	public int amount;
	public int installments = 1;
	public String id;
	public boolean live = PagarMe.live;
	public String costumerName;
	public String cardLastDigits;

	public String cardNumber;
	public String cardHolderName;
	public int cardExpiracyMonth;
	public int cardExpiracyYear;
	public String cardCVV;

	public String cardHash = null;

	public PagarMeTransaction() {
	}

	private void updateFieldsFromJsonResponse(JsonObject jsonResponse ) {
		// TODO: 'date_created'
		this.amount = jsonResponse.get("amount").getAsInt();
		this.status = PagarMeTransactionStatus.valueOf(jsonResponse.get("status").getAsString());
		this.installments = jsonResponse.get("installments").getAsInt();
		this.id = jsonResponse.get("id").getAsString();
		this.live = jsonResponse.get("live").getAsBoolean();
		this.costumerName = jsonResponse.get("costumer_name").getAsString();
		this.cardLastDigits = jsonResponse.get("card_last_digits").getAsString();
	}

	public PagarMeTransaction(JsonObject jsonResponse) {
		super();
		updateFieldsFromJsonResponse(jsonResponse);
	}

	public PagarMeTransaction(String cardHash) {
		super();
		this.cardHash = cardHash;
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

	private String cardHashEncryptionString() {
		String returnString = "";
		returnString += "card_number=" + cardNumber + "&";
		returnString += "card_holder_name=" + cardHolderName + "&";
		returnString += "card_expiracy_date=" + String.format("%02d", cardExpiracyMonth) + String.format("%02d", cardExpiracyYear) + "&";
		returnString += "card_cvv=" + cardCVV;
		return returnString;
	}

	private String generateCardHash() throws PagarMeException {
        String encryptedCardHash = null;

		if(cardHash != null) {
			encryptedCardHash = cardHash;
		} else {
			PagarMeRequest request = new PagarMeRequest("/transactions/card_hash_key", "GET");
			JsonObject cardHashJson = request.run().getAsJsonObject();
			String publicKeyString = cardHashJson.get("public_key").getAsString();
			String keyId = cardHashJson.get("id").getAsString();

			String cardHashEncryptionString = cardHashEncryptionString();

			try {
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

				StringReader stringReader = new StringReader(publicKeyString);
				PemReader pemReader = new PemReader(stringReader);
				AsymmetricKeyParameter publicKey = PublicKeyFactory.createKey(pemReader.readPemObject().getContent());

				AsymmetricBlockCipher blockCipher = new RSAEngine();
				blockCipher = new org.bouncycastle.crypto.encodings.PKCS1Encoding(blockCipher);
				blockCipher.init(true, publicKey);

				byte[] bytesToEncrypt = cardHashEncryptionString.getBytes();
				byte[] encryptedBytes = blockCipher.processBlock(bytesToEncrypt, 0, bytesToEncrypt.length);

				encryptedCardHash = keyId + "_" + new String(Base64.encodeBase64(encryptedBytes));
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new PagarMeResponseException("Error generating card_hash.");
			}
		}

		return encryptedCardHash;
	}

	public void charge() throws PagarMeException {
		String cardHash = generateCardHash();

		PagarMeRequest request = new PagarMeRequest("/transactions/", "POST");
		request.parameters.put("amount", String.valueOf(amount));
		request.parameters.put("installments", String.valueOf(installments));
		request.parameters.put("card_hash", cardHash);

		JsonObject transactionJson = request.run().getAsJsonObject();
		updateFieldsFromJsonResponse(transactionJson);
	}

	public void chargeback() throws PagarMeException {
		PagarMeRequest request = new PagarMeRequest("/transactions/" + id, "DELETE");
		JsonObject transactionJson = request.run().getAsJsonObject();
		updateFieldsFromJsonResponse(transactionJson);
	}
}
