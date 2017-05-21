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
package org.actio.engine.infrastructure.repository.command;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.actio.engine.infrastructure.repository.BpmnErrorEvent;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.cmd.StartProcessInstanceCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author quirino.brizi
 *
 */
public class StartProcessInstanceCommand<T> extends StartProcessInstanceCmd<T> {

    private static final long serialVersionUID = 1037684037478981075L;

    private transient ExecutorService executorService;

    public StartProcessInstanceCommand(String processDefinitionKey, Map<String, Object> variables, ExecutorService executorService) {
        super(processDefinitionKey, null, null, variables);
        this.executorService = executorService;
    }

    @Override
    public ProcessInstance execute(CommandContext commandContext) {
        DeploymentManager deploymentManager = commandContext.getProcessEngineConfiguration().getDeploymentManager();

        // Find the process definition
        ProcessDefinitionEntity processDefinition = null;
        if (processDefinitionId != null) {
            processDefinition = deploymentManager.findDeployedProcessDefinitionById(processDefinitionId);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException("No process definition found for id = '" + processDefinitionId + "'",
                        ProcessDefinition.class);
            }
        } else if (processDefinitionKey != null && (tenantId == null || ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId))) {
            processDefinition = deploymentManager.findDeployedLatestProcessDefinitionByKey(processDefinitionKey);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException("No process definition found for key '" + processDefinitionKey + "'",
                        ProcessDefinition.class);
            }
        } else if (processDefinitionKey != null && tenantId != null && !ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId)) {
            processDefinition = deploymentManager.findDeployedLatestProcessDefinitionByKeyAndTenantId(processDefinitionKey, tenantId);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException(
                        "No process definition found for key '" + processDefinitionKey + "' for tenant identifier " + tenantId,
                        ProcessDefinition.class);
            }
        } else {
            throw new ActivitiIllegalArgumentException("processDefinitionKey and processDefinitionId are null");
        }

        // Do not start process a process instance if the process definition is
        // suspended
        if (deploymentManager.isProcessDefinitionSuspended(processDefinition.getId())) {
            throw new ActivitiException("Cannot start process instance. Process definition " + processDefinition.getName() + " (id = "
                    + processDefinition.getId() + ") is suspended");
        }

        // Start the process instance
        ExecutionEntity processInstance = processDefinition.createProcessInstance(businessKey);

        // now set the variables passed into the start command
        initializeVariables(processInstance);

        // now set processInstance name
        if (processInstanceName != null) {
            processInstance.setName(processInstanceName);
            commandContext.getHistoryManager().recordProcessInstanceNameChange(processInstance.getId(), processInstanceName);
        }

        doStartProcess(commandContext, processInstance);

        return processInstance;
    }

    private void doStartProcess(CommandContext commandContext, ExecutionEntity processInstance) {
        try {
            if (null != executorService) {
                executorService.execute(processInstance::start);
            } else {
                processInstance.start();
            }
        } catch (Exception e) {
            dispatchErrorEvent(commandContext, processInstance, e);
            throw e;
        }
    }

    private void dispatchErrorEvent(CommandContext commandContext, ExecutionEntity processInstance, Exception e) {
        BpmnErrorEvent event = new BpmnErrorEvent(ActivitiEventType.UNCAUGHT_BPMN_ERROR);
        event.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        event.setProcessInstanceId(processInstance.getId());
        event.setErrorMessage(e.getMessage());
        event.setErrorType(e.getClass().getName());
        commandContext.getEventDispatcher().dispatchEvent(event);
    }
}
