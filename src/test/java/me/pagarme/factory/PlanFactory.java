package me.pagarme.factory;


import java.util.Arrays;

import me.pagar.model.Plan;
import me.pagar.model.Transaction.PaymentMethod;

public class PlanFactory {

    public static final int DEFAULT_AMOUNT = 100;
    public static final int DEFAULT_DAYS = 30;
    public static final String DEFAULT_NAME = "Plano teste";

    public Plan create() {
        Plan plan = new Plan();
        plan.setCreationParameters(DEFAULT_AMOUNT, DEFAULT_DAYS, DEFAULT_NAME);
        plan.setPaymentMethods(Arrays.asList(
                PaymentMethod.BOLETO, PaymentMethod.CREDIT_CARD
        ));
        return plan;
    }


}
