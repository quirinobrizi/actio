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
package org.actio.commons.message.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.actio.commons.message.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class UpdateProcessStateRequestMessage implements Message {

	private static final long serialVersionUID = -3835476952876798738L;

	@JsonProperty("action")
	private String action;
	@JsonProperty("processId")
	private String processId;
	@JsonProperty("inputs")
	private Map<String, Object> inputs;

	@JsonCreator
	public UpdateProcessStateRequestMessage(@JsonProperty("action") String action,
			@JsonProperty("processId") String processId, @JsonProperty("inputs") Map<String, Object> inputs) {
		this.action = action;
		this.processId = processId;
		this.inputs = inputs == null ? new HashMap<>() : inputs;
	}

	@JsonIgnore
	public String getAction() {
		return action;
	}

	@JsonIgnore
	public String getProcessId() {
		return processId;
	}

	@JsonIgnore
	public Map<String, Object> getInputs() {
		return Collections.unmodifiableMap(inputs);
	}
}
