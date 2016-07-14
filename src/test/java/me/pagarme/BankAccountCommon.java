package me.pagarme;

import org.junit.Before;
import me.pagar.model.BankAccount;

public class BankAccountCommon extends BaseTest {

    protected BankAccount bankAccount;

    @Before
    public void setUp() {
        super.setUp();
        bankAccount = new BankAccount();
    }

    protected static String AGENCIA = "0192";
    protected static String AGENCIA_DV = "0";
    protected static String CONTA = "03245";
    protected static String CONTA_DV = "0";
    protected static String BANK_CODE = "0341";
    protected static String DOCUMENT_NUMBER = "26268738888";
    protected static String LEGAL_NAME = "Java Lib Bank Account";

    protected BankAccount bankAccountDefault () {

        bankAccount.setAgencia(AGENCIA);
        bankAccount.setAgenciaDv(AGENCIA_DV);
        bankAccount.setConta(CONTA);
        bankAccount.setContaDv(CONTA_DV);
        bankAccount.setBankCode(BANK_CODE);
        bankAccount.setDocumentNumber(DOCUMENT_NUMBER);
        bankAccount.setLegalName(LEGAL_NAME);

        return bankAccount;
    }

}
