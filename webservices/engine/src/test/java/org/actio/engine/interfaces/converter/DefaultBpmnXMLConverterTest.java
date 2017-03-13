package org.actio.engine.interfaces.converter;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.model.BaseElement;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class DefaultBpmnXMLConverterTest {

    private DefaultBpmnXMLConverter testObj = new DefaultBpmnXMLConverter();

    @SuppressWarnings({ "unchecked" })
    @Test
    public void test_contains_actio_SendTaskXmlConverter() {
        Map<String, BaseBpmnXMLConverter> convertersToBpmnMap = (Map<String, BaseBpmnXMLConverter>) ReflectionTestUtils.getField(testObj,
                "convertersToBpmnMap");

        Map<Class<? extends BaseElement>, BaseBpmnXMLConverter> convertersToXMLMap = (Map<Class<? extends BaseElement>, BaseBpmnXMLConverter>) ReflectionTestUtils
                .getField(testObj, "convertersToXMLMap");
        SendTaskXmlConverter converter = new SendTaskXmlConverter();
        // assert
        assertTrue(convertersToBpmnMap.containsKey("sendTask"));
        assertTrue(convertersToXMLMap.containsKey(converter.getBpmnElementType()));
    }

}
