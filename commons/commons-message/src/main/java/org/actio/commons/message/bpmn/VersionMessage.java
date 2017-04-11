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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class VersionMessage implements Message {

    private static final long serialVersionUID = -8373356750153900980L;

    @JsonProperty("versionId")
    private String versionId;
    @JsonProperty("model")
    private ModelMessage model;
    @JsonProperty("processes")
    private Collection<ProcessMessage> processes;

    public VersionMessage(@JsonProperty("versionId") String versionId, @JsonProperty("model") ModelMessage model,
            @JsonProperty("processes") Collection<ProcessMessage> processes) {
        this.versionId = versionId;
        this.model = model;
        this.processes = processes;
    }
}
