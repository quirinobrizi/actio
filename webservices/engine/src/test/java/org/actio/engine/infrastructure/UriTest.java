package org.actio.engine.infrastructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UriTest {

    private Uri testObj = Uri.from("http://localhost:8080?authType=Basic&authToken=123-XYZ");

    @Test
    public void testGetQueryParameter() {
        assertEquals("123-XYZ", testObj.getQueryParameter("authToken"));
    }

    @Test
    public void testRemoveQueryString() {
        assertEquals("http://localhost:8080", testObj.removeQueryString().toString());
    }

}
