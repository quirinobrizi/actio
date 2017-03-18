package org.actio.commons.authentication.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.actio.commons.authentication.exception.TooManyElementException;
import org.actio.commons.authentication.ldap.LdapUtilsTest.MockInitialDirContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LdapTemplateTest {

    @Mock
    private LdapContext ldapContext;
    @Mock
    private SearchControls searchControls;

    private LdapConfiguration ldapConfiguration = new LdapConfiguration();

    private LdapTemplate testObj;

    @Mock
    private NamingEnumeration<SearchResult> searchResults;
    @Mock
    private SearchResult searchResult;

    private String ldapSearchBase = "dc=ad,dc=my-domain,dc=com";

    @Before
    public void before() {
        ldapConfiguration.setBaseSearch(ldapSearchBase);
        ldapConfiguration.setUserIdSearchFilter("(&(objectClass=user)(objectSid={userId}))");
        ldapConfiguration.setUsernameSearchFilter("(&(objectClass=user)(sAMUserName={username}))");
        ldapConfiguration.setProvider("ldap://test.com:389");
        ldapConfiguration.setPrincipal("username");
        ldapConfiguration.setPassword("password");
        ldapConfiguration.setFactory(MockInitialDirContextFactory.class.getName());
        testObj = LdapTemplate.newInstance(ldapContext, searchControls, ldapConfiguration);
    }

    @Test
    public void testFindById() throws NamingException {
        String identifier = "id";
        String filter = "(&(objectClass=user)(objectSid=id))";
        when(ldapContext.search(ldapConfiguration.getBaseSearch(), filter, searchControls)).thenReturn(searchResults);
        when(searchResults.hasMoreElements()).thenReturn(true, false);
        when(searchResults.nextElement()).thenReturn(searchResult);
        // act
        SearchResult actual = testObj.findById(identifier);
        // assert
        assertEquals(searchResult, actual);
    }

    @Test(expected = TooManyElementException.class)
    public void testFindById_too_many_results() throws NamingException {
        String identifier = "id";
        String filter = "(&(objectClass=user)(objectSid=id))";
        when(ldapContext.search(ldapConfiguration.getBaseSearch(), filter, searchControls)).thenReturn(searchResults);
        when(searchResults.hasMoreElements()).thenReturn(true, true, false);
        when(searchResults.nextElement()).thenReturn(searchResult, searchResult);
        // act
        SearchResult actual = testObj.findById(identifier);
        // assert
        assertEquals(searchResult, actual);
    }

    @Test
    public void testFindByName() throws NamingException {
        String identifier = "id";
        String filter = "(&(objectClass=user)(sAMUserName=id))";
        when(ldapContext.search(ldapConfiguration.getBaseSearch(), filter, searchControls)).thenReturn(searchResults);
        when(searchResults.hasMoreElements()).thenReturn(true, false);
        when(searchResults.nextElement()).thenReturn(searchResult);
        // act
        List<SearchResult> actual = testObj.findByName(identifier);
        // assert
        assertEquals(Arrays.asList(searchResult), actual);
    }

    @Test
    public void testTryBind_success() {
        // act
        assertTrue(testObj.tryBind("username", "password"));
    }

}
