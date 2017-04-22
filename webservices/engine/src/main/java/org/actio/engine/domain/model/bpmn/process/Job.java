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

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author quirino.brizi
 *
 */
public class Job {

    private String id;
    private JobType jobType;
    private Date dueDate;

    public Job(String id, JobType jobType, Date dueDate) {
        this.id = id;
        this.jobType = jobType;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public JobType getJobType() {
        return jobType;
    }

    public Date getDueDate() {
        return dueDate;
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
        Job other = (Job) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("id", id).append("jobType", jobType);
        return builder.toString();
    }

}
