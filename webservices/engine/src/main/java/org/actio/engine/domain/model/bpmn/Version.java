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
package org.actio.engine.domain.model.bpmn;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.actio.engine.domain.model.bpmn.model.Model;
import org.actio.engine.domain.model.bpmn.process.Process;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity. Define a BPMN revision version, exposes BPMN definition and runtime.
 * 
 * @author quirino.brizi
 *
 */
public class Version {

    private final VersionId versionId;
    private Model model;
    private final Set<Process> processes;

    public Version(VersionId versionId) {
        Validate.notNull(versionId, "version identifier must not be null");
        this.versionId = versionId;
        this.processes = new HashSet<>();
    }

    public String getVersionId() {
        return versionId.toString();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void addProcess(Process process) {
        Validate.notNull(process, "process must not be null");
        this.processes.add(process);
    }

    public Set<Process> getProcesses() {
        return Collections.unmodifiableSet(processes);
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
        Version other = (Version) obj;
        return this.versionId.equals(other.versionId);
    }

    @Override
    public int hashCode() {
        return this.versionId.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("versionId", versionId).append("model", model).append("processes", processes);
        return builder.toString();
    }

}
