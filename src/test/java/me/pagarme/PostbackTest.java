package me.pagarme;

import me.pagar.model.PagarMe;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class PostbackTest extends BaseTest {
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testNullOrEmptyParams() {
        Assert.assertFalse( PagarMe.validateRequestSignature("", "") );
        Assert.assertFalse( PagarMe.validateRequestSignature(null, null) );        
    }
}
