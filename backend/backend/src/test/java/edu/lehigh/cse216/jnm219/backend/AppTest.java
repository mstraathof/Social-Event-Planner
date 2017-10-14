package edu.lehigh.cse216.jnm219.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.math.BigInteger;
/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Test the getIntFromEnv Method
     */
    public void testGetIntFromEnv()
    {
        int a = App.getIntFromEnv("PORT", 4567);
        assertEquals(4567, a);
    }
    public void testCheckKey()
    {
        App app= new App();
        app.logged_in.put("username",1234);
        boolean check1= app.checkKey("username",1234);
        assertTrue(check1);
        boolean check2= app.checkKey("username",124);
        assertFalse(check2);
        boolean check3= app.checkKey("rname",1234);
        assertFalse(check3);
    }

    
}
