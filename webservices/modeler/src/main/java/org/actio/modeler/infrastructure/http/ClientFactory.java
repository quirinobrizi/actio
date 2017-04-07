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
package org.actio.modeler.infrastructure.http;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ClientFactory {

    private Map<String, RestTemplate> clients = new HashMap<>();

    public RestTemplate newClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String token = authentication.getDetails().toString();
            if (clients.containsKey(token)) {
                return clients.get(token);
            } else {
                HttpClient httpClient = createHttpClient(token);
                RestTemplate client = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
                clients.put(token, client);
                return client;
            }
        }
        throw new IllegalStateException("is authentication valid?");
    }

    private HttpClient createHttpClient(String token) {
        String[] parts = new String(Base64.getDecoder().decode(token)).split(":");
        BasicCredentialsProvider basicProvider = new BasicCredentialsProvider();
        basicProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthSchemes.BASIC),
                new UsernamePasswordCredentials(parts[0], parts[1]));
        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory()).build();
        return HttpClientBuilder.create().setDefaultAuthSchemeRegistry(authSchemeRegistry).setDefaultCredentialsProvider(basicProvider)
                .build();
    }
}
