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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class VersionMetrics {

	@JsonProperty("version")
	private String version;
	@JsonProperty("running")
	private Integer running = 0;
	@JsonProperty("completed")
	private Integer completed = 0;

	public VersionMetrics(String version) {
		this.version = version;
	}

	@JsonIgnore
	public String getVersion() {
		return version;
	}

	@JsonIgnore
	public Integer getRunning() {
		return running;
	}

	@JsonIgnore
	public void increaseRunning(Integer add) {
		this.running += add;
	}

	@JsonIgnore
	public Integer getCompleted() {
		return completed;
	}

	@JsonIgnore
	public void increaseCompleted(Integer add) {
		this.completed += add;
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
		VersionMetrics other = (VersionMetrics) obj;
		return new EqualsBuilder().append(this.version, other.version).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.version).toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("version", version).append("running", running).append("completed", completed);
		return builder.toString();
	}
}
