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
package org.actio.commons.message.bpmn;

import java.util.Collection;
import java.util.Map;

import org.actio.commons.message.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class InstanceMessage implements Message {

    private static final long serialVersionUID = -7972822999099135146L;

    @JsonProperty("instanceId")
    private String instanceId;
    @JsonProperty("variables")
    private Map<String, Object> variables;
    @JsonProperty("instanceState")
    private String instanceState;
    @JsonProperty("startDate")
    private Long startDate;
    @JsonProperty("endDate")
    private Long endDate;
    @JsonProperty("jobs")
    private Collection<JobMessage> jobs;
    @JsonProperty("tasks")
    private Collection<TaskMessage> tasks;

    @JsonCreator
    public InstanceMessage(@JsonProperty("instanceId") String instanceId, @JsonProperty("variables") Map<String, Object> variables,
            @JsonProperty("instanceState") String instanceState, @JsonProperty("startDate") Long startDate,
            @JsonProperty("endDate") Long endDate, @JsonProperty("jobs") Collection<JobMessage> jobs,
            @JsonProperty("tasks") Collection<TaskMessage> tasks) {
        this.instanceId = instanceId;
        this.variables = variables;
        this.instanceState = instanceState;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobs = jobs;
        this.tasks = tasks;
    }

}
