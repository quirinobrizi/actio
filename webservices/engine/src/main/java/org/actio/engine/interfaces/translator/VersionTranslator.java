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

import org.actio.commons.message.bpmn.ModelMessage;
import org.actio.commons.message.bpmn.ProcessMessage;
import org.actio.commons.message.bpmn.VersionMessage;
import org.actio.engine.domain.model.bpmn.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class VersionTranslator implements Translator<VersionMessage, Version> {

    @Autowired
    private ModelTranslator modelTranslator;
    @Autowired
    private ProcessTranslator processTranslator;

    @Override
    public VersionMessage translate(Version version) {
        ModelMessage model = modelTranslator.translate(version.getModel());
        Collection<ProcessMessage> processes = processTranslator.translate(version.getProcesses());
        return new VersionMessage(version.getVersionId(), model, processes);
    }

    @Override
    public Collection<VersionMessage> translate(Collection<Version> versions) {
        Set<VersionMessage> answer = new HashSet<>();
        for (Version version : versions) {
            answer.add(translate(version));
        }
        return answer;
    }

}
