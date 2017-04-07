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
package org.actio.modeler.interfaces;

import org.actio.commons.message.Message;
import org.actio.modeler.infrastructure.exception.UnauthorizedException;
import org.actio.modeler.infrastructure.security.model.User;
import org.actio.modeler.interfaces.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quirino.brizi
 *
 */
@RestController
public class Login {

    @Autowired
    private UserTranslator userTranslator;

    @RequestMapping(value = "/authorize")
    @ResponseStatus(code = HttpStatus.OK)
    public Message authorize() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (User.class.isAssignableFrom(principal.getClass())) {
            User user = (User) principal;
            return userTranslator.translate(user);
        }
        throw UnauthorizedException.newInstance();
    }
}
