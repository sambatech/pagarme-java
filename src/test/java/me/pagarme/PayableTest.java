package me.pagarme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.pagar.model.Payable;
import me.pagar.model.Payable.Status;
import me.pagar.model.Payable.Type;
import me.pagar.model.Recipient;
import me.pagar.model.SplitRule;
import me.pagar.model.Transaction;
import me.pagar.model.Transaction.PaymentMethod;
import me.pagarme.factory.RecipientFactory;
import me.pagarme.factory.TransactionFactory;
import me.pagarme.helper.TestEndpoints;

public class PayableTest extends BaseTest {

    private RecipientFactory recipientFactory = new RecipientFactory();
    private TransactionFactory transactionFactory = new TransactionFactory();
    private TestEndpoints testEndpoints = new TestEndpoints();

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testRetrieveTransactionPayables() throws Throwable {
        Transaction transaction = transactionFactory.createBoletoTransaction();
        transaction.save();
        testEndpoints.payBoleto(transaction);
        Collection<Payable> payables = transaction.findPayableCollection(10, 0);
        Assert.assertEquals(1, payables.size());
    }

    @Test
    public void testRetrieveTransactionPayableFields() throws Throwable {
        Transaction transaction = transactionFactory.createBoletoTransaction();
        transaction.save();
        testEndpoints.payBoleto(transaction);
        Collection<Payable> payables = transaction.findPayableCollection(10, 0);
        Assert.assertEquals(1, payables.size());

        Payable payable = payables.iterator().next();
        Assert.assertEquals(transaction.getAmount(), payable.getAmount());
        Assert.assertNotNull(payable.getFee());
        Assert.assertEquals(PaymentMethod.BOLETO, payable.getPaymentMethod());
        Assert.assertNotNull(payable.getTransactionId());
        Assert.assertNotNull(payable.getPaymentDate());
        Assert.assertEquals(Status.PAID, payable.getStatus());
        Assert.assertEquals(Type.CREDIT, payable.getType());
        Assert.assertNotNull(payable.getRecipientId());
        Assert.assertNotNull(payable.getAnticipationFee());
    }

    @Test
    public void testRetrieveTransactionPayable() throws Throwable {
        Transaction transaction = transactionFactory.createBoletoTransaction();
        transaction.save();
        testEndpoints.payBoleto(transaction);
        Collection<Payable> payables = transaction.findPayableCollection(10, 0);
        Assert.assertEquals(1, payables.size());

        Payable payableFromCollection = payables.iterator().next();
        Payable payableFromFind = new Payable().find(payableFromCollection.getId());
        Assert.assertEquals(payableFromCollection.getId(), payableFromFind.getId());
    }

    //Não entrou ainda find com filtro, quando entrar aí descomenta
 // @Test
 // public void testRetrievePayableWithFilters() throws Throwable {
//      Transaction transaction = transactionFactory.createBoletoTransaction();
//      transaction.setCapture(true);
//      transaction.setAmount(10000);

//      Collection<SplitRule> splitRules = new ArrayList<SplitRule>();
     
//      Recipient recipient1 = recipientFactory.create();
//      recipient1.save();
//      SplitRule splitRule1 = new SplitRule();
//      recipient1.save();
//      splitRule1.setRecipientId(recipient1.getId());
//      splitRule1.setPercentage(50);
//      splitRule1.setLiable(true);
//      splitRule1.setChargeProcessingFee(true);
//      splitRules.add(splitRule1);

//      Recipient recipient2 = recipientFactory.create();
//      recipient2.save();
//      SplitRule splitRule2 = new SplitRule();
//      recipient2.save();
//      splitRule2.setRecipientId(recipient2.getId());
//      splitRule2.setPercentage(50);
//      splitRule2.setLiable(true);
//      splitRule2.setChargeProcessingFee(true);

//      splitRules.add(splitRule2);
//      transaction.setSplitRules(splitRules);
//      transaction.save();
//      testEndpoints.payBoleto(transaction);
//      Collection<Payable> payables = transaction.findPayableCollection(10, 0);
//      Assert.assertEquals(2, payables.size());

//      String idRecipientToFilter = recipient1.getId();
//      Map<String, Object> filters = new HashMap<String, Object>();
//      filters.put("recipient_id", idRecipientToFilter);
//      Payable payable = new Payable();
//      payable.setRecipientId(recipient1.getId());
//      Collection<Payable> filteredPayables = payable.findCollection(10, 1);
//      Assert.assertEquals(1, filteredPayables.size());
 // }

}