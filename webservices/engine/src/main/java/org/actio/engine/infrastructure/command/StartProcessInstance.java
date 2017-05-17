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
import java.util.concurrent.ExecutorService;

import org.actio.engine.infrastructure.activiti.command.StartProcessInstanceCommand;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class StartProcessInstance implements Command<Map<String, Object>, ExecutionEntity> {

    @Autowired
    private RuntimeService runtimeService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.infrastructure.command.Command#execute(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public ExecutionEntity execute(String bpmnId, Map<String, Object> inputs) {
        return (ExecutionEntity) runtimeService().getCommandExecutor()
                .execute(new StartProcessInstanceCommand<ProcessInstance>(bpmnId, inputs, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.infrastructure.service.command.Command#execute(java.lang
     * .Object)
     */
    @Override
    public ExecutionEntity execute(String bpmnId, Map<String, Object> inputs, ExecutorService executor) {
        return (ExecutionEntity) runtimeService().getCommandExecutor()
                .execute(new StartProcessInstanceCommand<ProcessInstance>(bpmnId, inputs, executor));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.infrastructure.service.command.Command#type()
     */
    @Override
    public org.actio.engine.infrastructure.command.Command.Type type() {
        return Command.Type.START_PROCESS_INSTANCE;
    }

    private RuntimeServiceImpl runtimeService() {
        return (RuntimeServiceImpl) runtimeService;
    }

}
