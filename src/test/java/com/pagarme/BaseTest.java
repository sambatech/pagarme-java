package com.pagarme;

import com.google.common.base.Strings;
import pagarme.PagarMe;
import pagarme.model.Address;
import pagarme.model.Customer;
import pagarme.model.Phone;
import pagarme.model.Transaction;

public class BaseTest {

    private Transaction transaction;

    public void setUp() {
        transaction = new Transaction();
        String apiKey = System.getenv("PAGARME_API_KEY");

        if (Strings.isNullOrEmpty(apiKey)) {
            apiKey = "ak_test_Rw4JR98FmYST2ngEHtMvVf5QJW7Eoo";
        }

        PagarMe.init("ak_test_Rw4JR98FmYST2ngEHtMvVf5QJW7Eoo");
    }

    /**
     *
     * @return
     */
    protected Transaction transactionCreditCardCommon() {
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        transaction.setCardHolderName("Lucas Dos Santos Alves");
        transaction.setCardExpirationDate("0916");
        transaction.setCardCvv("401");
        transaction.setCardNumber("4111111111111111");
        transaction.setInstallments(1);

        return transaction;
    }

    protected Customer customerCommon() {
        Customer customer = new Customer();
        customer.setName("lucas santos");
        customer.setDocumentNumber("29542749139");
        customer.setEmail("testelibjava@pagar.me");

        return customer;
    }

    protected Address addressCommon() {
        Address address = new Address();
        address.setStreet("Rua Piraju");
        address.setStreetNumber("218");
        address.setComplementary("ao lado da consigáz");
        address.setNeighborhood("Interlagos");
        address.setZipcode("04840110");

        return address;
    }

    protected Phone phoneCommon() {
        Phone phone = new Phone();
        phone.setDdd("11");
        phone.setNumber("55284132");

        return phone;
    }

    /**
     *
     * @return
     */
    protected Transaction transactionBoletoCommon() {
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.BOLETO);
        return transaction;
    }
}
