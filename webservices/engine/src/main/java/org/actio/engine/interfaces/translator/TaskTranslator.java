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

import org.actio.commons.message.bpmn.TaskMessage;
import org.actio.engine.domain.model.bpmn.process.Task;
import org.actio.engine.infrastructure.Translator;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class TaskTranslator implements Translator<TaskMessage, Task> {

    @Override
    public TaskMessage translate(Task task) {
        return new TaskMessage(task.getId(), task.getAssignee(), task.getDueDate());
    }

    @Override
    public Collection<TaskMessage> translate(Collection<Task> tasks) {
        Collection<TaskMessage> answer = new HashSet<>();
        for (Task task : tasks) {
            answer.add(translate(task));
        }
        return answer;
    }

}
