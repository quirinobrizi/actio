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

import org.actio.commons.message.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class BpmnMessage implements Message {

    private static final long serialVersionUID = 9077148071385731149L;

    @JsonProperty("bpmnId")
    private String bpmnId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("versions")
    private Collection<VersionMessage> versions;
    @JsonProperty("errors")
    private Collection<ErrorMessage> errors;

    @JsonCreator
    public BpmnMessage(@JsonProperty("bpmnId") String bpmnId, @JsonProperty("name") String name,
            @JsonProperty("versions") Collection<VersionMessage> versions, @JsonProperty("errors") Collection<ErrorMessage> errors) {
        this.bpmnId = bpmnId;
        this.name = name;
        this.versions = versions;
        this.errors = errors;
    }
}
