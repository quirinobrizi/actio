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

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.actio.commons.authentication.exception.TooManyElementException;
import org.activiti.engine.ActivitiException;

/**
 * LDAP operation template
 * 
 * @author quirino.brizi
 *
 */
public class LdapTemplate {

    private LdapContext ldapContext;
    private SearchControls searchControls;
    private LdapConfiguration ldapConfiguration;

    public static LdapTemplate newInstance(LdapContext ldapContext, SearchControls searchControls, LdapConfiguration ldapConfiguration) {
        return new LdapTemplate(ldapContext, searchControls, ldapConfiguration);
    }

    private LdapTemplate(LdapContext ldapContext, SearchControls searchControls, LdapConfiguration ldapConfiguration) {
        this.ldapContext = ldapContext;
        this.searchControls = searchControls;
        this.ldapConfiguration = ldapConfiguration;
    }

    public SearchResult findUserById(String identifier) {
        try {
            String search = this.ldapConfiguration.getUserIdSearchFilter().replaceAll("\\{userId\\}", identifier);
            NamingEnumeration<SearchResult> searchResults = this.ldapContext.search(this.ldapConfiguration.getBaseSearch(), search,
                    this.searchControls);
            SearchResult answer = null;
            if (searchResults.hasMoreElements()) {
                answer = searchResults.nextElement();
                if (searchResults.hasMoreElements()) {
                    throw TooManyElementException.newInstance("LDAP search by identifier resulted on more than 1 result");
                }
            }
            return answer;
        } catch (NamingException e) {
            throw new ActivitiException("unable to query LDAP server", e);
        }
    }

    public List<SearchResult> findUserByName(String name) {
        try {
            String search = this.ldapConfiguration.getUsernameSearchFilter().replaceAll("\\{username\\}", name);
            NamingEnumeration<SearchResult> searchResults = this.ldapContext.search(this.ldapConfiguration.getBaseSearch(), search,
                    this.searchControls);
            List<SearchResult> answer = new ArrayList<>();
            if (searchResults.hasMoreElements()) {
                answer.add(searchResults.nextElement());
            }
            return answer;
        } catch (NamingException e) {
            throw new ActivitiException("unable to query LDAP server", e);
        }
    }

    public List<SearchResult> findGroupByUserId(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<SearchResult> findGroupByName(String replaceAll) {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean tryBind(String username, String password) {
        LdapContext context = null;
        try {
            LdapConfiguration config = ldapConfiguration.clone();
            config.setPrincipal(username);
            config.setPassword(password);
            context = new LdapUtils().bind(config);
            return true;
        } catch (Exception e) {
            // an exception is thrown if unable to authenticate.
            return false;
        } finally {
            if (null != context) {
                try {
                    context.close();
                } catch (NamingException e) {
                    // NOOP
                }
            }
        }
    }

}
