package me.pagarme;


import org.junit.Assert;
import org.junit.Test;
import me.pagar.model.BankAccount;
import me.pagar.model.PagarMeException;
import me.pagar.model.Recipient;

import java.util.Collection;

public class RecipientTest extends RecipientCommon {

    BankAccount bankAccount;

    @Test
    public void testCreateRecipient() {

        int bankAccountId = this.getBankAccountId();

        recipient = this.recipientDefault();
        recipient.setBankAccountId(bankAccountId);

        try {
            recipient.save();
            Assert.assertEquals(recipient.getTransferInterval(), Recipient.TransferInterval.WEEKLY);
            Assert.assertEquals(recipient.isTransferEnabled(), TRANSFER_ENABLE);

            int recipientBankAccountId = recipient.getBankAccount().getId();
            Assert.assertEquals(recipientBankAccountId, bankAccountId);

        }  catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testRecipientFind() {

        int bankAccountId = this.getBankAccountId();

        recipient = this.recipientDefault();
        recipient.setBankAccountId(bankAccountId);

        try {

            recipient.save();
            recipient.find(recipient.getId());

            Assert.assertEquals(recipient.getTransferInterval(), Recipient.TransferInterval.WEEKLY);
            Assert.assertEquals(recipient.isTransferEnabled(), TRANSFER_ENABLE);

            int recipientBankAccountId = recipient.getBankAccount().getId();
            Assert.assertEquals(recipientBankAccountId, bankAccountId);

        }  catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testRecipientFindCollection() {

        int bankAccountId = this.getBankAccountId();

        recipient = this.recipientDefault();
        recipient.setBankAccountId(bankAccountId);

        try {

            recipient.save();
            Collection<Recipient> recipientCollection =  recipient.findCollection(1,0);

            for (Recipient recipient : recipientCollection) {
                Assert.assertEquals(recipient.getTransferInterval(), Recipient.TransferInterval.WEEKLY);
                Assert.assertEquals(recipient.isTransferEnabled(), TRANSFER_ENABLE);

                int recipientBankAccountId = recipient.getBankAccount().getId();
                Assert.assertEquals(recipientBankAccountId, bankAccountId);
            }

        }  catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    /**
     *
     * @return
     */
    private Integer getBankAccountId() {

        bankAccount = new BankAccount();
        bankAccount.setAgencia(AGENCIA);
        bankAccount.setAgenciaDv(AGENCIA_DV);
        bankAccount.setConta(CONTA);
        bankAccount.setContaDv(CONTA_DV);
        bankAccount.setBankCode(BANK_CODE);
        bankAccount.setDocumentNumber(DOCUMENT_NUMBER);
        bankAccount.setLegalName(LEGAL_NAME);

        try {

            bankAccount.save();
            return bankAccount.getId();

        } catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

}
