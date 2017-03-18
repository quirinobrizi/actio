package org.actio.commons.authentication.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.actio.commons.authentication.ldap.LdapUtilsTest.MockInitialDirContextFactory;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.junit.Test;

public class LdapUserManagerFactoryTest {

    private LdapConfiguration ldapConfiguration = new LdapConfiguration();
    private LdapUserManagerFactory testObj = new LdapUserManagerFactory(ldapConfiguration);

    @Test
    public void testGetSessionType() {
        assertEquals(UserIdentityManager.class, testObj.getSessionType());
    }

    @Test
    public void testOpenSession() {
        ldapConfiguration.setProvider("ldap://test.com:389");
        ldapConfiguration.setPrincipal("username");
        ldapConfiguration.setPassword("password");
        ldapConfiguration.setFactory(MockInitialDirContextFactory.class.getName());
        // act
        Session actual = testObj.openSession();
        // assert
        assertNotNull(actual);
    }

}
