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
package org.actio.engine.interfaces.validator.impl;

import java.util.List;

import org.actio.engine.domain.model.activities.SendTask;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Interface;
import org.activiti.bpmn.model.Operation;
import org.activiti.bpmn.model.Process;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.Problems;
import org.activiti.validation.validator.impl.ExternalInvocationTaskValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * Validate {@link SendTask} definition.
 * 
 * @author quirino.brizi
 *
 */
public class SendTaskValidator extends ExternalInvocationTaskValidator {

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<SendTask> sendTasks = process.findFlowElementsOfType(SendTask.class);
        for (SendTask sendTask : sendTasks) {
            verifyImplementation(process, sendTask, errors);
            verifyType(process, sendTask, errors);
            verifyResultVariableName(process, sendTask, errors);
            verifyWebservice(bpmnModel, process, sendTask, errors);
        }

    }

    protected void verifyImplementation(Process process, SendTask sendTask, List<ValidationError> errors) {
        if (!ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(sendTask.getImplementationType())
                && !ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(sendTask.getImplementationType())
                && !ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(sendTask.getImplementationType())
                && !ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(sendTask.getImplementationType())
                && StringUtils.isEmpty(sendTask.getType())) {
            addError(errors, Problems.SERVICE_TASK_MISSING_IMPLEMENTATION, process, sendTask,
                    "One of the attributes 'class', 'delegateExpression', 'type', 'operation', or 'expression' is mandatory on sendTask.");
        }
    }

    protected void verifyType(Process process, SendTask sendTask, List<ValidationError> errors) {
        if (StringUtils.isNotEmpty(sendTask.getType())) {

            if (!sendTask.getType().equalsIgnoreCase("mail") && !sendTask.getType().equalsIgnoreCase("mule")
                    && !sendTask.getType().equalsIgnoreCase("camel") && !(sendTask.getType().equalsIgnoreCase("shell"))) {
                addError(errors, Problems.SERVICE_TASK_INVALID_TYPE, process, sendTask, "Invalid or unsupported send task type");
            }

            if (sendTask.getType().equalsIgnoreCase("mail")) {
                validateFieldDeclarationsForEmail(process, sendTask, sendTask.getFieldExtensions(), errors);
            } else if (sendTask.getType().equalsIgnoreCase("shell")) {
                validateFieldDeclarationsForShell(process, sendTask, sendTask.getFieldExtensions(), errors);
            }

        }
    }

    protected void verifyResultVariableName(Process process, SendTask sendTask, List<ValidationError> errors) {
        if (StringUtils.isNotEmpty(sendTask.getResultVariableName())
                && (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equals(sendTask.getImplementationType())
                        || ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equals(sendTask.getImplementationType()))) {
            addError(errors, Problems.SERVICE_TASK_RESULT_VAR_NAME_WITH_DELEGATE, process, sendTask,
                    "'resultVariableName' not supported for send tasks using 'class' or 'delegateExpression");
        }
    }

    protected void verifyWebservice(BpmnModel bpmnModel, Process process, SendTask sendTask, List<ValidationError> errors) {
        if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(sendTask.getImplementationType())
                && StringUtils.isNotEmpty(sendTask.getOperationRef())) {

            boolean operationFound = false;
            if (bpmnModel.getInterfaces() != null && !bpmnModel.getInterfaces().isEmpty()) {
                for (Interface bpmnInterface : bpmnModel.getInterfaces()) {
                    if (bpmnInterface.getOperations() != null && !bpmnInterface.getOperations().isEmpty()) {
                        for (Operation operation : bpmnInterface.getOperations()) {
                            if (operation.getId() != null && operation.getId().equals(sendTask.getOperationRef())) {
                                operationFound = true;
                            }
                        }
                    }
                }
            }

            if (!operationFound) {
                addError(errors, Problems.SERVICE_TASK_WEBSERVICE_INVALID_OPERATION_REF, process, sendTask,
                        "Invalid operation reference");
            }

        }
    }

}
