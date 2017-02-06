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
package org.actio.modeler.controller;

import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
@RestController
@RequestMapping(path = "/processes", produces = MediaType.APPLICATION_JSON_VALUE)
public class Processes {

	@Autowired
	private ModelerConfigurationProperties configuration;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/metrics")
	public String getMetrics() {
		return restTemplate.getForObject(configuration.getEngine().getUrlFormat(), String.class, "activiti");
	}
}
