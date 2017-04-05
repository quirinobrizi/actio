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
package org.actio.engine.interfaces;

import org.actio.commons.message.Message;
import org.actio.commons.message.identity.AuthenticateRequestMessage;
import org.actio.engine.app.AuthenticationService;
import org.actio.engine.domain.model.authentication.User;
import org.actio.engine.interfaces.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
public class AuthentictionInterface {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserTranslator userTranslator;

    @RequestMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message authenticate(@RequestBody AuthenticateRequestMessage message) {
        User user = authenticationService.authenticate(message.getUsername(), message.getPassword());
        return userTranslator.translate(user);
    }
}
