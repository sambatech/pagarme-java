package me.pagarme;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.pagar.SubscriptionStatus;
import me.pagar.model.Address;
import me.pagar.model.Card;
import me.pagar.model.Customer;
import me.pagar.model.Phone;
import me.pagar.model.Plan;
import me.pagar.model.Subscription;

public class SubscriptionTest extends BaseTest {

    private Plan plan;
	private Subscription subscription;

    private static Integer AMOUNT = 100;

    @Before
    public void setUp() {
        super.setUp();
        plan = new Plan();
        customer = new Customer();
	    subscription = new Subscription();
    }

    @Test
    public void testCreatePlan() throws Throwable {

	    plan = this.planCommon(AMOUNT);
	    plan.save();

        Assert.assertNotNull(plan.getId());
    }

    @Test
    public void testFindPlan() throws Throwable {
	    plan = this.planCommon(AMOUNT);
	    plan.save();

	    Plan searchPlan = new Plan();
	    searchPlan.find(plan.getId());

	    Assert.assertEquals(searchPlan.getId(), plan.getId());
    }

    @Test
    public void testListPlan() throws Throwable {
	    plan = this.planCommon(AMOUNT);
	    plan.save();

	    Collection<Plan> plans = plan.list();
	    Assert.assertTrue(plans.size() >= 1);
    }

    @Test
    public void testCreateSubscription() throws Throwable {

		createSubscription();

        Assert.assertEquals(subscription.getStatus(), SubscriptionStatus.paid);
	    Assert.assertNotNull(subscription.getId());
    }

    @Test
    public void testFindSubscription() throws Throwable {
		createSubscription();
	    Subscription searchSubscription = new Subscription();
	    searchSubscription.find(subscription.getId());

	    Assert.assertEquals(subscription.getId(), searchSubscription.getId());
	    Assert.assertEquals(subscription.getCard().getId(), searchSubscription.getCard().getId());
    }

	@Test
	public void testListSubscription() throws Throwable {
		createSubscription();

		Collection<Subscription> subscriptions = subscription.list();

		Assert.assertTrue(subscriptions.size() >= 1);
	}

	@Test
	public void testCancelSubscription() throws Throwable {
		createSubscription();

		subscription.cancel();

		Assert.assertEquals(subscription.getStatus(), SubscriptionStatus.canceled);
	}

	private void createSubscription() throws Throwable {
	    Customer customer = this.customerCommon();
	    Address address = this.addressCommon();
	    Phone phone = this.phoneCommon();

	    customer.setPhone(phone);
	    customer.setAddress(address);

	    plan = this.planCommon(AMOUNT);
	    plan.save();

	    Card card = this.cardCommon(customer);
	    card.save();

	    subscription = this.subscriptionCommon(plan, customer, card);
	    subscription.save();
    }

}