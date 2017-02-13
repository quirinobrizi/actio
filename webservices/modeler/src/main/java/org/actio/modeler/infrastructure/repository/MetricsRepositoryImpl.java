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
package org.actio.modeler.infrastructure.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.actio.commons.message.metrics.MetricsMessage;
import org.actio.modeler.domain.model.Metrics;
import org.actio.modeler.domain.model.ProcessMetrics;
import org.actio.modeler.domain.repository.MetricsRepository;
import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class MetricsRepositoryImpl implements MetricsRepository {

	private final static Pattern KEY_VERSION_PATTERN = Pattern.compile("(.*) \\((v[0-9]+)\\)");
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsRepositoryImpl.class);

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ModelerConfigurationProperties configuration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.actio.modeler.domain.repository.MetricsRepository#get()
	 */
	@Override
	public Metrics get() {
		MetricsMessage metrics = restTemplate.getForObject(configuration.getEngine().getUrlFormat(),
				MetricsMessage.class, "activiti");
		return new Metrics(metrics.getCompletedActivities(), metrics.getProcessDefinitionCount(),
				metrics.getCachedProcessDefinitionCount(), extractProcessesMetrics(metrics));
	}

	private Set<ProcessMetrics> extractProcessesMetrics(MetricsMessage metrics) {
		Map<String, ProcessMetrics> processes = new HashMap<>();
		Map<String, Integer> completedProcessInstanceCount = metrics.getCompletedProcessInstanceCount();
		Map<String, Integer> runningProcessInstanceCount = metrics.getRunningProcessInstanceCount();
		for (String processDefinition : metrics.getDeployedProcessDefinitions()) {
			Matcher matcher = KEY_VERSION_PATTERN.matcher(processDefinition);
			if (matcher.matches()) {
				String key = matcher.group(1);
				String version = matcher.group(2);
				ProcessMetrics processMetrics = null;
				if (processes.containsKey(key)) {
					processMetrics = processes.get(key);
				} else {
					processMetrics = new ProcessMetrics(key);
					processes.put(key, processMetrics);
				}
				processMetrics.updateOrCreate(version, completedProcessInstanceCount.get(processDefinition),
						runningProcessInstanceCount.get(processDefinition));
			} else {
				LOGGER.info("process definition {} does not match provided regex");
				continue;
			}
		}
		return new HashSet<>(processes.values());
	}

}
