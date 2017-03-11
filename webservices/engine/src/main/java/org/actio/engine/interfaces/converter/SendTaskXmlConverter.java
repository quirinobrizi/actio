package org.actio.engine.interfaces.converter;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.actio.engine.domain.model.activities.SendTask;
import org.activiti.bpmn.converter.SendTaskXMLConverter;
import org.activiti.bpmn.converter.export.FieldExtensionExport;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.ImplementationType;
import org.apache.commons.lang3.StringUtils;

public class SendTaskXmlConverter extends SendTaskXMLConverter {

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        SendTask sendTask = new SendTask();
        BpmnXMLUtil.addXMLLocation(sendTask, xtr);

        sendTask.setType(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TYPE));

        String clazz = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_CLASS);
        String expression = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_EXPRESSION);
        String delegateExpression = xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION);
        String implementation = xtr.getAttributeValue(null, ATTRIBUTE_TASK_IMPLEMENTATION);
        if (StringUtils.isNotEmpty(clazz)) {
            sendTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
            sendTask.setImplementation(clazz);
        } else if (StringUtils.isNotEmpty(expression)) {
            sendTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
            sendTask.setImplementation(expression);
        } else if (StringUtils.isNotEmpty(delegateExpression)) {
            sendTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            sendTask.setImplementation(delegateExpression);
        } else if ("##WebService".equals(implementation)) {
            sendTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE);
            sendTask.setOperationRef(parseOperationRef(xtr.getAttributeValue(null, ATTRIBUTE_TASK_OPERATION_REF), model));
        }

        sendTask.setResultVariableName(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_RESULTVARIABLE));
        if (StringUtils.isEmpty(sendTask.getResultVariableName())) {
            sendTask.setResultVariableName(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, "resultVariable"));
        }
        sendTask.setExtensionId(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_EXTENSIONID));

        if (StringUtils.isNotEmpty(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_SKIP_EXPRESSION))) {
            sendTask.setSkipExpression(xtr.getAttributeValue(ACTIVITI_EXTENSIONS_NAMESPACE, ATTRIBUTE_TASK_SERVICE_SKIP_EXPRESSION));
        }
        parseChildElements(getXMLElementName(), sendTask, model, xtr);

        return sendTask;
    }

    @Override
    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {

        SendTask sendTask = (SendTask) element;

        if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equals(sendTask.getImplementationType())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_CLASS, sendTask.getImplementation(), xtw);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equals(sendTask.getImplementationType())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_EXPRESSION, sendTask.getImplementation(), xtw);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equals(sendTask.getImplementationType())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION, sendTask.getImplementation(), xtw);
        }

        if (StringUtils.isNotEmpty(sendTask.getResultVariableName())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_RESULTVARIABLE, sendTask.getResultVariableName(), xtw);
        }
        if (StringUtils.isNotEmpty(sendTask.getType())) {
            writeQualifiedAttribute(ATTRIBUTE_TYPE, sendTask.getType(), xtw);
        }
        if (StringUtils.isNotEmpty(sendTask.getExtensionId())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_EXTENSIONID, sendTask.getExtensionId(), xtw);
        }
        if (StringUtils.isNotEmpty(sendTask.getSkipExpression())) {
            writeQualifiedAttribute(ATTRIBUTE_TASK_SERVICE_SKIP_EXPRESSION, sendTask.getSkipExpression(), xtw);
        }
    }

    @Override
    protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw)
            throws Exception {
        SendTask sendTask = (SendTask) element;

        if (!sendTask.getCustomProperties().isEmpty()) {
            for (CustomProperty customProperty : sendTask.getCustomProperties()) {

                if (StringUtils.isEmpty(customProperty.getSimpleValue())) {
                    continue;
                }

                if (didWriteExtensionStartElement == false) {
                    xtw.writeStartElement(ELEMENT_EXTENSIONS);
                    didWriteExtensionStartElement = true;
                }
                xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, ELEMENT_FIELD, ACTIVITI_EXTENSIONS_NAMESPACE);
                xtw.writeAttribute(ATTRIBUTE_FIELD_NAME, customProperty.getName());
                if ((customProperty.getSimpleValue().contains("${") || customProperty.getSimpleValue().contains("#{"))
                        && customProperty.getSimpleValue().contains("}")) {

                    xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, ATTRIBUTE_FIELD_EXPRESSION, ACTIVITI_EXTENSIONS_NAMESPACE);
                } else {
                    xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, ELEMENT_FIELD_STRING, ACTIVITI_EXTENSIONS_NAMESPACE);
                }
                xtw.writeCData(customProperty.getSimpleValue());
                xtw.writeEndElement();
                xtw.writeEndElement();
            }
        } else {
            didWriteExtensionStartElement = FieldExtensionExport.writeFieldExtensions(sendTask.getFieldExtensions(),
                    didWriteExtensionStartElement, xtw);
        }

        return didWriteExtensionStartElement;
    }

    @Override
    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
    }

    @Override
    protected String parseOperationRef(String operationRef, BpmnModel model) {
        String result = null;
        if (StringUtils.isNotEmpty(operationRef)) {
            int indexOfP = operationRef.indexOf(':');
            if (indexOfP != -1) {
                String prefix = operationRef.substring(0, indexOfP);
                String resolvedNamespace = model.getNamespace(prefix);
                result = resolvedNamespace + ":" + operationRef.substring(indexOfP + 1);
            } else {
                result = model.getTargetNamespace() + ":" + operationRef;
            }
        }
        return result;
    }
}
