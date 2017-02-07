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
package org.actio.modeler.domain.model;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class Metrics {

	@JsonProperty("completedActivities")
	private Integer completedActivities;
	@JsonProperty("processDefinitionCount")
	private Integer processDefinitionCount;
	@JsonProperty("cachedProcessDefinitionCount")
	private Integer cachedProcessDefinitionCount;
	@JsonProperty("processesMetrics")
	private Set<ProcessMetrics> processesMetrics;

	public Metrics(Integer completedActivities, Integer processDefinitionCount, Integer cachedProcessDefinitionCount,
			Set<ProcessMetrics> processesMetrics) {
		this.completedActivities = completedActivities;
		this.processDefinitionCount = processDefinitionCount;
		this.cachedProcessDefinitionCount = cachedProcessDefinitionCount;
		this.processesMetrics = processesMetrics;
	}

	@JsonIgnore
	public Integer getCompletedActivities() {
		return completedActivities;
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
	public Set<ProcessMetrics> getProcessesMetrics() {
		return processesMetrics;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("completedActivities", completedActivities)
				.append("processDefinitionCount", processDefinitionCount)
				.append("cachedProcessDefinitionCount", cachedProcessDefinitionCount)
				.append("processesMetrics", processesMetrics);
		return builder.toString();
	}
}
