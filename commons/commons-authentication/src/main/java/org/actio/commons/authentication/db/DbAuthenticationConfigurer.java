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
package org.actio.commons.authentication.db;

import java.io.IOException;
import java.util.List;

import org.actio.commons.authentication.AuthenticationConfigurer;
import org.actio.commons.authentication.AuthenticationProperties;
import org.actio.commons.authentication.exception.UnableToLoadResourceException;
import org.activiti.engine.IdentityService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author quirino.brizi
 *
 */
public class DbAuthenticationConfigurer implements AuthenticationConfigurer {

    private ObjectMapper mapper = new ObjectMapper();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.commons.authentication.AuthenticationConfigurer#configure(org.
     * actio.commons.authentication.AuthenticationProperties)
     */
    @Override
    public void configure(IdentityService identityService, AuthenticationProperties authenticationProperties) {
        UsersAndGroups usersAndGroups = getUsersAndGroups(authenticationProperties.getIdentity().getSource());
        for (Group group : usersAndGroups.getGroups()) {
            org.activiti.engine.identity.Group newGroup = identityService.newGroup(group.getId());
            newGroup.setName(group.getName());
            newGroup.setType(group.getRole());
            identityService.saveGroup(newGroup);
        }

        for (User user : usersAndGroups.getUsers()) {
            org.activiti.engine.identity.User newUser = identityService.newUser(user.getName());
            newUser.setPassword(user.getPassword());
            identityService.saveUser(newUser);

            for (String group : user.getGroups()) {
                identityService.createMembership(newUser.getId(), group);
            }
        }
    }

    private UsersAndGroups getUsersAndGroups(String source) {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource(source);
        if (resource.exists()) {
            try {
                return mapper.readValue(resource.getInputStream(), UsersAndGroups.class);
            } catch (IOException e) {
                throw UnableToLoadResourceException.newInstance(e.getMessage());
            }
        }
        throw UnableToLoadResourceException.newInstance(String.format("resource %s not found", source));
    }

    private static class UsersAndGroups {

        @JsonProperty("groups")
        private List<Group> groups;
        @JsonProperty("users")
        private List<User> users;

        @JsonIgnore
        public List<Group> getGroups() {
            return groups;
        }

        @JsonIgnore
        public List<User> getUsers() {
            return users;
        }
    }

    private static class Group {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("role")
        private String role;

        @JsonIgnore
        public String getId() {
            return id;
        }

        @JsonIgnore
        public String getName() {
            return name;
        }

        @JsonIgnore
        public String getRole() {
            return role;
        }
    }

    private static class User {

        @JsonProperty("name")
        private String name;
        @JsonProperty("password")
        private String password;
        @JsonProperty("groups")
        private List<String> groups;

        @JsonIgnore
        public String getName() {
            return name;
        }

        @JsonIgnore
        public String getPassword() {
            return password;
        }

        @JsonIgnore
        public List<String> getGroups() {
            return groups;
        }
    }
}
