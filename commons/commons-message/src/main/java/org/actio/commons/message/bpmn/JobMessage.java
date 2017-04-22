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
package org.actio.commons.message.bpmn;

import java.util.Date;

import org.actio.commons.message.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class JobMessage implements Message {

    private static final long serialVersionUID = -7557517181827746346L;

    @JsonProperty("jobId")
    private String id;
    @JsonProperty("jobType")
    private String jobType;
    @JsonProperty("dueDate")
    private Date dueDate;

    @JsonCreator
    public JobMessage(@JsonProperty("jobId") String id, @JsonProperty("jobType") String jobType, @JsonProperty("dueDate") Date dueDate) {
        this.id = id;
        this.jobType = jobType;
        this.dueDate = dueDate;
    }
}
