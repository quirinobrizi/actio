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
package org.actio.engine.domain.model.bpmn.process;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A runnable instance of a process
 * 
 * @author quirino.brizi
 *
 */
public class Instance {

    private InstanceId instanceId;
    private final Map<String, Object> variables;
    private InstanceState processState;
    private Date startDate;
    private Date endDate;

    public Instance(InstanceId instanceId) {
        this.instanceId = instanceId;
        this.variables = new HashMap<>();
    }

    public String getInstanceId() {
        return instanceId.toString();
    }

    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    public void addVariable(String key, Object value) {
        Validate.notNull(key, "variable identifier must not be null");
        this.variables.put(key, value);
    }

    public void addVariables(Map<String, Object> variables) {
        Validate.notNull(variables, "variables map must not be null");
        this.variables.putAll(variables);
    }

    public void setVariables(Map<String, Object> variables) {
        Validate.notNull(variables, "variables map must not be null");
        this.variables.clear();
        this.variables.putAll(variables);
    }

    public InstanceState getProcessState() {
        return processState;
    }

    public void setInstanceState(InstanceState processState) {
        this.processState = processState;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Instance other = (Instance) obj;
        return this.instanceId.equals(other.instanceId);
    }

    @Override
    public int hashCode() {
        return this.instanceId.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("instanceId", instanceId).append("variables", variables).append("processState", processState);
        return builder.toString();
    }

}
