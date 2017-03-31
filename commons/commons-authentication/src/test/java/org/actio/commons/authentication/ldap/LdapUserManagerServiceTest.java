package org.actio.commons.authentication.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.naming.directory.SearchResult;

import org.actio.commons.authentication.exception.OperationNotSupportedExcepion;
import org.actio.commons.authentication.ldap.translator.UserTranslator;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class LdapUserManagerServiceTest {

    @InjectMocks
    private LdapUserManagerService testObj;

    @Mock
    private LdapTemplate ldapTemplate;
    @Mock
    private UserTranslator userTranslator;

    @Mock
    private User user;
    @Mock
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Mock
    private CommandExecutor commandExecutor;
    @Mock
    private SearchResult searchResult;
    @Mock
    private UserQueryImpl query;
    @Mock
    private Page page;

    @Before
    public void before() {
        testObj = LdapUserManagerService.newInstance(ldapTemplate, userTranslator);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testCreateNewUser() {
        testObj.createNewUser("aaa");
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testInsertUser() {
        testObj.insertUser(user);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testUpdateUser() {
        testObj.updateUser(user);
    }

    @Test
    public void testFindUserById() {
        String identifier = "id";
        when(ldapTemplate.findUserById(identifier)).thenReturn(searchResult);
        when(userTranslator.translate(searchResult)).thenReturn(user);
        // act
        User actual = testObj.findUserById(identifier);
        // assert
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testDeleteUser() {
        testObj.deleteUser("aaa");
    }

    @Test
    public void testFindUserByQueryCriteria_identifier() {
        when(query.getId()).thenReturn("id");
        when(query.getFullNameLike()).thenReturn(null);
        when(ldapTemplate.findUserById("id")).thenReturn(searchResult);
        when(userTranslator.translate(searchResult)).thenReturn(user);
        // act
        List<User> actual = testObj.findUserByQueryCriteria(query, page);
        // assert
        assertEquals(Arrays.asList(user), actual);
    }

    @Test
    public void testFindUserByQueryCriteria_fullname() {
        when(query.getId()).thenReturn(null);
        when(query.getFullNameLike()).thenReturn("fullName");
        List<SearchResult> searchResults = Arrays.asList(searchResult);
        when(ldapTemplate.findUserByName("fullName")).thenReturn(searchResults);
        List<User> users = Arrays.asList(user);
        when(userTranslator.translate(searchResults)).thenReturn(users);
        // act
        List<User> actual = testObj.findUserByQueryCriteria(query, page);
        // assert
        assertEquals(users, actual);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindUserByQueryCriteria_not_supported() {
        when(query.getId()).thenReturn(null);
        when(query.getFullNameLike()).thenReturn(null);
        // act
        testObj.findUserByQueryCriteria(query, page);
    }

    @Test
    public void testFindUserCountByQueryCriteria() {
        when(query.getId()).thenReturn(null);
        when(query.getFullNameLike()).thenReturn("fullName");
        List<SearchResult> searchResults = Arrays.asList(searchResult);
        when(ldapTemplate.findUserByName("fullName")).thenReturn(searchResults);
        List<User> users = Arrays.asList(user);
        when(userTranslator.translate(searchResults)).thenReturn(users);
        // act
        long actual = testObj.findUserCountByQueryCriteria(query);
        // assert
        assertEquals(1, actual);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindGroupsByUser() {
        testObj.findGroupsByUser("ddd");
    }

    @Test
    public void testCreateNewUserQuery() {
        PowerMockito.mockStatic(Context.class);
        when(Context.getProcessEngineConfiguration()).thenReturn(processEngineConfiguration);
        when(processEngineConfiguration.getCommandExecutor()).thenReturn(commandExecutor);
        UserQuery actual = testObj.createNewUserQuery();
        // assert
        assertNotNull(actual);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindUserInfoByUserIdAndKey() {
        // act
        testObj.findUserInfoByUserIdAndKey("userId", "key");
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindUserInfoKeysByUserIdAndType() {
        // act
        testObj.findUserInfoKeysByUserIdAndType("userId", "type");
    }

    @Test
    public void testCheckPassword_blank_password() {
        // act
        assertFalse(testObj.checkPassword("userId", ""));
    }

    @Test
    public void testCheckPassword_valid_password() {
        when(ldapTemplate.findUserById("userId")).thenReturn(searchResult);
        when(searchResult.getNameInNamespace()).thenReturn("userDn");
        when(ldapTemplate.tryBind("userDn", "password")).thenReturn(true);
        // act
        assertTrue(testObj.checkPassword("userId", "password"));
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindPotentialStarterUsers() {
        testObj.findPotentialStarterUsers("proc");
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindUsersByNativeQuery() {
        testObj.findUsersByNativeQuery(null, 0, 10);
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testFindUserCountByNativeQuery() {
        testObj.findUserCountByNativeQuery(null);
    }

    @Test
    public void testIsNewUser() {
        assertFalse(testObj.isNewUser(user));
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testGetUserPicture() {
        // act
        testObj.getUserPicture("userId");
    }

    @Test(expected = OperationNotSupportedExcepion.class)
    public void testSetUserPicture() {
        testObj.setUserPicture("user", null);
    }

}
