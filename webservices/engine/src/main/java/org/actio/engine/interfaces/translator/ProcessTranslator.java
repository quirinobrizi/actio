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
import java.util.Set;

import org.actio.commons.message.bpmn.InstanceMessage;
import org.actio.commons.message.bpmn.ProcessMessage;
import org.actio.engine.domain.model.bpmn.process.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ProcessTranslator implements Translator<ProcessMessage, Process> {

    @Autowired
    private InstanceTranslator intanceTranslator;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.lang.
     * Object)
     */
    @Override
    public ProcessMessage translate(Process process) {
        Collection<InstanceMessage> instances = intanceTranslator.translate(process.getInstances());
        return new ProcessMessage(process.getProcessId(), instances);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.util.
     * Collection)
     */
    @Override
    public Collection<ProcessMessage> translate(Collection<Process> processes) {
        Set<ProcessMessage> answer = new HashSet<>();
        for (Process process : processes) {
            answer.add(translate(process));
        }
        return answer;
    }

}
