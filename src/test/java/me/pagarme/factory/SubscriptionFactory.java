package me.pagarme.factory;

import me.pagar.model.Customer;
import me.pagar.model.Subscription;

public class SubscriptionFactory {

    public Subscription createCreditCardSubscription(String planId, String cardId, Customer customer){
        Subscription subscription = new Subscription();
        subscription.setCreditCardSubscriptionWithCardId(planId, cardId, customer);
        return subscription;
    }

    public Subscription createBoletoSubscription(String planId, Customer customer){
        Subscription subscription = new Subscription();
        subscription.setBoletoSubscription(planId, customer);
        return subscription;
    }
}
