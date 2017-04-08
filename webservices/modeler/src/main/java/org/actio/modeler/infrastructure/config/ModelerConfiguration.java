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
package org.actio.modeler.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * Actio modeler entrypoint
 * 
 * @author quirino.brizi
 *
 */
@EnableAsync
@Configuration
@EnableEurekaClient
@EnableAutoConfiguration
@EnableConfigurationProperties(value = { ModelerConfigurationProperties.class })
@ComponentScan({ "org.actio.modeler.interfaces", "org.actio.modeler.app", "org.actio.modeler.infrastructure.repository",
        "org.actio.modeler.infrastructure.http", "org.actio.modeler.infrastructure.exception" })
@Import({ HttpSessionConfig.class, SecurityConfig.class, WebMvcConfiguration.class })
public class ModelerConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ModelerConfiguration.class, args);
    }

    @Bean
    RestTemplate restTemplate(ModelerConfigurationProperties modelerConfiguration) {
        return new RestTemplate();
    }

}
