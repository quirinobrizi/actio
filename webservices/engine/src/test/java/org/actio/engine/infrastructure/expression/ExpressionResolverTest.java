package org.actio.engine.infrastructure.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.javax.el.ELContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionResolverTest {

    @Mock
    private VariableScope variableScope;
    @Mock
    private ELContext context;
    private ExpressionResolver testObj;

    @Before
    public void before() {
        testObj = new ExpressionResolver(variableScope);
    }

    @Test
    public void testGetValueELContextObjectObject() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${abc}");
        when(variableScope.getVariable("abc", true)).thenReturn(10);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(10, actual);
    }

    @Test
    public void testGetValueELContext_no_expression_or_template() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn(20);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(20, actual);
    }

    @Test
    public void testGetValueELContext_no_expression_or_template_string() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("abcdfe ggg");

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals("abcdfe ggg", actual);
    }

    @Test
    public void testGetValueELContext_no_expression_or_template_null() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn(null);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertNull(actual);
    }

    @Test
    public void testGetValueELContext_nested_variables() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.c}");
        Map<String, Object> values = new HashMap<>();
        values.put("c", 10);
        when(variableScope.getVariable("ab", true)).thenReturn(values);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(10, actual);
    }

    @Test
    public void testGetValueELContext_nested_primitive() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.c}");
        when(variableScope.getVariable("ab", true)).thenReturn(10);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(10, actual);
    }

    @Test
    public void testGetValueELContext_nested_collection() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.p}");
        when(variableScope.getVariable("ab", true)).thenReturn(Arrays.asList(""));

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(Arrays.asList(""), actual);
    }

    @Test
    public void testGetValueELContext_nested_null_obj() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.p}");
        when(variableScope.getVariable("ab", true)).thenReturn(null);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertNull(actual);
    }

    @Test
    public void testGetValueELContext_nested_object() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.p}");
        when(variableScope.getVariable("ab", true)).thenReturn(new Obj());

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals("value", actual);
    }

    @Test
    public void testGetValueELContext_nested_object_field_not_found() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("${ab.c}");
        Obj obj = new Obj();
        when(variableScope.getVariable("ab", true)).thenReturn(obj);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals(obj, actual);
    }

    @Test
    public void testGetValueELContext_template() {
        when(variableScope.hasVariable("property")).thenReturn(true);
        when(variableScope.getVariable("property")).thenReturn("ab ${c}");
        Map<String, Object> values = new HashMap<>();
        values.put("c", 10);
        when(variableScope.getVariables()).thenReturn(values);

        // act
        Object actual = testObj.getValue(context, null, "property");
        // assert
        assertEquals("ab 10", actual);
    }

    class Obj {
        private String p = "value";
    }
}
