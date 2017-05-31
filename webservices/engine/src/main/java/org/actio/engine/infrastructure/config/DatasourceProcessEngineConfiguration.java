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
package org.actio.engine.infrastructure.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.actio.commons.authentication.AuthenticationConfigurer;
import org.actio.commons.authentication.AuthenticationProvider;
import org.actio.commons.authentication.AuthenticationProviderFactory;
import org.actio.commons.authentication.db.DbAuthenticationProvider;
import org.actio.commons.authentication.ldap.LdapAuthenticationProvider;
import org.actio.engine.infrastructure.listener.ErrorEventListener;
import org.actio.engine.infrastructure.parser.DefaultBpmnParseFactory;
import org.actio.engine.infrastructure.parser.behavior.DefaultActivityBehaviorFactory;
import org.actio.engine.interfaces.validator.ValidatorFactory;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.cfg.SpringBeanFactoryProxyMap;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringCallerRunsRejectedJobsHandler;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.SpringRejectedJobsHandler;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.DataSourceProcessEngineAutoConfiguration;
import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration.JpaConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

/**
 * @author quirino.brizi
 *
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({ DataSourceProcessEngineAutoConfiguration.class, JpaConfiguration.class })
public class DatasourceProcessEngineConfiguration { // NOSONAR

    @Configuration
    @ConditionalOnClass(name = "javax.persistence.EntityManagerFactory")
    @EnableConfigurationProperties(ActivitiProperties.class)
    public static class JpaConfiguration {
        private static final Logger logger = LoggerFactory.getLogger(JpaConfiguration.class);

        protected ActivitiProperties activitiProperties;

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private ResourcePatternResolver resourceLoader;

        @Autowired(required = false)
        private ProcessEngineConfigurationConfigurer processEngineConfigurationConfigurer;

        @Autowired
        private ErrorEventListener errorEventListener;

        @Bean
        public SpringAsyncExecutor springAsyncExecutor(TaskExecutor taskExecutor) {
            return new SpringAsyncExecutor(taskExecutor, springRejectedJobsHandler());
        }

        @Bean
        public SpringRejectedJobsHandler springRejectedJobsHandler() {
            return new SpringCallerRunsRejectedJobsHandler();
        }

        @Autowired
        private DefaultBpmnParseFactory bpmnParseFactory;

        @Bean
        @ConditionalOnMissingBean
        public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
            return new JpaTransactionManager(emf);
        }

        @Bean
        @ConditionalOnMissingBean
        public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource,
                EntityManagerFactory entityManagerFactory, PlatformTransactionManager transactionManager,
                SpringAsyncExecutor springAsyncExecutor) throws IOException {

            ProcessEngineConfiguration engine = new ProcessEngineConfiguration();

            List<Resource> procDefResources = this.discoverProcessDefinitionResources(this.resourceLoader,
                    this.activitiProperties.getProcessDefinitionLocationPrefix(),
                    this.activitiProperties.getProcessDefinitionLocationSuffixes(), this.activitiProperties.isCheckProcessDefinitions());
            Resource[] processDefinitions = procDefResources.toArray(new Resource[procDefResources.size()]);
            if (processDefinitions != null && processDefinitions.length > 0) {
                engine.setDeploymentResources(processDefinitions);
            }
            engine.setDataSource(dataSource);
            engine.setTransactionManager(transactionManager);

            if (null != springAsyncExecutor) {
                engine.setAsyncExecutorEnabled(true);
                engine.setAsyncExecutor(springAsyncExecutor);
            }

            engine.setDeploymentName(defaultText(activitiProperties.getDeploymentName(), engine.getDeploymentName()));
            engine.setDatabaseSchema(defaultText(activitiProperties.getDatabaseSchema(), engine.getDatabaseSchema()));
            engine.setDatabaseSchemaUpdate(defaultText(activitiProperties.getDatabaseSchemaUpdate(), engine.getDatabaseSchemaUpdate()));
            engine.setDbIdentityUsed(activitiProperties.isDbIdentityUsed());
            engine.setDbHistoryUsed(activitiProperties.isDbHistoryUsed());
            // engine.setEnableDatabaseEventLogging(true);

            engine.setJobExecutorActivate(activitiProperties.isJobExecutorActivate());
            engine.setAsyncExecutorEnabled(activitiProperties.isAsyncExecutorEnabled());
            engine.setAsyncExecutorActivate(activitiProperties.isAsyncExecutorActivate());

            engine.setMailServerHost(activitiProperties.getMailServerHost());
            engine.setMailServerPort(activitiProperties.getMailServerPort());
            engine.setMailServerUsername(activitiProperties.getMailServerUserName());
            engine.setMailServerPassword(activitiProperties.getMailServerPassword());
            engine.setMailServerDefaultFrom(activitiProperties.getMailServerDefaultFrom());
            engine.setMailServerUseSSL(activitiProperties.isMailServerUseSsl());
            engine.setMailServerUseTLS(activitiProperties.isMailServerUseTls());

            engine.setHistoryLevel(activitiProperties.getHistoryLevel());

            if (activitiProperties.getCustomMybatisMappers() != null) {
                engine.setCustomMybatisMappers(getCustomMybatisMapperClasses(activitiProperties.getCustomMybatisMappers()));
            }

            if (activitiProperties.getCustomMybatisXMLMappers() != null) {
                engine.setCustomMybatisXMLMappers(new HashSet<String>(activitiProperties.getCustomMybatisXMLMappers()));
            }

            if (processEngineConfigurationConfigurer != null) {
                processEngineConfigurationConfigurer.configure(engine);
            }

            engine.setJpaEntityManagerFactory(entityManagerFactory);
            engine.setTransactionManager(transactionManager);
            engine.setJpaHandleTransaction(false);
            engine.setJpaCloseEntityManager(false);

            BpmnParser bpmnParser = new BpmnParser();
            engine.setBpmnParseFactory(bpmnParseFactory);
            bpmnParser.setBpmnParseFactory(bpmnParseFactory);
            engine.setBpmnParser(bpmnParser);
            engine.setProcessValidator(createDefaultProcessValidator());
            engine.setActivityBehaviorFactory(new DefaultActivityBehaviorFactory());
            engine.setExpressionManager(new ExpressionManagerConfig(applicationContext, new SpringBeanFactoryProxyMap(applicationContext)));
            engine.setEventListeners(Arrays.asList(errorEventListener));

            return engine;
        }

        private ProcessValidator createDefaultProcessValidator() {
            ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
            processValidator.addValidatorSet(new ValidatorFactory().createActivitiExecutableProcessValidatorSet());
            return processValidator;
        }

        private Set<Class<?>> getCustomMybatisMapperClasses(List<String> customMyBatisMappers) {
            Set<Class<?>> mybatisMappers = new HashSet<Class<?>>();
            for (String customMybatisMapperClassName : customMyBatisMappers) {
                try {
                    Class<?> customMybatisClass = Class.forName(customMybatisMapperClassName);
                    mybatisMappers.add(customMybatisClass);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Class " + customMybatisMapperClassName + " has not been found.", e);
                }
            }
            return mybatisMappers;
        }

        private String defaultText(String deploymentName, String deploymentName1) {
            if (StringUtils.hasText(deploymentName))
                return deploymentName;
            return deploymentName1;
        }

        @Autowired
        protected void setActivitiProperties(ActivitiProperties activitiProperties) {
            this.activitiProperties = activitiProperties;
        }

        protected ActivitiProperties getActivitiProperties() {
            return this.activitiProperties;
        }

        @Bean
        InitializingBean usersAndGroupsInitializer(final ProcessEngineConfiguration engine,
                final EngineConfigurationProperties engineConfigurationProperties,
                final AuthenticationProviderFactory authenticationProviderFactory) {

            return new InitializingBean() {
                @Override
                public void afterPropertiesSet() throws Exception {
                    AuthenticationProvider authenticationProvider = authenticationProviderFactory
                            .getProviderFor(defaultText(engineConfigurationProperties.getAuthentication().getType(), "db"));
                    configureAuthentication(engine, authenticationProvider, engineConfigurationProperties);

                    AuthenticationConfigurer authenticationConfigurer = authenticationProvider.authenticationConfigurer();
                    if (null != authenticationConfigurer) {
                        authenticationConfigurer.configure(engine.getIdentityService(), engineConfigurationProperties.getAuthentication());
                    }
                }
            };
        }

        @Bean
        AuthenticationProviderFactory authenticationProviderFactory() {
            AuthenticationProviderFactory answer = new AuthenticationProviderFactory();
            answer.register(new LdapAuthenticationProvider(null));
            answer.register(new DbAuthenticationProvider());
            return answer;
        }

        @Bean
        ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration configuration) throws Exception {
            ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
            processEngineFactoryBean.setProcessEngineConfiguration(configuration);
            return processEngineFactoryBean;
        }

        @Bean
        @ConditionalOnMissingBean
        RuntimeService runtimeServiceBean(ProcessEngine processEngine) {
            return processEngine.getRuntimeService();
        }

        @Bean
        @ConditionalOnMissingBean
        RepositoryService repositoryServiceBean(ProcessEngine processEngine) {
            return processEngine.getRepositoryService();
        }

        @Bean
        @ConditionalOnMissingBean
        TaskService taskServiceBean(ProcessEngine processEngine) {
            return processEngine.getTaskService();
        }

        @Bean
        @ConditionalOnMissingBean
        HistoryService historyServiceBean(ProcessEngine processEngine) {
            return processEngine.getHistoryService();
        }

        @Bean
        @ConditionalOnMissingBean
        ManagementService managementServiceBeanBean(ProcessEngine processEngine) {
            return processEngine.getManagementService();
        }

        @Bean
        @ConditionalOnMissingBean
        FormService formServiceBean(ProcessEngine processEngine) {
            return processEngine.getFormService();
        }

        @Bean
        @ConditionalOnMissingBean
        IdentityService identityServiceBean(ProcessEngine processEngine) {
            return processEngine.getIdentityService();
        }

        @Bean
        @ConditionalOnMissingBean
        TaskExecutor taskExecutor() {
            return new SimpleAsyncTaskExecutor();
        }

        List<Resource> discoverProcessDefinitionResources(ResourcePatternResolver applicationContext, String prefix, List<String> suffixes,
                boolean checkPDs) throws IOException {
            if (checkPDs) {

                List<Resource> result = new ArrayList<>();
                for (String suffix : suffixes) {
                    String path = prefix + suffix;
                    Resource[] resources = applicationContext.getResources(path);
                    if (resources != null && resources.length > 0) {
                        for (Resource resource : resources) {
                            result.add(resource);
                        }
                    }
                }

                if (result.isEmpty()) {
                    logger.info("No process definitions were found for autodeployment");
                }

                return result;
            }
            return new ArrayList<>();
        }

        private void configureAuthentication(ProcessEngineConfiguration engine, AuthenticationProvider authenticationProvider,
                EngineConfigurationProperties engineConfigurationProperties) {
            SessionFactory userManagerFactory = authenticationProvider.getUserManagerFactory();
            SessionFactory groupManagerFactory = authenticationProvider.getGroupManagerFactory();
            SessionFactory membershiprManagerFactory = authenticationProvider.getMembershiprManagerFactory();

            Map<Class<?>, SessionFactory> sessionFactories = engine.getSessionFactories();
            sessionFactories.put(userManagerFactory.getSessionType(), userManagerFactory);
            sessionFactories.put(groupManagerFactory.getSessionType(), groupManagerFactory);
            if (!"ldap".equals(authenticationProvider.authenticationType())) {
                sessionFactories.put(membershiprManagerFactory.getSessionType(), membershiprManagerFactory);
            }
        }
    }
}
