package me.pagarme.helper;

import java.util.Collection;

import org.joda.time.DateTime;

import me.pagar.model.BulkAnticipation;
import me.pagar.model.BulkAnticipation.Timeframe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Recipient;
import me.pagar.model.Transaction;
import me.pagarme.factory.TransactionFactory;

public class TestCases {

    private static TransactionFactory transactionFactory = new TransactionFactory();

    public static Recipient increaseAnticipationVolume(Recipient recipient) throws PagarMeException{
        recipient.setAnticipatableVolumePercentage(100);
        return recipient.save();
    }

    public static BulkAnticipation createAnticipationOnRecipient(Integer requestedAmount, Recipient recipient) throws PagarMeException{
        Transaction transaction = transactionFactory.createCreditCardTransactionWithoutPinMode();
        transaction.setAmount(requestedAmount);
        transaction.save();

        BulkAnticipation anticipation = new BulkAnticipation();
        anticipation.setRequiredParametersForCreation(new DateTime().plusDays(3), Timeframe.END, requestedAmount, false);
        anticipation = recipient.anticipate(anticipation);
        return anticipation;
    }

    public static BulkAnticipation createBuildingAnticipationOnRecipient(Integer requestedAmount, Recipient recipient) throws PagarMeException{
        Transaction transaction = transactionFactory.createCreditCardTransactionWithoutPinMode();
        transaction.setAmount(requestedAmount);
        transaction.save();

        BulkAnticipation anticipation = new BulkAnticipation();
        anticipation.setRequiredParametersForCreation(new DateTime().plusDays(3), Timeframe.END, requestedAmount, true);
        anticipation = recipient.anticipate(anticipation);
        return anticipation;
    }

    public static Recipient getDefaultRecipient() throws PagarMeException{
        Collection<Recipient> recipients = new Recipient().findCollection(10, 0);
        return recipients.iterator().next();
    }
}
