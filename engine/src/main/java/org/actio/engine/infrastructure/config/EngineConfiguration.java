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

import org.actio.engine.infrastructure.config.EngineConfigurationProperties.Authentication;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Actio engine entrypoint
 * 
 * @author quirino.brizi
 *
 */
@EnableAsync
@Configuration
@ComponentScan(basePackages = { "org.actio.engine.interfaces", "org.actio.engine.infrastructure.config" })
@EnableEurekaClient
@EnableAutoConfiguration
public class EngineConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(EngineConfiguration.class, args);
	}

	@Bean
	InitializingBean usersAndGroupsInitializer(final IdentityService identityService,
			final EngineConfigurationProperties engineConfigurationProperties) {

		return new InitializingBean() {
			@Override
			public void afterPropertiesSet() throws Exception {
				Authentication authentication = engineConfigurationProperties.getAuthentication();
				Group group = identityService.newGroup("administrators");
				group.setName("administrators");
				group.setType("security-role");
				identityService.saveGroup(group);

				User user = identityService.newUser(authentication.getUsername());
				user.setPassword(authentication.getPassword());
				identityService.saveUser(user);

				identityService.createMembership(user.getId(), group.getId());
			}
		};
	}

}
