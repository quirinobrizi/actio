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

/**
 * A request message that allows to exchange information between UI and modeler
 * service
 * 
 * @author quirino.brizi
 *
 */
public class Model implements Serializable {

	private static final long serialVersionUID = -8545462369415759403L;

	private String name;
	private String key;
	private String category;
	private Integer version;
	private String metaInfo;
	private String deploymentId;
	private String tenantId;
	private String definition;
	private String svg;

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getCategory() {
		return category;
	}

	public Integer getVersion() {
		return version;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getDefinition() {
		return definition;
	}

	public String getSvg() {
		return svg;
	}

}
