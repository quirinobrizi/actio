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
package org.actio.engine.interfaces.converter;

import javax.xml.stream.XMLStreamReader;

import org.actio.engine.infrastructure.parser.ScriptResourceParser;
import org.activiti.bpmn.converter.ScriptTaskXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ScriptTask;
import org.apache.commons.lang3.StringUtils;

/**
 * @author quirino.brizi
 *
 */
public class ScriptTaskXmlConverter extends ScriptTaskXMLConverter {

    public ScriptTaskXmlConverter() {
        super();
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        ScriptTask scriptTask = new ScriptTask();
        BpmnXMLUtil.addXMLLocation(scriptTask, xtr);
        scriptTask.setScriptFormat(xtr.getAttributeValue(null, ATTRIBUTE_TASK_SCRIPT_FORMAT));
        scriptTask.setResultVariable(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SCRIPT_RESULTVARIABLE));
        if (StringUtils.isEmpty(scriptTask.getResultVariable())) {
            scriptTask.setResultVariable(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_RESULTVARIABLE));
        }
        String autoStoreVariables = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SCRIPT_AUTO_STORE_VARIABLE);
        if (StringUtils.isNotEmpty(autoStoreVariables)) {
            scriptTask.setAutoStoreVariables(Boolean.valueOf(autoStoreVariables));
        }

        String scriptResource = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, "resource");
        if (StringUtils.isNotBlank(scriptResource)) {
            new ScriptResourceParser().parseChildElement(xtr, scriptTask, model);
        } else {
            parseChildElements(getXMLElementName(), scriptTask, childParserMap, model, xtr);
        }
        return scriptTask;
    }
}
