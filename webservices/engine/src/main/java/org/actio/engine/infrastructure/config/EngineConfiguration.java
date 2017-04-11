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

import org.activiti.rest.security.BasicAuthenticationProvider;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Actio engine entrypoint
 * 
 * @author quirino.brizi
 *
 */
@EnableAsync
@Configuration
@ComponentScan(basePackages = { "org.actio.engine.interfaces", "org.actio.engine.app", "org.actio.engine.infrastructure.config",
        "org.actio.engine.infrastructure.bpmn", "org.actio.engine.infrastructure.repository", "org.actio.engine.interfaces.translator" })
@EnableEurekaClient
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class EngineConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(EngineConfiguration.class, args);
    }

    @Configuration
    @ConditionalOnClass(name = { "org.activiti.rest.service.api.RestUrls", "org.springframework.web.servlet.DispatcherServlet" })
    @EnableWebSecurity
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Bean
        public AuthenticationProvider authenticationProvider() {
            return new BasicAuthenticationProvider();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authenticationProvider(authenticationProvider()).csrf().disable().authorizeRequests().antMatchers("/authenticate")
                    .permitAll().anyRequest().authenticated().and().httpBasic();
        }
    }
}
