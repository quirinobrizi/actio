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

import org.actio.commons.message.bpmn.ModelMessage;
import org.actio.engine.domain.model.bpmn.model.Model;
import org.actio.engine.domain.model.bpmn.model.ResourceType;
import org.actio.engine.infrastructure.Translator;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class ModelTranslator implements Translator<ModelMessage, Model> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.lang.
     * Object)
     */
    @Override
    public ModelMessage translate(Model model) {
        return new ModelMessage(model.getModelId(), model.getResource(ResourceType.XML).getDefinition(),
                model.getResource(ResourceType.SVG).getDefinition());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.engine.interfaces.translator.Translator#translate(java.util.
     * Collection)
     */
    @Override
    public Collection<ModelMessage> translate(Collection<Model> input) {
        return null;
    }

}
