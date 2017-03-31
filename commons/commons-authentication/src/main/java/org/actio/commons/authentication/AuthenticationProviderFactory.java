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
package org.actio.commons.authentication;

import java.util.HashMap;
import java.util.Map;

import org.actio.commons.authentication.exception.AuthenticationProviderNotFoundException;

/**
 * @author quirino.brizi
 *
 */
public class AuthenticationProviderFactory {

    private Map<String, AuthenticationProvider> authenticationProviders = new HashMap<>();

    public AuthenticationProvider getProviderFor(String authenticationType) {
        if (authenticationProviders.containsKey(authenticationType)) {
            return authenticationProviders.get(authenticationType);
        }
        throw AuthenticationProviderNotFoundException.newInstance(authenticationType);
    }

    public void register(AuthenticationProvider authenticationProvider) {
        this.authenticationProviders.put(authenticationProvider.authenticationType(), authenticationProvider);
    }
}
