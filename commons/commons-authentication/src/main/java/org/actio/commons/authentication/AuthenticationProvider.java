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

import org.activiti.engine.impl.interceptor.SessionFactory;

/**
 * @author quirino.brizi
 *
 */
public interface AuthenticationProvider {

    /**
     * Provide a fully configured user factory
     * 
     * @return
     */
    SessionFactory getUserManagerFactory();

    /**
     * Provide a fully configured group factory
     * 
     * @return
     */
    SessionFactory getGroupManagerFactory();

    /**
     * Provide a fully configured membership factory
     * 
     * @return
     */
    SessionFactory getMembershiprManagerFactory();

    /**
     * Provides authentication configurer, may be null if not needed
     * 
     * @return
     */
    AuthenticationConfigurer authenticationConfigurer();

    /**
     * Define the type of authentication factories are provided for.
     * 
     * @return
     */
    String authenticationType();
}
