package org.actio.engine.interfaces.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;

import org.actio.engine.domain.model.activities.SendTask;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ImplementationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SendTaskXmlConverterTest {

    @InjectMocks
    private SendTaskXmlConverter testObj;
    @Mock
    private XMLStreamReader xtr;
    @Mock
    private Location location;

    @Test
    public void testConvertXMLToElement_type() throws Exception {
        BpmnModel model = new BpmnModel();
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn("mail");
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("mail", actual.getType());
    }

    @Test
    public void testConvertXMLToElement_class() throws Exception {
        BpmnModel model = new BpmnModel();
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn("org.actio.clazz");

        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("org.actio.clazz", actual.getImplementation());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_CLASS, actual.getImplementationType());
    }

    @Test
    public void testConvertXMLToElement_expression() throws Exception {
        BpmnModel model = new BpmnModel();
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION))
                .thenReturn("#{a.b()}");
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_RESULTVARIABLE))
                .thenReturn("result");
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_SKIP_EXPRESSION))
                .thenReturn("skipExpression");
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("#{a.b()}", actual.getImplementation());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION, actual.getImplementationType());
        assertEquals("result", actual.getResultVariableName());
        assertEquals("skipExpression", actual.getSkipExpression());
    }

    @Test
    public void testConvertXMLToElement_delegateExpression() throws Exception {
        BpmnModel model = new BpmnModel();
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE,
                BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION)).thenReturn("delegateExpression");
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("delegateExpression", actual.getImplementation());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION, actual.getImplementationType());
    }

    @Test
    public void testConvertXMLToElement_webservice() throws Exception {
        BpmnModel model = new BpmnModel();
        model.addNamespace("abc", "http://abc.com");
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE,
                BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION)).thenReturn(null);
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_IMPLEMENTATION)).thenReturn("##WebService");
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_OPERATION_REF)).thenReturn("abc:opref");
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("http://abc.com:opref", actual.getOperationRef());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE, actual.getImplementationType());
    }

    @Test
    public void testConvertXMLToElement_webservice_opref_no_prefix() throws Exception {
        BpmnModel model = new BpmnModel();
        model.addNamespace("abc", "http://abc.com");
        model.setTargetNamespace("http://target");
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE,
                BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION)).thenReturn(null);
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_IMPLEMENTATION)).thenReturn("##WebService");
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_OPERATION_REF)).thenReturn("opref");
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertEquals("http://target:opref", actual.getOperationRef());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE, actual.getImplementationType());
    }

    @Test
    public void testConvertXMLToElement_webservice_opref_null() throws Exception {
        BpmnModel model = new BpmnModel();
        model.addNamespace("abc", "http://abc.com");
        model.setTargetNamespace("http://target");
        when(xtr.getLocation()).thenReturn(location);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TYPE)).thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_CLASS))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE, BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION))
                .thenReturn(null);
        when(xtr.getAttributeValue(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE,
                BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_DELEGATEEXPRESSION)).thenReturn(null);
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_IMPLEMENTATION)).thenReturn("##WebService");
        when(xtr.getAttributeValue(null, BpmnXMLConstants.ATTRIBUTE_TASK_OPERATION_REF)).thenReturn(null);
        // act
        SendTask actual = (SendTask) testObj.convertXMLToElement(xtr, model);
        // assert
        assertNull(actual.getOperationRef());
        assertEquals(ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE, actual.getImplementationType());
    }

}
