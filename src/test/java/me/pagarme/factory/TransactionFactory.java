package me.pagarme.factory;

import org.joda.time.LocalDate;

import me.pagar.model.Transaction;

public class TransactionFactory {

    /**
     * @param pinMode can be online or offline. With online option, the bank will check the card, and with offline the pin is used to check the card. 
     * @return the transaction
     */
    public Transaction create(String pinMode) {
        Transaction transaction = new Transaction();
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.DEBIT_CARD);
        transaction.setCaptureMethod(Transaction.CaptureMethod.EMV);
        transaction.setCardHolderName("Lucas Dos Santos Alves");
        transaction.setCardExpirationDate("0517");
        transaction.setCardNumber("4111111111111111");
        transaction.setInstallments(1);
        transaction.setCardEmvData("0");
        transaction.setCardTrack1("0");
        transaction.setCardTrack2("0");
        transaction.setCardTrack3("0");
        transaction.setCardPinMode(pinMode);

        if (pinMode.equals("offline")) return transaction;

        transaction.setCardPin("0");
        transaction.setCardPinKek("0");

        return transaction;
    }
    
    public Transaction createCreditCardTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        transaction.setCardHolderName("Lucas Dos Santos Alves");
        transaction.setCardExpirationDate("0517");
        transaction.setCardCvv("401");
        transaction.setCardNumber("4111111111111111");
        transaction.setInstallments(1);

        return transaction;
    }
    
    public Transaction createBoletoTransaction() {
        Transaction transaction = new Transaction();
        transaction.setBoletoExpirationDate(LocalDate.now().plusDays(4));
        transaction.setAmount(100);
        transaction.setPaymentMethod(Transaction.PaymentMethod.BOLETO);
        return transaction;
    }
}
