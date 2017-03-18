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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Configuration for LDAP server
 * 
 * @author quirino.brizi
 *
 */

public class LdapConfiguration implements Cloneable {

    private String factory = "com.sun.jndi.ldap.LdapCtxFactory";
    private String autentication = "simple";
    private String principal;
    private String password;
    private String provider;
    private String attributes;
    private String referral = "follow";
    private String baseSearch;
    private String userIdSearchFilter = "(&(objectClass=user)(objectSid={userId}))";
    private String usernameSearchFilter = "(&(objectClass=user)(sAMUserName={username}))";

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getAutentication() {
        return autentication;
    }

    public void setAutentication(String autentication) {
        this.autentication = autentication;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getBaseSearch() {
        return baseSearch;
    }

    public void setBaseSearch(String baseSearch) {
        this.baseSearch = baseSearch;
    }

    public String getAttributes() {
        return StringUtils.isBlank(attributes) ? "" : attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getUserIdSearchFilter() {
        return userIdSearchFilter;
    }

    public void setUserIdSearchFilter(String userSearchFilter) {
        this.userIdSearchFilter = userSearchFilter;
    }

    public String getUsernameSearchFilter() {
        return usernameSearchFilter;
    }

    public void setUsernameSearchFilter(String usernameSearchFilter) {
        this.usernameSearchFilter = usernameSearchFilter;
    }

    @Override
    public LdapConfiguration clone() {
        LdapConfiguration answer = new LdapConfiguration();
        answer.setAttributes(attributes);
        answer.setAutentication(autentication);
        answer.setBaseSearch(baseSearch);
        answer.setFactory(factory);
        answer.setPassword(password);
        answer.setPrincipal(principal);
        answer.setProvider(provider);
        answer.setReferral(referral);
        answer.setReferral(referral);
        answer.setUserIdSearchFilter(userIdSearchFilter);
        answer.setUsernameSearchFilter(usernameSearchFilter);
        return answer;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("factory", factory).append("autentication", autentication).append("principal", principal)
                .append("password", password).append("provider", provider).append("attributes", attributes).append("referral", referral)
                .append("baseSearch", baseSearch).append("userIdSearchFilter", userIdSearchFilter)
                .append("userNameSearchFilter", usernameSearchFilter);
        return builder.toString();
    }

}
