package me.pagar;

import com.google.gson.annotations.SerializedName;

public enum BankAccountType {
    @SerializedName("conta_corrente")
    CONTA_CORRENTE,
    @SerializedName("conta_poupanca")
    CONTA_POUPANCA,
    @SerializedName("conta_corrente_conjunta")
    CONTA_CORRENTE_CONJUNTA,
    @SerializedName("conta_poupanca_conjunta")
    CONTA_POUPANCA_CONJUNTA
}
