package com.niki.msgserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple GreetingServer.
 *
 *  Author: Ahmed
 *
 *  Unit tests helper
 *
 */
public class AmsMessageServerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AmsMessageServerTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( AmsMessageServerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
