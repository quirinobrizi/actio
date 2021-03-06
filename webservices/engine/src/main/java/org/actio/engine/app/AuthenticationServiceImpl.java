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
package org.actio.engine.app;

import org.actio.engine.domain.model.authentication.User;
import org.actio.engine.domain.repository.UserRepository;
import org.actio.engine.infrastructure.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quirino.brizi
 *
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.app.AuthenticationService#authenticate(java.lang.String,
     * java.lang.String)
     */
    @Override
    public User authenticate(String username, String password) {
        User user = userRepository.find(username, password);
        if (null == user) {
            throw UnauthorizedException.newInstance(username);
        }
        return user;
    }

}
