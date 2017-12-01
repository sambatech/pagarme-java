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
package me.pagarme.factory;

import me.pagar.model.Shipping;

public class ShippingFactory {
    public static final String DEFAULT_NAME = "Qwe qwe qwe";
    public static final int DEFAULT_FEE = 100;
    public static final String DEFAULT_DELIVERY_DATE = "2020-12-31";
    public static final Boolean DEFAULT_EXPEDITED = true;

    private AddressFactory addressFactory = new AddressFactory();

    public Shipping create() {
        Shipping shipping = new Shipping();
        shipping.setName(DEFAULT_NAME);
        shipping.setFee(DEFAULT_FEE);
        shipping.setDeliveryDate(DEFAULT_DELIVERY_DATE);
        shipping.setExpedited(DEFAULT_EXPEDITED);
        shipping.setAddress(addressFactory.create());

        return shipping;
    }
}
