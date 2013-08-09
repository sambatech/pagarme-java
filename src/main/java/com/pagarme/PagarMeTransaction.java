package com.pagarme;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

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
 
public class PagarMeTransaction
{
	public String status = "local";
	public Date dateCreated;
	public int amount;
	public int installments = 1;
	public String id;
	public String customerName;
	public String cardLastDigits;
	public String postbackURL = null;
	public String paymentMethod = "credit_card";

	public String cardNumber;
	public String cardHolderName;
	public int cardExpiracyMonth;
	public int cardExpiracyYear;
	public String cardCVV;

	public String cardHash = null;

	public PagarMeTransaction() {
	}

	public PagarMeTransaction(JsonObject jsonResponse) {
		super();
		updateFieldsFromJsonResponse(jsonResponse);
	}

	public PagarMeTransaction(String cardHash) {
		super();
		this.cardHash = cardHash;
	}

	private void updateFieldsFromJsonResponse(JsonObject jsonResponse) {
		// TODO: 'date_created'
		this.amount = jsonResponse.get("amount").getAsInt();
		this.status = jsonResponse.get("status").getAsString();
		this.installments = jsonResponse.get("installments").getAsInt();
		this.id = jsonResponse.get("id").getAsString();
		if(!jsonResponse.get("customer_name").isJsonNull()) this.customerName = jsonResponse.get("customer_name").getAsString();
		if(!jsonResponse.get("card_last_digits").isJsonNull()) this.cardLastDigits = jsonResponse.get("card_last_digits").getAsString();
		this.paymentMethod = jsonResponse.get("payment_method").getAsString();
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
		validateFields();

		String cardHash = generateCardHash();

		PagarMeRequest request = new PagarMeRequest("/transactions/", "POST");
		request.parameters.put("amount", String.valueOf(amount));
		request.parameters.put("installments", String.valueOf(installments));
		request.parameters.put("card_hash", cardHash);
		request.parameters.put("payment_method", paymentMethod);
		if(postbackURL != null) {
			request.parameters.put("postback_url", postbackURL);
		}

		JsonObject transactionJson = request.run().getAsJsonObject();
		updateFieldsFromJsonResponse(transactionJson);
	}

	public void chargeback() throws PagarMeException {
		if(status != "approved") {
			throw new PagarMeValidationException("Transação com status '" + status + "' não pode ser cancelada!");
		}
		
		if(paymentMethod != "credit_card") {
			throw new PagarMeValidationException("Boletos não podem ser cancelados!");
		}

		PagarMeRequest request = new PagarMeRequest("/transactions/" + id, "DELETE");
		JsonObject transactionJson = request.run().getAsJsonObject();
		updateFieldsFromJsonResponse(transactionJson);
	}

	public void validateFields() throws PagarMeException {
		if(cardHash == null && paymentMethod == "credit_card") {
			if(cardNumber.length() < 16 || cardNumber.length() > 20) {
				throw new PagarMeValidationException("Número do cartão inválido.");
			}

			if(cardHolderName.length() < 1) {
				throw new PagarMeValidationException("Nome do portador inválido.");
			}

			if(cardExpiracyMonth < 1 || cardExpiracyMonth > 12) {
				throw new PagarMeValidationException("Mês de expiração inválido.");
			}

			if(cardExpiracyYear < 1) {
				throw new PagarMeValidationException("Ano de expiração inválido.");
			}

			if(cardCVV.length() < 3 || cardCVV.length() > 4) {
				throw new PagarMeValidationException("Código de segurança inválido.");
			}
		}

		if(amount < 1 && amount > 100000000) {
			throw new PagarMeValidationException("Valor inválido.");
		}
	}
}
