package com.pagarme;

import com.google.common.base.Strings;
import org.junit.Assert;
import pagarme.PagarMe;
import pagarme.model.Address;
import pagarme.model.Customer;
import pagarme.model.Phone;
import pagarme.model.Transaction;

public class BaseTest {

    private Transaction transaction;
    private Customer customer;

    private static String NAME = "Teste Create Customer";
    private static String DOCUMENT_NUMBER = "15317529506";
    private static String EMAIL = "testcreatecustomer@pagar.me";
    private static String STREET = "Rua Piraju";
    private static String STREET_NUMBER = "218";
    private static String COMPLEMENTARY = "ao lado da consigáz";
    private static String NEIGHBORHOOD = "Interlagos";
    private static String ZIP_CODE = "04840110";
    private static String PHONE_DDD = "11";
    private static String PHONE_NUMBER = "55284132";

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

    /**
     *
     * @return
     */
    protected Customer customerCommon() {
        Customer customer = new Customer();
        customer.setName("lucas santos");
        customer.setDocumentNumber("15317529506");
        customer.setEmail("testelibjava@pagar.me");

        return customer;
    }

    /**
     *
     * @return
     */
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

    protected Customer customerCreateCommon() {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setDocumentNumber(DOCUMENT_NUMBER);
        customer.setEmail(EMAIL);

        Address address = new Address();
        address.setStreet(STREET);
        address.setStreetNumber(STREET_NUMBER);
        address.setComplementary(COMPLEMENTARY);
        address.setNeighborhood(NEIGHBORHOOD);
        address.setZipcode(ZIP_CODE);

        Phone phone = new Phone();
        phone.setDdd(PHONE_DDD);
        phone.setNumber(PHONE_NUMBER);

        customer.setAddress(address);
        customer.setPhone(phone);

        return customer;
    }

    /**
     *
     * @param customer
     * @param customerAddress
     * @param customerPhone
     */
    protected void assertCustomer(Customer customer, Address customerAddress, Phone customerPhone) {
        this.assertCustomerData(customer);
        this.assertAddress(customerAddress);
        this.assertPhone(phoneCommon());
    }

    /**
     *
     * @param customer
     */
    protected void assertCustomerData(Customer customer) {

        Assert.assertEquals(customer.getName(), NAME);
        Assert.assertEquals(customer.getDocumentNumber(), DOCUMENT_NUMBER);
        Assert.assertEquals(customer.getEmail(), EMAIL);
    }

    /**
     *
     * @param customerAddress
     */
    protected void assertAddress(Address customerAddress) {

        Assert.assertEquals(customerAddress.getStreet(), STREET);
        Assert.assertEquals(customerAddress.getStreetNumber(), STREET_NUMBER);
        Assert.assertEquals(customerAddress.getComplementary(), COMPLEMENTARY);
        Assert.assertEquals(customerAddress.getNeighborhood(), NEIGHBORHOOD);
        Assert.assertEquals(customerAddress.getZipcode(), ZIP_CODE);
    }

    protected void assertPhone(Phone customerPhone) {
        Assert.assertEquals(customerPhone.getDdd(), PHONE_DDD);
        Assert.assertEquals(customerPhone.getNumber(), PHONE_NUMBER);
    }
}
