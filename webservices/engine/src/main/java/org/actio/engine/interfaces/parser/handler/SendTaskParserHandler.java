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
package org.actio.engine.interfaces.parser.handler;

import org.actio.engine.domain.model.activities.SendTask;
import org.actio.engine.interfaces.parser.behavior.DefaultActivityBehaviorFactory;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.DataAssociation;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.engine.impl.bpmn.behavior.WebServiceActivityBehavior;
import org.activiti.engine.impl.bpmn.data.AbstractDataAssociation;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.AbstractExternalInvocationBpmnParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser handler for {@link SendTask}
 * 
 * @author quirino.brizi
 *
 */
public class SendTaskParserHandler extends AbstractExternalInvocationBpmnParseHandler<SendTask> {

    private static Logger logger = LoggerFactory.getLogger(SendTaskParserHandler.class);

    @Override
    public Class<? extends BaseElement> getHandledType() {
        return SendTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, SendTask sendTask) {

        ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, sendTask, BpmnXMLConstants.ELEMENT_TASK_SEND);
        activity.setAsync(sendTask.isAsynchronous());
        activity.setFailedJobRetryTimeCycleValue(sendTask.getFailedJobRetryTimeCycleValue());
        activity.setExclusive(!sendTask.isNotExclusive());

        // Email, Mule and Shell service tasks
        DefaultActivityBehaviorFactory activityBehaviorFactory = (DefaultActivityBehaviorFactory) bpmnParse.getActivityBehaviorFactory();
        if (StringUtils.isNotEmpty(sendTask.getType())) {

            if (sendTask.getType().equalsIgnoreCase("mail")) {
                activity.setActivityBehavior(activityBehaviorFactory.createMailActivityBehavior(sendTask));

            } else if (sendTask.getType().equalsIgnoreCase("mule")) {
                activity.setActivityBehavior(activityBehaviorFactory.createMuleActivityBehavior(sendTask, bpmnParse.getBpmnModel()));

            } else if (sendTask.getType().equalsIgnoreCase("camel")) {
                activity.setActivityBehavior(activityBehaviorFactory.createCamelActivityBehavior(sendTask, bpmnParse.getBpmnModel()));

            } else if (sendTask.getType().equalsIgnoreCase("shell")) {
                activity.setActivityBehavior(activityBehaviorFactory.createShellActivityBehavior(sendTask));

            } else {
                logger.warn("Invalid send task type: '" + sendTask.getType() + "' " + " for service task " + sendTask.getId());
            }

            // activiti:class
        } else if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(sendTask.getImplementationType())) {
            activity.setActivityBehavior(activityBehaviorFactory.createClassDelegateServiceTask(sendTask));

            // activiti:delegateExpression
        } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(sendTask.getImplementationType())) {
            activity.setActivityBehavior(activityBehaviorFactory.createSendTaskDelegateExpressionActivityBehavior(sendTask));

            // activiti:expression
        } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(sendTask.getImplementationType())) {
            activity.setActivityBehavior(activityBehaviorFactory.createSendTaskExpressionActivityBehavior(sendTask));

            // Webservice
        } else if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(sendTask.getImplementationType())
                && StringUtils.isNotEmpty(sendTask.getOperationRef())) {

            if (!bpmnParse.getOperations().containsKey(sendTask.getOperationRef())) {
                logger.warn(sendTask.getOperationRef() + " does not exist for service task " + sendTask.getId());
            } else {

                WebServiceActivityBehavior webServiceActivityBehavior = activityBehaviorFactory.createWebServiceActivityBehavior(sendTask);
                webServiceActivityBehavior.setOperation(bpmnParse.getOperations().get(sendTask.getOperationRef()));

                if (sendTask.getIoSpecification() != null) {
                    IOSpecification ioSpecification = createIOSpecification(bpmnParse, sendTask.getIoSpecification());
                    webServiceActivityBehavior.setIoSpecification(ioSpecification);
                }

                for (DataAssociation dataAssociationElement : sendTask.getDataInputAssociations()) {
                    AbstractDataAssociation dataAssociation = createDataInputAssociation(bpmnParse, dataAssociationElement);
                    webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
                }

                for (DataAssociation dataAssociationElement : sendTask.getDataOutputAssociations()) {
                    AbstractDataAssociation dataAssociation = createDataOutputAssociation(bpmnParse, dataAssociationElement);
                    webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
                }

                activity.setActivityBehavior(webServiceActivityBehavior);
            }
        } else {
            logger.warn(
                    "One of the attributes 'class', 'delegateExpression', 'type', 'operation', or 'expression' is mandatory on sendTask "
                            + sendTask.getId());
        }

    }
}
