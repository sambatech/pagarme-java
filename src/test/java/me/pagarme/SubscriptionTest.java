package me.pagarme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.pagar.SubscriptionStatus;
import me.pagar.model.Card;
import me.pagar.model.Customer;
import me.pagar.model.PagarMeException;
import me.pagar.model.Plan;
import me.pagar.model.Recipient;
import me.pagar.model.SplitRule;
import me.pagar.model.Subscription;
import me.pagar.model.Transaction;
import me.pagar.model.Transaction.PaymentMethod;
import me.pagarme.factory.CardFactory;
import me.pagarme.factory.CustomerFactory;
import me.pagarme.factory.PlanFactory;
import me.pagarme.factory.RecipientFactory;
import me.pagarme.factory.SubscriptionFactory;
import static org.hamcrest.CoreMatchers.instanceOf;

public class SubscriptionTest extends BaseTest {

    private PlanFactory planFactory = new PlanFactory();
    private SubscriptionFactory subscriptionFactory = new SubscriptionFactory();
    private CustomerFactory customerFactory = new CustomerFactory();
    private CardFactory cardFactory = new CardFactory();
    private RecipientFactory recipientFactory = new RecipientFactory();

    private Plan defaultPlan;
    private Plan defaultPlan2;
    private Customer defaultCustomer;
    private Card defaultCard;

    @Before
    public void beforeEach() throws PagarMeException {
        super.setUp();
        defaultCustomer = customerFactory.create();
        defaultCustomer.save();

        defaultPlan = planFactory.create();
        defaultPlan.save();

        defaultPlan2 = planFactory.createPlanWithoutTrialDays();
        defaultPlan2.save();

        defaultCard = cardFactory.create();
        defaultCard.save();
    }

    @Test
    public void testCreateSubscription() throws Throwable {
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription.save();
        Assert.assertEquals(SubscriptionStatus.TRIALING, subscription.getStatus());
        Assert.assertNotNull(subscription.getId());
    }

    @Test
    public void testUpdateSubscriptionPlan() throws Throwable {
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription.save();

        Plan newPlan = planFactory.create();
        newPlan.save();

        String newPlanId = newPlan.getId();

        Subscription edittedSubscription = new Subscription();
        edittedSubscription.setId(subscription.getId());
        edittedSubscription.setUpdatableParameters(null, null, newPlanId);
        edittedSubscription.save();

        Assert.assertEquals(newPlanId, edittedSubscription.getPlan().getId());
    }

    @Test
    public void testFindSubscription() throws Throwable {
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription.save();

        Subscription foundSubscription = new Subscription().find(subscription.getId());

        Assert.assertEquals(subscription.getId(), foundSubscription.getId());
        Assert.assertEquals(subscription.getPostbackUrl(), foundSubscription.getPostbackUrl());
        Assert.assertEquals(subscription.getCharges(), foundSubscription.getCharges());
        Assert.assertEquals(subscription.getCreatedAt(), foundSubscription.getCreatedAt());
        Assert.assertEquals(subscription.getCurrentPeriodEnd(), foundSubscription.getCurrentPeriodEnd());
        Assert.assertEquals(subscription.getCurrentPeriodStart(), foundSubscription.getCurrentPeriodStart());
        Assert.assertEquals(subscription.getCurrentTransaction(), foundSubscription.getCurrentTransaction());
        Assert.assertEquals(subscription.getCustomer().getId(), foundSubscription.getCustomer().getId());
        Assert.assertEquals(subscription.getMetadata(), foundSubscription.getMetadata());
        Assert.assertEquals(PaymentMethod.CREDIT_CARD, foundSubscription.getPaymentMethod());
        Assert.assertEquals(subscription.getPlan().getId(), foundSubscription.getPlan().getId());
        Assert.assertEquals(subscription.getStatus(), foundSubscription.getStatus());
    }

    /*
    @Test
    public void testListSubscription() throws Throwable {
        Subscription subscription1 = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription1.save();

        Customer otherCustomer = customerFactory.create();
        otherCustomer.setEmail("other@email.com");
        otherCustomer.setDocumentNumber("31102987166");
        Subscription subscription2 = subscriptionFactory.createBoletoSubscription(defaultPlan.getId(), otherCustomer);
        subscription2.save();

        Collection<Subscription> subscriptions = new Subscription().findCollection(10, 1);

        Assert.assertEquals(2, subscriptions.size());
    }
    */
    @Test
    public void testTransactionsCollectionInSubscription() throws PagarMeException{
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription.save();
        Iterator t = subscription.transactions().iterator();
        while(t.hasNext()){
            Assert.assertThat(t.next(), instanceOf(Transaction.class));
        }
    }

    @Test
    public void testCancelSubscription() throws Throwable {
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);
        subscription.save();

        subscription.cancel();

        Assert.assertEquals(subscription.getStatus(), SubscriptionStatus.CANCELED);
    }

    @Test
    public void testChangePostbackUrl() throws PagarMeException{
        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan.getId(), defaultCard.getId(), defaultCustomer);

        subscription.setPostbackUrl("http://requestb.in/t5mzh9t5");

        subscription.save();

        Assert.assertNotNull(subscription.getPostbackUrl());
    }

    @Test
    public void testSplitSubscription() throws Throwable {

        Subscription subscription = subscriptionFactory.createCreditCardSubscription(defaultPlan2.getId(),defaultCard.getId(), defaultCustomer);
        Collection<SplitRule> splitRules = new ArrayList<SplitRule>();

        Recipient recipient1 = recipientFactory.create();
        recipient1.save();

        SplitRule splitRule = new SplitRule();
        splitRule.setRecipientId(recipient1.getId());
        splitRule.setPercentage(50);
        splitRule.setLiable(true);
        splitRule.setChargeProcessingFee(true);
        splitRules.add(splitRule);

        Recipient recipient2  = recipientFactory.create();
        recipient2.save();

        SplitRule splitRule2 = new SplitRule();
        recipient2.save();
        splitRule2.setRecipientId(recipient2.getId());
        splitRule2.setPercentage(50);
        splitRule2.setLiable(true);
        splitRule2.setChargeProcessingFee(true);
        splitRules.add(splitRule2);

        subscription.setSplitRules(splitRules);
        subscription.save();

        Transaction foundTransaction = new Transaction().find(subscription.getCurrentTransaction().getId());

        Collection<SplitRule> foundSplitRules = foundTransaction.getSplitRules();
        Assert.assertEquals(splitRules.size(), foundSplitRules.size());
    }
}
