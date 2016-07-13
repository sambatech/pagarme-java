package com.pagarme;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pagarme.model.*;

import java.util.HashMap;
import java.util.Map;

public class TransactionTest extends BaseTest {

    private Transaction transaction;
    private static Integer AMOUNT = 100;
    private static Integer INSTALLMENTS_AT_ONCE = 1;
    private static Integer PAID_AMOUNT_PARTIAL = 50;

    @Before
    public void setUp() {
        super.setUp();
        transaction = new Transaction();
    }

    @Test
    public void testCreateAndCaptureTransactionWithCreditCardWithoutGoingThroughFraud() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(true);
        transaction.save();

        Assert.assertEquals(transaction.getPaymentMethod(), Transaction.PaymentMethod.CREDIT_CARD);
        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PAID);
    }

    @Test
    public void testCreateAndCaptureTransactionMetaData() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(true);

        Map<String, Object> metadata =  new HashMap<String, Object>();
        metadata.put("metada-key", "meta-value");
        metadata.put("metada-key2", "meta-value2");

        transaction.setMetadata(metadata);
        transaction.save();

        Assert.assertEquals(transaction.getPaymentMethod(), Transaction.PaymentMethod.CREDIT_CARD);
        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PAID);
    }

    @Test
    public void testCreateAndAuthorizedTransactionWithCreditCardWithoutGoingThroughFraud() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(false);
        transaction.save();

        Assert.assertEquals(transaction.getPaymentMethod(), Transaction.PaymentMethod.CREDIT_CARD);
        Assert.assertEquals(transaction.getStatus(), Transaction.Status.AUTHORIZED);
    }

    @Test
    public void testTransactionCreatePostbackUrl() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setPostbackUrl("http://pagar.me");
        transaction.save();

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PROCESSING);
    }

    @Test
    public void testTransactionAuthAndCaptureCaptureTotalValue() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(false);
        transaction.save();

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.AUTHORIZED);

        transaction.capture(AMOUNT);

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PAID);
    }

    @Test
    public void testTransactionAuthAndCaptureCapturePartialValue() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(false);
        transaction.save();

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.AUTHORIZED);

        transaction.capture(50);

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PAID);
        Assert.assertEquals(transaction.getPaidAmount(), PAID_AMOUNT_PARTIAL);
        Assert.assertEquals(transaction.getAuthorizedAmount(), AMOUNT);
    }

    @Test
    public void testTransactionAuthAndCaptureRefoundPartialValue() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(true);
        transaction.save();

        transaction.refund(50);

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.PAID);
        Assert.assertEquals(transaction.getPaidAmount(), AMOUNT);
        Assert.assertEquals(transaction.getRefundedAmount(), PAID_AMOUNT_PARTIAL);
        Assert.assertEquals(transaction.getAuthorizedAmount(), AMOUNT);
    }

    @Test
    public void testTransactionAuthAndCaptureRefoundTotalValue() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(true);
        transaction.save();

        transaction.refund(AMOUNT);

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.REFUNDED);
        Assert.assertEquals(transaction.getRefundedAmount(), AMOUNT);
    }

    @Test
    public void testCreateAndAuthorizedTransactionWithBoleto() throws Throwable {

        transaction = this.transactionBoletoCommon();
        transaction.save();

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.WAITING_PAYMENT);
        Assert.assertEquals(transaction.getPaymentMethod(), Transaction.PaymentMethod.BOLETO);
    }

    @Test
    public void testCreateTransactionWithBoleto() throws Throwable {

        transaction = this.transactionBoletoCommon();
        transaction.save();

        Assert.assertEquals(transaction.getStatus(), Transaction.Status.WAITING_PAYMENT);
        Assert.assertEquals(transaction.getPaymentMethod(), Transaction.PaymentMethod.BOLETO);
        Assert.assertEquals(transaction.getBoletoUrl(), "https://pagar.me");
        Assert.assertNotNull(transaction.getBoletoBarcode());
    }

    @Test
    public void testFindTransactionById() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.save();

        Integer transactionId = transaction.getId();

        transaction = transaction.find(transactionId);
        Assert.assertEquals(transaction.getId(), transactionId);
    }

    @Test
    public void testCreatingCustomerTransactionThroughTheTransaction() throws Throwable {

        transaction = this.transactionCreditCardCommon();
        transaction.setCapture(true);

        Customer customer = this.customerCommon();
        Address address = this.addressCommon();
        Phone phone = this.phoneCommon();

        customer.setAddress(address);
        customer.setPhone(phone);

        transaction.setCustomer(customer);
        transaction.save();

        Customer transactionCustomer = transaction.getCustomer();
        Assert.assertEquals(transactionCustomer.getName(), "lucas santos");
        Assert.assertEquals(transactionCustomer.getDocumentNumber(), "15317529506");
        Assert.assertEquals(transactionCustomer.getEmail(), "testelibjava@pagar.me");

        Address transactionAddress = transactionCustomer.getAddress();
        Assert.assertEquals(transactionAddress.getStreet(), "Rua Piraju");
        Assert.assertEquals(transactionAddress.getStreetNumber(), "218");
        Assert.assertEquals(transactionAddress.getComplementary(), "ao lado da consigáz");
        Assert.assertEquals(transactionAddress.getNeighborhood(), "Interlagos");

        Phone transactionPhone = transactionCustomer.getPhone();
        Assert.assertEquals(transactionPhone.getDdd(), "11");
        Assert.assertEquals(transactionPhone.getNumber(), "55284132");
    }
}