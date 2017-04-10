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
package org.actio.engine.domain.model.bpmn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author quirino.brizi
 *
 */
public enum Action {
    START("start") {
        @Override
        public Bpmn execute(RuntimeService runtimeService, String processId, Map<String, Object> inputs) {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId, inputs);
            return new Bpmn(BpmnId.newInstance(processInstance.getId()), processInstance.getName());
        };
    },
    SUSPEND("suspend") {
        @Override
        public Bpmn execute(RuntimeService runtimeService, String processId, Map<String, Object> inputs) {
            runtimeService.suspendProcessInstanceById(processId);
            return null;
        };
    },
    RESUME("resume") {
        @Override
        public Bpmn execute(RuntimeService runtimeService, String processId, Map<String, Object> inputs) {
            runtimeService.activateProcessInstanceById(processId);
            return null;
        };
    };

    private final List<String> aliases;

    private Action(String... aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    public abstract Bpmn execute(RuntimeService runtimeService, String processId, Map<String, Object> inputs);

    public static Action get(String alias) {
        for (Action action : values()) {
            if (action.aliases.contains(alias.toLowerCase())) {
                return action;
            }
        }
        throw new IllegalArgumentException();
    }
}
