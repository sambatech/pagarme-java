package me.pagarme;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Assert;
import me.pagar.model.BankAccount;
import me.pagar.model.Card;
import me.pagar.model.Customer;
import me.pagar.model.PagarMeModel;
import me.pagar.model.Recipient;
import me.pagar.model.Transaction;
import me.pagar.util.JSONUtils;

public class ConverterTests {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testDateTimeConversionForAllRelatedModels(){
        String json = "{'date_created': '2015-02-25T00:00:00.000Z'}";
        Class[] classes = new Class[]{
            Transaction.class, Card.class, Customer.class, Recipient.class,  BankAccount.class
        };
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        
        for (Class clazz : classes) {
            PagarMeModel modelInstance = JSONUtils.getAsObject(jsonObject, clazz);
            System.out.println(modelInstance.getCreatedAt());
            Assert.assertNotNull(modelInstance.getCreatedAt());
        }
    }
}
