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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.actio.commons.message.bpmn.InstanceMessage;
import org.actio.commons.message.bpmn.JobMessage;
import org.actio.commons.message.bpmn.TaskMessage;
import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.infrastructure.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class InstanceTranslator implements Translator<InstanceMessage, Instance> {

    @Autowired
    private JobTranslator jobTranslator;
    @Autowired
    private TaskTranslator taskTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.lang.
     * Object)
     */
    @Override
    public InstanceMessage translate(Instance instance) {
        Long startDate = null != instance.getStartDate() ? instance.getStartDate().getTime() : null;
        Long endDate = null != instance.getEndDate() ? instance.getEndDate().getTime() : null;
        Collection<JobMessage> jobs = jobTranslator.translate(instance.getJobs());
        Collection<TaskMessage> tasks = taskTranslator.translate(instance.getTasks());
        return new InstanceMessage(instance.getInstanceId(), instance.getVariables(), instance.getProcessState().name(), startDate, endDate,
                jobs, tasks);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.util.
     * Collection)
     */
    @Override
    public Collection<InstanceMessage> translate(Collection<Instance> instances) {
        List<InstanceMessage> answer = new ArrayList<>();
        for (Instance instance : instances) {
            answer.add(translate(instance));
        }
        return answer;
    }

}
