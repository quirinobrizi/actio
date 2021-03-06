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

import org.actio.commons.message.AbstractDiscoverableMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class ProcessMessage extends AbstractDiscoverableMessage {

	private static final long serialVersionUID = -1127228873734980140L;

	@JsonProperty("processId")
	private String processId;
	@JsonProperty("processName")
	private String processName;

	@JsonCreator
	public ProcessMessage(@JsonProperty("processId") String processId, @JsonProperty("processName") String processName,
			@JsonProperty("href") String href) {
		super(href);
		this.processId = processId;
		this.processName = processName;
	}

	@JsonIgnore
	public String getProcessId() {
		return processId;
	}

	@JsonIgnore
	public String getProcessName() {
		return processName;
	}
}
