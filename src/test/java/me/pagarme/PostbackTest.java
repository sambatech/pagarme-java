package me.pagarme;

import me.pagar.model.PagarMe;
import org.junit.Assert;
import org.junit.Test;

public class PostbackTest extends BaseTest {
    
    @Test
    public void testNullOrEmptyParams() {
        Assert.assertFalse( PagarMe.validateRequestSignature("", "") );
        Assert.assertFalse( PagarMe.validateRequestSignature(null, null) );        
    }
}
