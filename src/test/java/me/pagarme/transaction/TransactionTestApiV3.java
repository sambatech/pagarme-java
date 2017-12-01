/*
 * The MIT License
 *
 * Copyright 2017 Pagar.me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.pagarme.transaction;

import me.pagarme.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.pagar.model.Transaction;
import me.pagarme.factory.CustomerFactory;
import me.pagarme.factory.TransactionFactory;

public class TransactionTestApiV3 extends BaseTest {
    private CustomerFactory customerFactory = new CustomerFactory();
    private TransactionFactory transactionFactory = new TransactionFactory();
    private static Integer AMOUNT = 1000;

    /*
        API Version 2017-08-28
    */

    @Before
    public void SetUpV3() {
        super.setUp("2017-08-28");
        transaction = new Transaction();
    }

    @Test
    public void testCreditCardTransactionApiV3() throws Throwable {
        transaction = transactionFactory.createCreditCardTransactionApiV3();
        transaction.setAmount(AMOUNT);
        customer = customerFactory.createApiV3();
        transaction.setCustomer(customer);
        transaction.save();

        Assert.assertEquals(Transaction.Status.PAID, transaction.getStatus());
    }

    @Test
    public void testBoletoTransactionApiV3() throws Throwable {
        transaction = transactionFactory.createBoletoTransactionApiV3();
        transaction.setAmount(AMOUNT);
        customer = customerFactory.createApiV3();
        transaction.setCustomer(customer);
        transaction.save();

        Assert.assertEquals(Transaction.Status.WAITING_PAYMENT, transaction.getStatus());
    }
}
