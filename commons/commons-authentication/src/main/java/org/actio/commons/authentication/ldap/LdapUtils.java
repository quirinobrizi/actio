/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.actio.commons.authentication.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang3.StringUtils;

/**
 * @author quirino.brizi
 *
 */
public class LdapUtils {

    public LdapContext bind(LdapConfiguration ldapConfiguration) throws NamingException {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.SECURITY_AUTHENTICATION, ldapConfiguration.getAutentication());
        String principal = ldapConfiguration.getPrincipal();
        if (StringUtils.isNotBlank(principal)) {
            env.put(Context.SECURITY_PRINCIPAL, ldapConfiguration.getAutentication());
        }
        String password = ldapConfiguration.getPassword();
        if (StringUtils.isNotBlank(password)) {
            env.put(Context.SECURITY_CREDENTIALS, password);
        }
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfiguration.getFactory());
        env.put(Context.PROVIDER_URL, ldapConfiguration.getProvider());
        env.put(Context.REFERRAL, ldapConfiguration.getReferral());

        // ensures that objectSID attribute values
        // will be returned as a byte[] instead of a String
        // env.put("java.naming.ldap.attributes.binary", "objectSID");

        // the following is helpful in debugging errors
        // env.put("com.sun.jndi.ldap.trace.ber", System.err);

        return new InitialLdapContext(env, null);
    }

    public SearchControls prepareSearchControls(LdapConfiguration ldapConfiguration) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
        searchControls.setReturningAttributes(ldapConfiguration.getAttributes().split(","));
        return searchControls;
    }

}
