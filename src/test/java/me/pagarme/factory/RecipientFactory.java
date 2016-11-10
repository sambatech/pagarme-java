package me.pagarme.factory;

import me.pagar.model.BankAccount;
import me.pagar.model.Recipient;
import me.pagar.model.Recipient.TransferInterval;

public class RecipientFactory {

    private BankAccountFactory bankAccountFactory = new BankAccountFactory();
    
    public Recipient create(){
        BankAccount bankAccount = bankAccountFactory.create();
        Recipient recipient = new Recipient();
        recipient.setBankAccount(bankAccount);
        recipient.setTransferDay(1);
        recipient.setTransferEnabled(true);
        recipient.setTransferInterval(TransferInterval.WEEKLY);
        return recipient;
    }
}
