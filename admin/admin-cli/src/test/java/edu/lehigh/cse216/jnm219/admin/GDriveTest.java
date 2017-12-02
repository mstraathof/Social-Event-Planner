package edu.lehigh.cse216.jnm219.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unit test for simple GDrive connection.
 */
public class GDriveTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GDriveTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GDriveTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testGDrive()
    {
        assertTrue( true );
    }
    /**
     * Making sure I get connected to google drive
     */
    public void testService()
    {
        Boolean errorCheck;
        try{
            Drive service = GDrive.getDriveService();
            FileList result = service.files().list()
            .execute();
            errorCheck = true;
        } catch (IOException e){
            errorCheck = false;
        }
        assertTrue(errorCheck);
    }

}
