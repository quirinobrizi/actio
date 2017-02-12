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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class ProcessMetrics {

	@JsonProperty("key")
	private String key;
	@JsonProperty("versions")
	private List<VersionMetrics> versions;

	public ProcessMetrics(String key) {
		this.key = key;
		this.versions = new ArrayList<>();
	}

	@JsonIgnore
	public String getKey() {
		return key;
	}

	@JsonIgnore
	public List<VersionMetrics> getVersions() {
		return Collections.unmodifiableList(versions);
	}

	public void updateOrCreate(String version, Integer completed, Integer running) {
		VersionMetrics versionMetrics = null;
		VersionMetrics newVersionMetrics = new VersionMetrics(version);
		int indexOf = this.versions.indexOf(newVersionMetrics);
		if (indexOf > 0) {
			versionMetrics = this.versions.get(indexOf);
		} else {
			versionMetrics = newVersionMetrics;
			this.versions.add(versionMetrics);
		}
		versionMetrics.increaseCompleted(completed);
		versionMetrics.increaseRunning(running);
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
		ProcessMetrics other = (ProcessMetrics) obj;
		return new EqualsBuilder().append(this.key, other.key).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.key).toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("key", key).append("versions", versions);
		return builder.toString();
	}

}
