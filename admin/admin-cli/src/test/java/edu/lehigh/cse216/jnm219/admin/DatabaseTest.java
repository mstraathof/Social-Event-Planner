package edu.lehigh.cse216.jnm219.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Database.
 */
public class DatabaseTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DatabaseTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testDatabase()
    {
        assertTrue( true );
    }
    /**
     * Making sure I get disconnected to the database
     */
    public void testDisconnect()
    {
        Database db = Database.getDatabase("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        assertTrue(db.disconnect());
       
    }
    /**
     * Test creating the table, if it creates an extra, it should return false
     */
    public void testCreateAllTables()
    {
        Database db = Database.getDatabase("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        System.out.println("in testCreateAllTables");
        db.dropAllTables();
        //db.dropTable("tblUser");
        assertTrue(db.createAllTables());
        //assertFalse(db.createAllTables());    // createAllTables() never fails because sql includes "IF NOT EXISTS"
        db.disconnect();
    }
    /**
     * Test dropping the table, if it drops nothing, it should return false
     */
    
    public void testDropAllTables()
    {
        Database db = Database.getDatabase ("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        System.out.println("in testDropAllTables");
        assertTrue(db.dropAllTables());
        assertFalse(db.dropAllTables());
        db.createAllTables();
        db.disconnect();
    }

    public void testAddToTblUnauthTable()
    {
        Database db = Database.getDatabase ("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        System.out.println("in testAddToTblUnauthTable");
        db.dropAllTables();
        db.createAllTables();
        assertTrue(db.addToTblUnauthTable("username", "realname", "mts219@lehigh.edu"));
        // username must be unique
        assertFalse(db.addToTblUnauthTable("username", "realname", "mts219@lehigh.edu")); 
    }

    public void testAuthorizeUser()
    {
        Database db = Database.getDatabase ("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        System.out.println("in testAuthorizeUser");
        db.dropAllTables();
        db.createAllTables();
        db.addToTblUnauthTable("mira", "Mira Straathof", "mts219@lehigh.edu");
        String[] credentials = new String[4];   // username, realname, email, password
        credentials[0] = "mira";
        assertTrue(db.authorizeUser(credentials));
    }

    public void testRejectUser()
    {
        Database db = Database.getDatabase ("jdbc:postgresql://ec2-107-22-211-182.compute-1.amazonaws.com:5432/dd8h04ocdonsvj?user=qcxhljggghpbxa&password=6d462cf3d5d52813f0a69912a10908fad2ff06725737ce41e0cf0750b83d2375&sslmode=require");
        System.out.println("in testRejectUser");
        db.dropAllTables();
        db.createAllTables();
        db.addToTblUnauthTable("mira", "Mira Straathof", "mts219@lehigh.edu");
        assertTrue(db.rejectUser("mira"));
    }

}
