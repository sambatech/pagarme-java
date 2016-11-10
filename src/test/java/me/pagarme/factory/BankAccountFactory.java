package me.pagarme.factory;

import me.pagar.model.BankAccount;

public class BankAccountFactory {

    public BankAccount create(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAgencia("0192");
        bankAccount.setAgenciaDv("0");
        bankAccount.setBankCode("341");
        bankAccount.setConta("03245");
        bankAccount.setContaDv("0");
        bankAccount.setAgenciaDv("0");
        bankAccount.setLegalName("Conta teste");
        bankAccount.setDocumentNumber("18344334799");
        
        return bankAccount;
    }
}
