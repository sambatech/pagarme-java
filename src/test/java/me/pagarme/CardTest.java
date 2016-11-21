package me.pagarme;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.pagar.model.Card;
import me.pagar.model.CardHashKey;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transaction;
import me.pagar.util.CardHashUtil;

public class CardTest extends BaseTest {

    @Before
    public void setUp() {
        super.setUp();
        card = new Card();
    }

    Card card;

    private static String HOLDER_NAME = "Pagarme LTDA";
    private static String CARD_NUMBER = "4111111111111111";
    private static String CARD_EXPIRATION_DATE = "092019";
    private static Integer CARD_CVV = 401;
    private static String EXPIRATION_DATE = "1022";

    @Test
    public void testCreateCard() {

        card.setHolderName(HOLDER_NAME);
        card.setNumber(CARD_NUMBER);
        card.setExpiresAt(CARD_EXPIRATION_DATE);
        card.setCvv(CARD_CVV);
        card.setExpiresAt(EXPIRATION_DATE);

        try {

            card.save();
            Assert.assertNotNull(card.getId());

        } catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testCreateCardFind() {

        card.setHolderName(HOLDER_NAME);
        card.setNumber(CARD_NUMBER);
        card.setExpiresAt(CARD_EXPIRATION_DATE);
        card.setCvv(CARD_CVV);
        card.setExpiresAt(EXPIRATION_DATE);

        try {

            card.save();
            Card cardFind = card.find(card.getId());

            Assert.assertNotNull(cardFind.getId());
            Assert.assertEquals(cardFind.getHolderName(), HOLDER_NAME);

        } catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }

    @Test
    public void testCreateCardHash() throws  Throwable {
        Transaction transaction = new Transaction();
        CardHashKey cardHashKey = transaction.cardHashKey();

        String data = "card_number=4901720080344448&card_holder_name=Usuario%20de%20Teste&card_expiration_date=1217&card_cvv=314";
        String hash = CardHashUtil.generateCardHash(cardHashKey, data);

        Card card = new Card();
        card.setHash(hash);
        card.save();

        Assert.assertEquals(card.getHolderName(), "Usuario de Teste");
        Assert.assertEquals(card.getLastDigits(), "4448");
    }
}
