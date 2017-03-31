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
package org.actio.engine.infrastructure.config;

import java.util.List;

import org.actio.engine.interfaces.parser.handler.SendTaskParserHandler;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.spring.SpringProcessEngineConfiguration;

/**
 * @author quirino.brizi
 *
 */
public class ProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    @Override
    protected List<BpmnParseHandler> getDefaultBpmnParseHandlers() {
        List<BpmnParseHandler> defaultBpmnParseHandlers = super.getDefaultBpmnParseHandlers();
        defaultBpmnParseHandlers.add(new SendTaskParserHandler());
        return defaultBpmnParseHandlers;
    }
}