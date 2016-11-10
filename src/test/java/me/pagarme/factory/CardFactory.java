package me.pagarme.factory;

import me.pagar.model.Card;

public class CardFactory {

    public static String CARD_HOLDER_NAME = "Pagarme LTDA";
    public static String CARD_NUMBER = "4111111111111111";
    public static Integer CARD_CVV = 401;
    public static String CARD_EXPIRATION_DATE = "1022";

    public Card create(){
        Card card = new Card();
        card.setHolderName(CARD_HOLDER_NAME);
        card.setNumber(CARD_NUMBER);
        card.setExpiresAt(CARD_EXPIRATION_DATE);
        card.setCvv(CARD_CVV);
        return card;
    }
}
