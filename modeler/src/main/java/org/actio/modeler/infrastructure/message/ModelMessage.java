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
package org.actio.modeler.infrastructure.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class ModelMessage implements Serializable {

	private static final long serialVersionUID = -5661050930873384828L;

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("key")
	private String key;
	@JsonProperty("category")
	private String category;
	@JsonProperty("version")
	private Integer version;
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

	@JsonCreator
	public ModelMessage(@JsonProperty("id") String id, @JsonProperty("name") String name,
			@JsonProperty("key") String key, @JsonProperty("category") String category,
			@JsonProperty("version") Integer version, @JsonProperty("metaInfo") String metaInfo,
			@JsonProperty("deploymentId") String deploymentId, @JsonProperty("tenantId") String tenantId,
			@JsonProperty("definition") String definition, @JsonProperty("svg") String svg) {
		this.id = id;
		this.name = name;
		this.key = key;
		this.category = category;
		this.version = version;
		this.metaInfo = metaInfo;
		this.deploymentId = deploymentId;
		this.tenantId = tenantId;
		this.definition = definition;
		this.svg = svg;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}

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
	public Integer getVersion() {
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
