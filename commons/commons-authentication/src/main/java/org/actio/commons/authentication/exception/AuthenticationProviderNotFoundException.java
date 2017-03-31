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
package org.actio.commons.authentication.exception;

import org.activiti.engine.ActivitiException;

/**
 * Exception thrown on operation not supported
 * 
 * @author quirino.brizi
 *
 */
public class AuthenticationProviderNotFoundException extends ActivitiException {

    private static final long serialVersionUID = 6578844795253090617L;

    public static AuthenticationProviderNotFoundException newInstance(String message) {
        return new AuthenticationProviderNotFoundException(message);
    }

    private AuthenticationProviderNotFoundException(String message) {
        super(String.format("authentication provider for type %s not found", message));
    }

}
