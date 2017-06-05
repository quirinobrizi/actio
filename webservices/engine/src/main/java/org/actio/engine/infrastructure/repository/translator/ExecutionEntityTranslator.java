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
import java.util.List;

import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.domain.model.bpmn.process.InstanceId;
import org.actio.engine.domain.model.bpmn.process.InstanceState;
import org.actio.engine.infrastructure.Translator;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ExecutionEntityTranslator implements Translator<Instance, ExecutionEntity> {

    @Autowired
    private JobEntityTranslator jobEntityTranslator;
    @Autowired
    private TaskEntityTranslator taskEntityTranslator;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ManagementService managementService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.infrastructure.Translator#translate(java.lang.Object)
     */
    @Override
    public Instance translate(ExecutionEntity entity) {
        Instance instance = new Instance(InstanceId.newInstance(entity.getId()));
        instance.setVariables(entity.getProcessVariables());
        InstanceState instanceState;
        if (entity.isEnded()) {
            instanceState = InstanceState.TERMINATED;
        } else if (entity.isSuspended()) {
            instanceState = InstanceState.SUSPENDED;
        } else {
            instanceState = InstanceState.ACTIVE;
        }
        instance.setInstanceState(instanceState);
        instance.setStartDate(entity.getLockTime());
        List<Job> jobs = managementService.createJobQuery().executionId(entity.getId()).list();
        instance.setJobs(jobEntityTranslator.translate(jobs));
        List<Task> tasks = taskService.createTaskQuery().executionId(entity.getId()).list();
        instance.setTasks(taskEntityTranslator.translate(tasks));
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.infrastructure.Translator#translate(java.util.
     * Collection)
     */
    @Override
    public Collection<Instance> translate(Collection<ExecutionEntity> inputs) {
        Collection<Instance> answer = new HashSet<>();
        for (ExecutionEntity executionEntity : inputs) {
            answer.add(translate(executionEntity));
        }
        return answer;
    }

}
