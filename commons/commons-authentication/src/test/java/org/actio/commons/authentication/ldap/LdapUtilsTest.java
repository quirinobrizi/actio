package org.actio.commons.authentication.ldap;

import static org.junit.Assert.assertNotNull;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;
import javax.naming.spi.InitialContextFactory;

import org.junit.Test;
import org.mockito.Mockito;

public class LdapUtilsTest {

    private LdapUtils testObj = new LdapUtils();

    @Test
    public void testBind() throws NamingException {
        LdapConfiguration ldapConfiguration = new LdapConfiguration();
        ldapConfiguration.setProvider("ldap://test.com:389");
        ldapConfiguration.setPrincipal("username");
        ldapConfiguration.setPassword("password");
        ldapConfiguration.setFactory(MockInitialDirContextFactory.class.getName());
        // act
        LdapContext context = testObj.bind(ldapConfiguration);
        // assert
        assertNotNull(context);
    }

    @Test
    public void testPrepareSearchControls() {
        LdapConfiguration ldapConfiguration = new LdapConfiguration();
        ldapConfiguration.setProvider("ldap://test.com:389");
        ldapConfiguration.setPrincipal("username");
        ldapConfiguration.setPassword("password");
        ldapConfiguration.setFactory(MockInitialDirContextFactory.class.getName());
        // act
        SearchControls searchControls = testObj.prepareSearchControls(ldapConfiguration);
        // assert
        assertNotNull(searchControls);
    }

    public static class MockInitialDirContextFactory implements InitialContextFactory {

        private static DirContext mockContext = null;

        /**
         * Returns the last DirContext (which is a Mockito mock) retrieved from
         * this factory.
         */
        public static DirContext getLatestMockContext() {
            return mockContext;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Context getInitialContext(Hashtable environment) throws NamingException {
            synchronized (MockInitialDirContextFactory.class) {
                mockContext = Mockito.mock(DirContext.class);
            }
            return mockContext;
        }
    }
}
