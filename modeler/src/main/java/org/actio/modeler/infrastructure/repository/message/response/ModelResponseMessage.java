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
package org.actio.modeler.infrastructure.repository.message.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class ModelResponseMessage implements Serializable {

	private static final long serialVersionUID = 1447519471851571954L;

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
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
	@JsonProperty("deploymentUrl")
	private String deploymentUrl;
	@JsonProperty("createTime")
	private String createTime;
	@JsonProperty("lastUpdateTime")
	private String lastUpdateTime;

	@JsonIgnore
	public String getId() {
		return id;
	}

	@JsonIgnore
	public String getUrl() {
		return url;
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
	public String getDeploymentUrl() {
		return deploymentUrl;
	}

	@JsonIgnore
	public String getCreateTime() {
		return createTime;
	}

	@JsonIgnore
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
}
