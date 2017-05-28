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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * V.O. describe an uncaught BPMN execution error.
 * 
 * @author quirino.brizi
 *
 */
public class Error {

    private final String errorType;
    private final String errorMessage;
    private final Long timestamp;

    public Error(String errorType, String errorMessage, Long timestamp) {
        Validate.notBlank(errorType, "Error type must be provided");
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.timestamp = null == timestamp ? System.currentTimeMillis() : timestamp;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getTimestamp() {
        return timestamp;
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
        Error other = (Error) obj;
        return new EqualsBuilder().append(this.errorType, other.errorType).append(this.errorMessage, other.errorMessage)
                .append(this.timestamp, other.timestamp).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.errorType).append(this.errorMessage).append(this.timestamp).toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("errorType", errorType).append("errorMessage", errorMessage).append("timestamp", timestamp);
        return builder.toString();
    }

}
