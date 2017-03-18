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
package org.actio.commons.authentication.ldap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchResult;

import org.actio.commons.authentication.exception.OperationNotSupportedExcepion;
import org.actio.commons.authentication.ldap.translator.UserTranslator;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.apache.commons.lang3.StringUtils;

/**
 * LDAP user management service
 * 
 * @author quirino.brizi
 *
 */
public class LdapUserManagerService extends AbstractManager implements UserIdentityManager {

    public static LdapUserManagerService newInstance(LdapTemplate ldapTemplate, UserTranslator userTranslator) {
        return new LdapUserManagerService(ldapTemplate, userTranslator);
    }

    private LdapTemplate ldapTemplate;
    private UserTranslator userTranslator;

    protected LdapUserManagerService(LdapTemplate ldapTemplate, UserTranslator userTranslator) {
        this.ldapTemplate = ldapTemplate;
        this.userTranslator = userTranslator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * createNewUser(java.lang.String)
     */
    @Override
    public User createNewUser(String userId) {
        throw OperationNotSupportedExcepion.newInstance("cannot create new user on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * insertUser(org.activiti.engine.identity.User)
     */
    @Override
    public void insertUser(User user) {
        throw OperationNotSupportedExcepion.newInstance("cannot insert new user on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * updateUser(org.activiti.engine.identity.User)
     */
    @Override
    public void updateUser(User updatedUser) {
        throw OperationNotSupportedExcepion.newInstance("cannot update new user on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserById(java.lang.String)
     */
    @Override
    public User findUserById(String userId) {
        SearchResult searchResult = ldapTemplate.findById(userId);
        return userTranslator.translate(searchResult);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(String userId) {
        throw OperationNotSupportedExcepion.newInstance("cannot delete new user on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserByQueryCriteria(org.activiti.engine.impl.UserQueryImpl,
     * org.activiti.engine.impl.Page)
     */
    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        String id = query.getId();
        String fullNameLike = query.getFullNameLike();
        if (null != id) {
            return Arrays.asList(findUserById(id));
        } else if (null != fullNameLike) {
            List<SearchResult> searchResults = ldapTemplate.findByName(fullNameLike.replaceAll("%", ""));
            return userTranslator.translate(searchResults);
        } else {
            throw OperationNotSupportedExcepion.newInstance("only id and full name like are supported");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserCountByQueryCriteria(org.activiti.engine.impl.UserQueryImpl)
     */
    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        return findUserByQueryCriteria(query, null).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findGroupsByUser(java.lang.String)
     */
    @Override
    public List<Group> findGroupsByUser(String userId) {
        throw OperationNotSupportedExcepion.newInstance("cannot query groups from LDAP user manager");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * createNewUserQuery()
     */
    @Override
    public UserQuery createNewUserQuery() {
        return new UserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserInfoByUserIdAndKey(java.lang.String, java.lang.String)
     */
    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw OperationNotSupportedExcepion.newInstance("query not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserInfoKeysByUserIdAndType(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw OperationNotSupportedExcepion.newInstance("query not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * checkPassword(java.lang.String, java.lang.String)
     */
    @Override
    public Boolean checkPassword(String userId, String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        SearchResult searchResult = ldapTemplate.findById(userId);
        return ldapTemplate.tryBind(searchResult.getNameInNamespace(), password);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findPotentialStarterUsers(java.lang.String)
     */
    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        throw OperationNotSupportedExcepion.newInstance("cannot query users on LDAP server by process definition id");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUsersByNativeQuery(java.util.Map, int, int)
     */
    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw OperationNotSupportedExcepion.newInstance("cannot nativelly query users on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * findUserCountByNativeQuery(java.util.Map)
     */
    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw OperationNotSupportedExcepion.newInstance("cannot natvelly query users on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.activiti.engine.impl.persistence.entity.UserIdentityManager#isNewUser
     * (org.activiti.engine.identity.User)
     */
    @Override
    public boolean isNewUser(User user) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * getUserPicture(java.lang.String)
     */
    @Override
    public Picture getUserPicture(String userId) {
        throw OperationNotSupportedExcepion.newInstance("query not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.UserIdentityManager#
     * setUserPicture(java.lang.String, org.activiti.engine.identity.Picture)
     */
    @Override
    public void setUserPicture(String userId, Picture picture) {
        throw OperationNotSupportedExcepion.newInstance("cannot create/update user on LDAP server");
    }

}
