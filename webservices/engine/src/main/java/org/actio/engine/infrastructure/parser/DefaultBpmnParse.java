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

import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ValidationError;

/**
 * @author quirino.brizi
 *
 */
public class DefaultBpmnParse extends BpmnParse {

    private BpmnXMLConverter bpmnXmlConverter;

    public DefaultBpmnParse(BpmnParser parser) {
        super(parser);
    }

    @Override
    public BpmnParse execute() {
        try {

            ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();

            boolean enableSafeBpmnXml = false;
            String encoding = null;
            if (processEngineConfiguration != null) {
                enableSafeBpmnXml = processEngineConfiguration.isEnableSafeBpmnXml();
                encoding = processEngineConfiguration.getXmlEncoding();
            }

            if (encoding != null) {
                bpmnModel = bpmnXmlConverter.convertToBpmnModel(streamSource, validateSchema, enableSafeBpmnXml, encoding);
            } else {
                bpmnModel = bpmnXmlConverter.convertToBpmnModel(streamSource, validateSchema, enableSafeBpmnXml);
            }

            // XSD validation goes first, then process/semantic validation
            if (validateProcess) {
                ProcessValidator processValidator = processEngineConfiguration.getProcessValidator();
                if (processValidator == null) {
                    LOGGER.warn("Process should be validated, but no process validator is configured on the process engine configuration!");
                } else {
                    List<ValidationError> validationErrors = processValidator.validate(bpmnModel);
                    if (validationErrors != null && !validationErrors.isEmpty()) {

                        StringBuilder warningBuilder = new StringBuilder();
                        StringBuilder errorBuilder = new StringBuilder();

                        for (ValidationError error : validationErrors) {
                            if (error.isWarning()) {
                                warningBuilder.append(error.toString());
                                warningBuilder.append("\n");
                            } else {
                                errorBuilder.append(error.toString());
                                errorBuilder.append("\n");
                            }
                        }

                        // Throw exception if there is any error
                        if (errorBuilder.length() > 0) {
                            throw new ActivitiException("Errors while parsing:\n" + errorBuilder.toString());
                        }

                        // Write out warnings (if any)
                        if (warningBuilder.length() > 0) {
                            LOGGER.warn("Following warnings encountered during process validation: " + warningBuilder.toString());
                        }

                    }
                }
            }

            // Validation successfull (or no validation)
            createImports();
            createItemDefinitions();
            createMessages();
            createOperations();
            transformProcessDefinitions();

        } catch (Exception e) {
            if (e instanceof ActivitiException) {
                throw (ActivitiException) e;
            } else if (e instanceof XMLException) {
                throw (XMLException) e;
            } else {
                throw new ActivitiException("Error parsing XML", e);
            }
        }

        return this;
    }

    public void setBpmnXMLConverter(BpmnXMLConverter bpmnXMLConverter) {
        this.bpmnXmlConverter = bpmnXMLConverter;
    }
}
