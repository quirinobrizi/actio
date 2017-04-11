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
package org.actio.engine.domain.model.bpmn.process;

import org.apache.commons.lang3.Validate;

/**
 * V.O. define the process identifier
 * 
 * @author quirino.brizi
 *
 */
public class InstanceId {

    public static InstanceId newInstance(String identifier) {
        return new InstanceId(identifier);
    }

    private String identifier;

    private InstanceId(String identifier) {
        Validate.notBlank(identifier, "process identifier must be provided");
        this.identifier = identifier;
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
        InstanceId other = (InstanceId) obj;
        return this.identifier.equals(other.identifier);
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public String toString() {
        return this.identifier;
    }
}
