package me.pagar.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import me.pagar.model.CardHashKey;

public class CardHashUtil {

    /**
     * Encrypts the card data.
     * @param publicKey - Public key.
     * @param cardData - Card data.
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeySpecException
     */
    private static String encryptCard(String publicKey, String cardData) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        byte[] cardHash = encrypt(cardData, getKey(publicKey));
        return Base64.encodeBase64String(cardHash);
    }

    /**
     * Encrypts data using a crypt class.
     * @param text
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] encrypt(String text, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text.getBytes());
    }

    /**
     * Gets the public key.
     * @param key - Key.
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PublicKey getKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] pubKey = key.getBytes();
        byte[] byteKey = Base64.decodeBase64(pubKey);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(X509publicKey);
    }

    private static String cleanPublicKey(String publicKey) {
        String key = publicKey;
        key = key.replace("-----BEGIN PUBLIC KEY-----", "");
        key = key.replace("-----END PUBLIC KEY-----", "");
        key = key.replace("\n", "");
        return key;
    }

    /**
     * Generates the card hash.
     * @param data - Card info. Example: "card_number=4901720080344448&card_holder_name=Usuario%20de%20Teste&card_expiration_date=1217&card_cvv=314"
     * @param cardHashKey - Client public key.
     * @return Card hash
     * @throws Exception
     */
    public static String generateCardHash(CardHashKey cardHashKey, String data) throws Exception {
        String pubKey = cleanPublicKey(cardHashKey.getPublicKey());
        return cardHashKey.getId() + "_" + encryptCard(pubKey, data);
    }
}
