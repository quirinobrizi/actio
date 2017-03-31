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

import org.actio.commons.authentication.exception.OperationNotSupportedExcepion;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.MembershipIdentityManager;

/**
 * @author quirino.brizi
 *
 */
public class LdapMembershipManagerFactory implements SessionFactory {

    private LdapConfiguration ldapConfiguration;
    private LdapUtils ldapUtils;

    public LdapMembershipManagerFactory(LdapConfiguration ldapConfiguration) {
        this.ldapConfiguration = ldapConfiguration;
        this.ldapUtils = new LdapUtils();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.interceptor.SessionFactory#getSessionType()
     */
    @Override
    public Class<?> getSessionType() {
        return MembershipIdentityManager.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.interceptor.SessionFactory#openSession()
     */
    @Override
    public Session openSession() {
        throw OperationNotSupportedExcepion.newInstance("Memberships are not supported in LDAP");
    }

}
