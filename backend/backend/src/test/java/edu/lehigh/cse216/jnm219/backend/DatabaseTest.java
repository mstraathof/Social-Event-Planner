package edu.lehigh.cse216.jnm219.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Random;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;
import java.io.IOException;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

public class DatabaseTest extends TestCase
{
    Random rand = new Random();
    int  random = rand.nextInt(10000) + 1000;
    String use= Integer.toString(random);
    String username = "testuser"+use;

    public void testApp()
    {
        assertTrue( true );
    }
    /**
     * Test the insertRow method of Database
     */
    public void testInsertUser()
    {
        Database dbt = Database.getDatabase(2);
        
        boolean check = dbt.insertUser(username,"Real Name", "Email@email.com");
        assertTrue(check);
        dbt.disconnect();
    }
    
    public void testInsertProfile()
    {
        Database dbt = Database.getDatabase(2);
        boolean chk=dbt.insertProfile(username);
        boolean check=dbt.insertProfile(username);
        assertFalse(check);
        dbt.disconnect();
    }
    public void testUpdateProfile()
    {
        Database dbt = Database.getDatabase(2);
        boolean check=dbt.updateProfile(username,"This is the new profile table");
        assertTrue(check);
        dbt.disconnect();
    }


    /**
     * Test the selectAll method of Database
     */
    /*
    public void testSelectAll()
    {

        Database dbt = Database.getDatabase(2);
        dbt.insertRow("s", "t");
        ArrayList<RowData> list = dbt.selectAll();
        int listLength = list.size();
        assertEquals(false, list.get(listLength-1).mSubject.equals("s"));
    }*/

    /**
     * Test the selectOne method of Database
     *//*
    public void testSelectOne()
    {
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("u", "v");
        ArrayList<RowData> list = dbt.selectAll();
        int listLength = list.size();
        int id = list.get(listLength-1).mId;
        assertEquals(id, dbt.selectOne(id).mId);
    }*/
    
    /**
     * Test the upVote method of Database
     *//*
    public void testupVote()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("w", "x");
        assertEquals(false, -1 == dbt.upVote(1, 1));
    }*/
    /**
     * Test the downVote method of Database
     */
    /*
    public void testdownVote()
    {
        Spark.port(App.getIntFromEnv("PORT", 5432));
        Database dbt = Database.getDatabase(2);
        dbt.insertRow("y", "z");
        assertEquals(false, -1 == dbt.upVote(1, -1));
    }*/

    /*
        public void testUpdatePassword()throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        Database dbt = Database.getDatabase(2);
        App abc= new App();
        byte [] salt= abc.getSalt();
        byte [] pw= abc.encryptPw("aaa",salt);
        boolean check =dbt.updatePassword("mira",pw,salt);
        assertTrue(check);
        dbt.disconnect();
    }*/

    /*
    public void testSelectOneUser()throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        Database dbt = Database.getDatabase(2);
        App abc= new App();
        boolean check = dbt.selectOneUser(username);
        assertTrue(check);
        dbt.disconnect();
    }

    public void testInsertOneMessage()
    {
        Database dbt = Database.getDatabase(2);
        boolean check=dbt.insertOneMessage("subject525","message321",username);
        assertTrue(check);
        dbt.disconnect();
    }
    public void testInsertComment()
    {
        Database dbt = Database.getDatabase(2);
        boolean check=dbt.insertComment(username,1,"comment");
        assertTrue(check);
        dbt.disconnect();
    }
    public void testUpVote()
    {
        Database dbt = Database.getDatabase(2);
        boolean check=dbt.updateUpVote(username,1);
        assertTrue(check);
        dbt.disconnect();
    }
    public void testDownVote()
    {
        Database dbt = Database.getDatabase(2);
        boolean check=dbt.updateDownVote(username,1);
        assertTrue(check);
        dbt.disconnect();
    }*/
}