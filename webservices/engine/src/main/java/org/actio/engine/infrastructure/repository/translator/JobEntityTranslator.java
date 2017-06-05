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
package org.actio.engine.infrastructure.repository.translator;

import java.util.Collection;
import java.util.HashSet;

import org.actio.engine.domain.model.bpmn.process.Job;
import org.actio.engine.domain.model.bpmn.process.JobType;
import org.actio.engine.infrastructure.Translator;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class JobEntityTranslator implements Translator<Job, org.activiti.engine.runtime.Job> {

    @Override
    public Job translate(org.activiti.engine.runtime.Job job) {
        JobEntity entity = (JobEntity) job;
        return new Job(entity.getId(), JobType.from(entity.getJobType()), entity.getDuedate());
    }

    @Override
    public Collection<Job> translate(Collection<org.activiti.engine.runtime.Job> jobs) {
        Collection<Job> answer = new HashSet<>();
        for (org.activiti.engine.runtime.Job entity : jobs) {
            answer.add(translate(entity));
        }
        return answer;
    }

}
