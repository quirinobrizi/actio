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

import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchResult;

import org.actio.commons.authentication.exception.OperationNotSupportedExcepion;
import org.actio.commons.authentication.ldap.translator.GroupTranslator;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

/**
 * LDAP Group management service
 * 
 * @author quirino.brizi
 *
 */
public class LdapGroupManagerService extends AbstractManager implements GroupIdentityManager {

    public static LdapGroupManagerService newInstance(LdapTemplate ldapTemplate, GroupTranslator GroupTranslator) {
        return new LdapGroupManagerService(ldapTemplate, GroupTranslator);
    }

    private LdapTemplate ldapTemplate;
    private GroupTranslator groupTranslator;

    protected LdapGroupManagerService(LdapTemplate ldapTemplate, GroupTranslator groupTranslator) {
        this.ldapTemplate = ldapTemplate;
        this.groupTranslator = groupTranslator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * createNewGroup(java.lang.String)
     */
    @Override
    public Group createNewGroup(String GroupId) {
        throw OperationNotSupportedExcepion.newInstance("cannot create new Group on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * insertGroup(org.activiti.engine.identity.Group)
     */
    @Override
    public void insertGroup(Group Group) {
        throw OperationNotSupportedExcepion.newInstance("cannot insert new Group on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * updateGroup(org.activiti.engine.identity.Group)
     */
    @Override
    public void updateGroup(Group updatedGroup) {
        throw OperationNotSupportedExcepion.newInstance("cannot update new Group on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * deleteGroup(java.lang.String)
     */
    @Override
    public void deleteGroup(String GroupId) {
        throw OperationNotSupportedExcepion.newInstance("cannot delete new Group on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * findGroupsByUser(java.lang.String)
     */
    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<SearchResult> searchResults = ldapTemplate.findGroupByUserId(userId);
        return groupTranslator.translate(searchResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * findGroupByQueryCriteria(org.activiti.engine.impl.GroupQueryImpl,
     * org.activiti.engine.impl.Page)
     */
    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        String id = query.getId();
        String fullNameLike = query.getNameLike();
        if (null != id) {
            return findGroupsByUser(id);
        } else if (null != fullNameLike) {
            List<SearchResult> searchResults = ldapTemplate.findGroupByName(fullNameLike.replaceAll("%", ""));
            return groupTranslator.translate(searchResults);
        } else {
            throw OperationNotSupportedExcepion.newInstance("only id and full name like are supported");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * findGroupCountByQueryCriteria(org.activiti.engine.impl.GroupQueryImpl)
     */
    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        return findGroupByQueryCriteria(query, null).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * createNewGroupQuery()
     */
    @Override
    public GroupQuery createNewGroupQuery() {
        return new GroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * findGroupsByNativeQuery(java.util.Map, int, int)
     */
    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw OperationNotSupportedExcepion.newInstance("cannot nativelly query Groups on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * findGroupCountByNativeQuery(java.util.Map)
     */
    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw OperationNotSupportedExcepion.newInstance("cannot natvelly query Groups on LDAP server");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.activiti.engine.impl.persistence.entity.GroupIdentityManager#
     * isNewGroup (org.activiti.engine.identity.Group)
     */
    @Override
    public boolean isNewGroup(Group Group) {
        return false;
    }

}
