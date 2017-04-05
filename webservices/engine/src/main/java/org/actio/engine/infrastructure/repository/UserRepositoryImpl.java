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
package org.actio.engine.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import org.actio.engine.domain.model.authentication.User;
import org.actio.engine.domain.repository.UserRepository;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private IdentityService identityService;

    @Override
    public User find(String username, String password) {
        if (identityService.checkPassword(username, password)) {
            org.activiti.engine.identity.User user = identityService.createUserQuery().userId(username).singleResult();
            List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
            return new User(user.getId(), username, user.getFirstName(), user.getLastName(), user.getEmail(), buildGroupsFrom(groups));
        }
        return null;
    }

    private List<org.actio.engine.domain.model.authentication.Group> buildGroupsFrom(List<Group> groups) {
        List<org.actio.engine.domain.model.authentication.Group> answer = new ArrayList<>();
        for (Group group : groups) {
            answer.add(org.actio.engine.domain.model.authentication.Group.newInstance(group.getName(), group.getType()));
        }
        return answer;
    }
}
