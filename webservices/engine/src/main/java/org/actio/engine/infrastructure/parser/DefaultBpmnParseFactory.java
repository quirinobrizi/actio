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
package org.actio.engine.infrastructure.parser;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.cfg.BpmnParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class DefaultBpmnParseFactory implements BpmnParseFactory {

    @Autowired
    private BpmnXMLConverter bpmnXMLConverter;

    @Override
    public BpmnParse createBpmnParse(BpmnParser bpmnParser) {
        DefaultBpmnParse defaultBpmnParse = new DefaultBpmnParse(bpmnParser);
        defaultBpmnParse.setBpmnXMLConverter(bpmnXMLConverter);
        return defaultBpmnParse;
    }

}
