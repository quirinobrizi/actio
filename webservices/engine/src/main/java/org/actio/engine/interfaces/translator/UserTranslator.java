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
package org.actio.engine.interfaces.translator;

import java.util.List;

import org.actio.commons.message.Message;
import org.actio.commons.message.identity.GroupMessage;
import org.actio.commons.message.identity.UserMessage;
import org.actio.engine.domain.model.authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class UserTranslator {

    @Autowired
    private GroupTranslator groupTranslator;

    public Message translate(User user) {
        List<GroupMessage> groups = groupTranslator.translate(user.getGroups());
        return new UserMessage(null, user.getUserId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(),
                groups);
    }

}
