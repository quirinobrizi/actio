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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * V.O. the resource that define a model;
 * 
 * @author quirino.brizi
 *
 */
public class Resource {

    public static Resource newInstance(String definition) {
        return new Resource(definition);
    }

    private final String definition;

    private Resource(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
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
        Resource other = (Resource) obj;
        return this.definition.equals(other.definition);
    }

    @Override
    public int hashCode() {
        return this.definition.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("definition", definition);
        return builder.toString();
    }

}
