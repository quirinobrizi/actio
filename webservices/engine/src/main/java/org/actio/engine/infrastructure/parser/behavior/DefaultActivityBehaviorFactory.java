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
package org.actio.engine.infrastructure.parser.behavior;

import java.util.List;

import org.actio.engine.domain.model.activities.SendTask;
import org.activiti.bpmn.model.TaskWithFieldExtensions;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.ServiceTaskDelegateExpressionActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ServiceTaskExpressionActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ShellActivityBehavior;
import org.activiti.engine.impl.bpmn.helper.ClassDelegate;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link ActivityBehaviorFactory}. Used when no
 * 
 * @author quirino.brizi
 *
 */
public class DefaultActivityBehaviorFactory extends org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory {

    public ShellActivityBehavior createShellActivityBehavior(TaskWithFieldExtensions task) {
        List<FieldDeclaration> fieldDeclarations = createFieldDeclarations(task.getFieldExtensions());
        return (ShellActivityBehavior) ClassDelegate.defaultInstantiateDelegate(ShellActivityBehavior.class, fieldDeclarations);
    }

    public ClassDelegate createClassDelegateServiceTask(SendTask sendTask) {
        Expression skipExpression;
        if (StringUtils.isNotEmpty(sendTask.getSkipExpression())) {
            skipExpression = expressionManager.createExpression(sendTask.getSkipExpression());
        } else {
            skipExpression = null;
        }
        return new ClassDelegate(sendTask.getId(), sendTask.getImplementation(), createFieldDeclarations(sendTask.getFieldExtensions()),
                skipExpression, sendTask.getMapExceptions());
    }

    public ServiceTaskDelegateExpressionActivityBehavior createSendTaskDelegateExpressionActivityBehavior(SendTask sendTask) {
        Expression delegateExpression = expressionManager.createExpression(sendTask.getImplementation());
        Expression skipExpression;
        if (StringUtils.isNotEmpty(sendTask.getSkipExpression())) {
            skipExpression = expressionManager.createExpression(sendTask.getSkipExpression());
        } else {
            skipExpression = null;
        }
        return new ServiceTaskDelegateExpressionActivityBehavior(sendTask.getId(), delegateExpression, skipExpression,
                createFieldDeclarations(sendTask.getFieldExtensions()));
    }

    public ServiceTaskExpressionActivityBehavior createSendTaskExpressionActivityBehavior(SendTask sendTask) {
        Expression expression = expressionManager.createExpression(sendTask.getImplementation());
        Expression skipExpression;
        if (StringUtils.isNotEmpty(sendTask.getSkipExpression())) {
            skipExpression = expressionManager.createExpression(sendTask.getSkipExpression());
        } else {
            skipExpression = null;
        }
        return new ServiceTaskExpressionActivityBehavior(sendTask.getId(), expression, skipExpression,
                sendTask.getResultVariableName());
    }
}
