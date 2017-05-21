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

import org.actio.engine.domain.model.bpmn.process.Task;
import org.actio.engine.infrastructure.Translator;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class TaskEntityTranslator implements Translator<Task, TaskEntity> {

    @Override
    public Task translate(TaskEntity entity) {
        return new Task(entity.getId(), entity.getAssignee(), entity.getDueDate());
    }

    @Override
    public Collection<Task> translate(Collection<TaskEntity> tasks) {
        Collection<Task> answer = new HashSet<>();
        for (TaskEntity entity : tasks) {
            answer.add(translate(entity));
        }
        return answer;
    }

}
