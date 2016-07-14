package com.pagarme;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pagarme.model.*;

import java.util.Collection;

public class CustomerTest extends BaseTest {

    private Customer customer;

    @Before
    public void setUp() {
        super.setUp();
        customer = new Customer();
    }

    @Test
    public void testCreateCustomer() {

        Customer customer = this.customerCreateCommon();

        try {
            customer.save();

            Address customerAddress = customer.getAddress();
            Phone customerPhone = customer.getPhone();

            this.assertCustomer(customer, customerAddress, customerPhone);

        } catch (PagarMeException ex) {
            throw new UnsupportedOperationException(ex);
        }

    }

    @Test
    public void testCustomerFind() {

        Customer customer = this.customerCreateCommon();

        try {

            customer.save();

            Integer customerId = customer.getId();
            customer.find(customerId);

            Address customerAddress = customer.getAddress();
            Phone customerPhone = customer.getPhone();

           this.assertCustomer(customer, customerAddress, customerPhone);

        } catch (PagarMeException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    @Test
    public void testCustomerCollectionFind() {

        Customer createCustomer = this.customerCreateCommon();

        try {

            createCustomer.save();

            Collection<Customer> customerCollection = customer.findCollection(1,0);
            Assert.assertEquals(customerCollection.size(), 1);

            for (Customer customer : customerCollection) {

                this.assertCustomerData(customer);

                for (Address address : customer.getAddresses()) {
                    Address customerAddress = address;
                    this.assertAddress(customerAddress);
                }

                for (Phone phone : customer.getPhones()) {
                    Phone customerPhone = phone;
                    this.assertPhone(phone);
                }
            }

        } catch (PagarMeException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}