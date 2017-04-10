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
package org.actio.engine.domain.model.bpmn.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * V.O. Representation of the graphical BPMN instance
 * 
 * @author quirino.brizi
 *
 */
public class Model {

    private final Map<ResourceType, Resource> resources;

    public Model() {
        this.resources = new HashMap<>();
    }

    public Resource getResource(ResourceType resourceType) {
        return this.resources.get(resourceType);
    }

    public Map<ResourceType, Resource> getResources() {
        return Collections.unmodifiableMap(resources);
    }

    public void addResource(ResourceType resourceType, Resource resource) {
        Validate.notNull(resource, "resource must not be null");
        Validate.notNull(resourceType, "resource type must not be null");
        this.resources.put(resourceType, resource);
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
        Model other = (Model) obj;
        return this.resources.equals(other.resources);
    }

    @Override
    public int hashCode() {
        return this.resources.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("resources", resources);
        return builder.toString();
    }
}
