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
package org.actio.modeler.infrastructure.security;

import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.actio.commons.message.identity.AuthenticateRequestMessage;
import org.actio.commons.message.identity.GroupMessage;
import org.actio.commons.message.identity.UserMessage;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.actio.modeler.infrastructure.security.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
public class ActioAuthenticationProvider implements AuthenticationProvider {

    private RestTemplate restTemplate;
    private ModelerConfigurationProperties configuration;

    public ActioAuthenticationProvider(RestTemplate restTemplate, ModelerConfigurationProperties configuration) {
        this.restTemplate = restTemplate;
        this.configuration = configuration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#
     * authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        String urlFormat = configuration.getEngine().getUrlFormat();
        AuthenticateRequestMessage authenticateRequestMessage = new AuthenticateRequestMessage(name, password);
        try {
            UserMessage userMessage = restTemplate.postForObject(urlFormat, authenticateRequestMessage, UserMessage.class, "authenticate");
            Collection<? extends GrantedAuthority> authorities = getAuthorities(userMessage);
            User user = new User(userMessage.getUserId(), userMessage.getUserName(), userMessage.getFirstName(), userMessage.getLastName(),
                    userMessage.getEmail());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password, authorities);
            token.setDetails(Base64.getEncoder().encodeToString(String.format("%s:%s", name, password).getBytes()));
            return token;
        } catch (RestClientException e) {
            throw new InternalAuthenticationServiceException("unable to contact engine", e);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserMessage userMessage) {
        Set<SimpleGrantedAuthority> answer = new HashSet<>();
        for (GroupMessage groupMessage : userMessage.getGroups()) {
            answer.add(new SimpleGrantedAuthority(groupMessage.getType()));
        }
        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.authentication.AuthenticationProvider#
     * supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
