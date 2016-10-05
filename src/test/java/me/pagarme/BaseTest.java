package me.pagarme;

import me.pagar.model.*;
import org.junit.Assert;
import org.omg.CORBA.Object;

public abstract class BaseTest {

    protected Transaction transaction;
    protected Customer customer;

    protected static String NAME = "Teste Create Customer";
    protected static String DOCUMENT_NUMBER = "84344469283";
    protected static String EMAIL = "testcreatecustomer@pagar.me";
    protected static String STREET = "Rua Piraju";
    protected static String STREET_NUMBER = "218";
    protected static String COMPLEMENTARY = "ao lado da consigáz";
    protected static String NEIGHBORHOOD = "Interlagos";
    protected static String ZIP_CODE = "04840110";
    protected static String PHONE_DDD = "11";
    protected static String PHONE_NUMBER = "55284132";

    protected static String AGENCIA = "0192";
    protected static String AGENCIA_DV = "0";
    protected static String CONTA = "03245";
    protected static String CONTA_DV = "0";
    protected static String BANK_CODE = "0341";
    protected static String LEGAL_NAME = "Java Lib Bank Account";

    protected static Integer TRANSFER_DAY = 1;
    protected static Boolean TRANSFER_ENABLE = true;

    public void setUp() {
        transaction = new Transaction();

        if (PagarMe.getApiKey() == null) {
            PagarMe.init("test_key");
            String apiKey = new CompaniesTempory().getTemporaryCompanyApiKey();
            PagarMe.init(apiKey);
        }
    }

    /**
     *
     * @return
     */
    protected Transaction transactionCreditCardCommon() {
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        transaction.setCardHolderName("Lucas Dos Santos Alves");
        transaction.setCardExpirationDate("0517");
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param customerPhone
     */
    protected void assertPhone(Phone customerPhone) {
        Assert.assertEquals(customerPhone.getDdd(), PHONE_DDD);
        Assert.assertEquals(customerPhone.getNumber(), PHONE_NUMBER);
    }
}
