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

import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties.Engine;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
@ComponentScan({ "org.actio.modeler.interfaces", "org.actio.modeler.app",
		"org.actio.modeler.infrastructure.repository" })
public class ModelerConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(ModelerConfiguration.class, args);
	}

	@Bean
	RestTemplate restTemplate(ModelerConfigurationProperties modelerConfiguration) {
		HttpClient httpClient = createHttpClient(modelerConfiguration);
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	private HttpClient createHttpClient(ModelerConfigurationProperties modelerConfiguration) {
		Engine engine = modelerConfiguration.getEngine();
		BasicCredentialsProvider basicProvider = new BasicCredentialsProvider();
		basicProvider.setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthSchemes.BASIC),
				new UsernamePasswordCredentials(engine.getUsername(), engine.getPassword()));
		Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
				.register(AuthSchemes.BASIC, new BasicSchemeFactory()).build();
		return HttpClientBuilder.create().setDefaultAuthSchemeRegistry(authSchemeRegistry)
				.setDefaultCredentialsProvider(basicProvider).build();
	}
}
