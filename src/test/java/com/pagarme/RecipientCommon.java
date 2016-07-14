package com.pagarme;

import org.junit.Before;
import pagarme.model.Recipient;

public class RecipientCommon extends BaseTest {

    protected Recipient recipient;

    protected static String AGENCIA = "0192";
    protected static String AGENCIA_DV = "0";
    protected static String CONTA = "03245";
    protected static String CONTA_DV = "0";
    protected static String BANK_CODE = "0341";
    protected static String DOCUMENT_NUMBER = "26268738888";
    protected static String LEGAL_NAME = "Java Lib Bank Account";

    @Before
    public void setUp() {
        super.setUp();
        recipient = new Recipient();
    }

    protected static Integer TRANSFER_DAY = 1;
    protected static Boolean TRANSFER_ENABLE = true;

    protected Recipient recipientDefault() {
        recipient.setTransferInterval(Recipient.TransferInterval.WEEKLY);
        recipient.setTransferDay(TRANSFER_DAY);
        recipient.setTransferEnabled(TRANSFER_ENABLE);

        return recipient;
    }

}
