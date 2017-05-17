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
package org.actio.engine.infrastructure.command;

import java.util.Map;

import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.model.bpmn.Inputs;
import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.domain.service.CommandExecutorService;
import org.actio.engine.infrastructure.activiti.translator.ExecutionEntityTranslator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class CommandExecutorServiceImpl implements CommandExecutorService {

    // private final ExecutorService executor =
    // Executors.newFixedThreadPool(100);

    @Autowired
    private CommandFactory commandFactory;
    @Autowired
    private ExecutionEntityTranslator executionEntityTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.domain.service.CommandExecutorService#
     * startNewBpmnProcessInstance(org.actio.engine.domain.model.bpmn.BpmnId,
     * org.actio.engine.domain.model.bpmn.Inputs)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Instance startNewBpmnProcessInstance(BpmnId bpmnId, Inputs inputs) {
        Command<Map<String, Object>, ExecutionEntity> command = (Command<Map<String, Object>, ExecutionEntity>) commandFactory
                .get(Command.Type.START_PROCESS_INSTANCE);
        return executionEntityTranslator.translate(command.execute(bpmnId.toString(), inputs));
    }
}
