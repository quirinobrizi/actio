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
package org.actio.engine.domain.model.bpmn;

import org.apache.commons.lang3.Validate;

/**
 * V.O. define the process identifier
 * 
 * @author quirino.brizi
 *
 */
public class VersionId implements Comparable<VersionId> {

    public static VersionId newInstance(Integer identifier) {
        return new VersionId(identifier);
    }

    private Integer identifier;

    private VersionId(Integer identifier) {
        Validate.notNull(identifier, "process identifier must be provided");
        this.identifier = identifier;
    }

    public Integer asInteger() {
        return this.identifier;
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
        VersionId other = (VersionId) obj;
        return this.identifier.equals(other.identifier);
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(this.identifier);
    }

    @Override
    public int compareTo(VersionId other) {
        if (this.identifier < other.identifier) {
            return -1;
        }
        if (this.identifier.equals(other.identifier)) {
            return 0;
        }
        return 1;
    }

}
