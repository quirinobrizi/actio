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

import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.domain.service.CommandExecutorService;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A.R. Define the entrypoint for the definition and runtime.
 * 
 * @author quirino.brizi
 *
 */
public class Bpmn {

    private final BpmnId bpmnId;
    private String name;
    private final Set<Version> versions;

    public Bpmn(BpmnId bpmnId, String name) {
        Validate.notNull(bpmnId, "bpmn identifier must not be null");
        this.bpmnId = bpmnId;
        this.name = name;
        this.versions = new HashSet<>();
    }

    public String getId() {
        return bpmnId.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasVersion(VersionId versionId) {
        return this.versions.contains(new Version(versionId));
    }

    public Version getVersion(VersionId versionId) {
        for (Version version : this.versions) {
            if (version.equals(new Version(versionId))) {
                return version;
            }
        }
        return null;
    }

    public void addVersion(Version version) {
        Validate.notNull(version, "version must not be null");
        this.versions.add(version);
    }

    public Set<Version> getVersions() {
        return Collections.unmodifiableSet(versions);
    }

    public void startNewProcessInstance(CommandExecutorService commandExecutorService, Inputs inputs) {
        Instance instance = commandExecutorService.startNewBpmnProcessInstance(bpmnId, inputs);
        getLatestVersion().addProcessInstance(instance); // NOSONAR
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
        Bpmn other = (Bpmn) obj;
        return this.bpmnId.equals(other.bpmnId);
    }

    @Override
    public int hashCode() {
        return this.bpmnId.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("bpmnId", bpmnId).append("name", name).append("versions", versions);
        return builder.toString();
    }

    private Version getLatestVersion() {
        Version answer = null;
        for (Version version : this.versions) {
            if (null == answer) {
                answer = version;
                continue;
            } else {
                if (version.getVersionId().compareTo(answer.getVersionId()) > 0) {
                    answer = version;
                }
            }
        }
        return answer;
    }
}
