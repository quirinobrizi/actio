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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * E. A runnable instance of a BPMN.
 * 
 * @author quirino.brizi
 *
 */
public class Process {

    public static Process newInstance(ProcessId processId) {
        return new Process(processId);
    }

    /**
     * Unique identifier for the process
     */
    private final ProcessId processId;
    private final List<Instance> instances;

    private Process(ProcessId processId) {
        Validate.notNull(processId, "process identifier must not be null");
        this.processId = processId;
        this.instances = new ArrayList<>();
    }

    public String getProcessId() {
        return processId.toString();
    }

    public void addInstance(Instance instance) {
        Validate.notNull(instance, "instance cannot be null");
        this.instances.add(instance);
    }

    public List<Instance> getInstances() {
        return Collections.unmodifiableList(instances);
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
        Process other = (Process) obj;
        return this.processId.equals(other.processId);
    }

    @Override
    public int hashCode() {
        return this.processId.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("processId", processId).append("instances", instances);
        return builder.toString();
    }

}
