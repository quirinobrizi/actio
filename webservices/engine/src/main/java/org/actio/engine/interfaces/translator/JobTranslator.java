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
package org.actio.engine.interfaces.translator;

import java.util.Collection;
import java.util.HashSet;

import org.actio.commons.message.bpmn.JobMessage;
import org.actio.engine.domain.model.bpmn.process.Job;
import org.actio.engine.infrastructure.Translator;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class JobTranslator implements Translator<JobMessage, Job> {

    @Override
    public JobMessage translate(Job job) {
        return new JobMessage(job.getId(), job.getJobType().name(), job.getDueDate());
    }

    @Override
    public Collection<JobMessage> translate(Collection<Job> jobs) {
        Collection<JobMessage> answer = new HashSet<>();
        for (Job job : jobs) {
            answer.add(translate(job));
        }
        return answer;
    }

}
