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
package org.actio.commons.message.metrics;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricsMessage {

	@JsonProperty("completedTaskCountToday")
	private Integer completedTaskCountToday;
	@JsonProperty("deployedProcessDefinitions")
	private List<String> deployedProcessDefinitions;
	@JsonProperty("processDefinitionCount")
	private Integer processDefinitionCount;
	@JsonProperty("cachedProcessDefinitionCount")
	private Integer cachedProcessDefinitionCount;
	@JsonProperty("runningProcessInstanceCount")
	private Map<String, Integer> runningProcessInstanceCount;
	@JsonProperty("completedTaskCount")
	private Integer completedTaskCount;
	@JsonProperty("completedActivities")
	private Integer completedActivities;
	@JsonProperty("completedProcessInstanceCount")
	private Map<String, Integer> completedProcessInstanceCount;
	@JsonProperty("openTaskCount")
	private Integer openTaskCount;

	@JsonIgnore
	public Integer getCompletedTaskCountToday() {
		return completedTaskCountToday;
	}

	@JsonIgnore
	public List<String> getDeployedProcessDefinitions() {
		return deployedProcessDefinitions;
	}

	@JsonIgnore
	public Integer getProcessDefinitionCount() {
		return processDefinitionCount;
	}

	@JsonIgnore
	public Integer getCachedProcessDefinitionCount() {
		return cachedProcessDefinitionCount;
	}

	@JsonIgnore
	public Map<String, Integer> getRunningProcessInstanceCount() {
		return runningProcessInstanceCount;
	}

	@JsonIgnore
	public Integer getCompletedTaskCount() {
		return completedTaskCount;
	}

	@JsonIgnore
	public Integer getCompletedActivities() {
		return completedActivities;
	}

	@JsonIgnore
	public Map<String, Integer> getCompletedProcessInstanceCount() {
		return completedProcessInstanceCount;
	}

	@JsonIgnore
	public Integer getOpenTaskCount() {
		return openTaskCount;
	}

}
