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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A request message that allows to exchange information between UI and modeler
 * service
 * 
 * @author quirino.brizi
 *
 */
public class Model implements Serializable {

	private static final long serialVersionUID = -8545462369415759403L;

	@JsonProperty("name")
	private String name;
	@JsonProperty("key")
	private String key;
	@JsonProperty("category")
	private String category;
	@JsonProperty("version")
	private String version;
	@JsonProperty("metaInfo")
	private String metaInfo;
	@JsonProperty("deploymentId")
	private String deploymentId;
	@JsonProperty("tenantId")
	private String tenantId;
	@JsonProperty("definition")
	private String definition;
	@JsonProperty("svg")
	private String svg;

	@JsonIgnore
	public String getName() {
		return name;
	}

	@JsonIgnore
	public String getKey() {
		return key;
	}

	@JsonIgnore
	public String getCategory() {
		return category;
	}

	@JsonIgnore
	public String getVersion() {
		return version;
	}

	@JsonIgnore
	public String getMetaInfo() {
		return metaInfo;
	}

	@JsonIgnore
	public String getDeploymentId() {
		return deploymentId;
	}

	@JsonIgnore
	public String getTenantId() {
		return tenantId;
	}

	@JsonIgnore
	public String getDefinition() {
		return definition;
	}

	@JsonIgnore
	public String getSvg() {
		return svg;
	}

}
