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

import org.actio.commons.authentication.AuthenticationConfigurer;
import org.actio.commons.authentication.AuthenticationProvider;
import org.activiti.engine.impl.interceptor.SessionFactory;

/**
 * @author quirino.brizi
 *
 */
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private LdapConfiguration ldapConfiguration;

    public LdapAuthenticationProvider(LdapConfiguration ldapConfiguration) {
        this.ldapConfiguration = ldapConfiguration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.commons.authentication.AuthenticationProvider#
     * getUserManagerFactory()
     */
    @Override
    public SessionFactory getUserManagerFactory() {
        return new LdapUserManagerFactory(ldapConfiguration);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.commons.authentication.AuthenticationProvider#
     * getGroupManagerFactory()
     */
    @Override
    public SessionFactory getGroupManagerFactory() {
        return new LdapGroupManagerFactory(ldapConfiguration);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.commons.authentication.AuthenticationProvider#
     * getMembershiprManagerFactory()
     */
    @Override
    public SessionFactory getMembershiprManagerFactory() {
        return new LdapMembershipManagerFactory(ldapConfiguration);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.commons.authentication.AuthenticationProvider#
     * authenticationType()
     */
    @Override
    public String authenticationType() {
        return "ldap";
    }

    @Override
    public AuthenticationConfigurer authenticationConfigurer() {
        return null;
    }

}
