package com.pagarme;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pagarme.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BankAccountTest extends BankAccountCommon {

    @Test
    public void testCreateBackAccount() {

        bankAccount = this.bankAccountDefault();

        try {

            bankAccount.save();

            Assert.assertEquals(bankAccount.getAgencia(), AGENCIA);
            Assert.assertEquals(bankAccount.getAgenciaDv(), AGENCIA_DV);
            Assert.assertEquals(bankAccount.getConta(), CONTA);
            Assert.assertEquals(bankAccount.getContaDv(), CONTA_DV);
            Assert.assertEquals(bankAccount.getBankCode(), BANK_CODE);
            Assert.assertEquals(bankAccount.getDocumentNumber(), DOCUMENT_NUMBER);
            Assert.assertEquals(bankAccount.getLegalName(), LEGAL_NAME);

        } catch (Exception exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testFindBankAccountById() {

        bankAccount = this.bankAccountDefault();

        try {

            bankAccount.save();
            BankAccount currentBanckAccount = bankAccount.find(bankAccount.getId());

            Assert.assertEquals(currentBanckAccount.getAgencia(), AGENCIA);
            Assert.assertEquals(currentBanckAccount.getAgenciaDv(), AGENCIA_DV);
            Assert.assertEquals(currentBanckAccount.getConta(), CONTA);
            Assert.assertEquals(currentBanckAccount.getContaDv(), CONTA_DV);
            Assert.assertEquals(currentBanckAccount.getBankCode(), BANK_CODE);
            Assert.assertEquals(currentBanckAccount.getDocumentNumber(), DOCUMENT_NUMBER);
            Assert.assertEquals(currentBanckAccount.getLegalName(), LEGAL_NAME);

        } catch (Exception exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testFindBankAccountCollection() {

        bankAccount = this.bankAccountDefault();

        try {

            bankAccount.save();
            Collection<BankAccount> currentBanckAccount = bankAccount.findCollection(1,0);

            Assert.assertEquals(currentBanckAccount.size(), 1);

            for (BankAccount bankAccount : currentBanckAccount) {

                Assert.assertEquals(bankAccount.getAgencia(), AGENCIA);
                Assert.assertEquals(bankAccount.getAgenciaDv(), AGENCIA_DV);
                Assert.assertEquals(bankAccount.getConta(), CONTA);
                Assert.assertEquals(bankAccount.getContaDv(), CONTA_DV);
                Assert.assertEquals(bankAccount.getBankCode(), BANK_CODE);
                Assert.assertEquals(bankAccount.getDocumentNumber(), DOCUMENT_NUMBER);
                Assert.assertEquals(bankAccount.getLegalName(), LEGAL_NAME);
            }

        } catch (Exception exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

}
