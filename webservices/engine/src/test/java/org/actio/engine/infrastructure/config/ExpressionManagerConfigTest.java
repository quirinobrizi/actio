package org.actio.engine.infrastructure.config;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.activiti.engine.impl.javax.el.CompositeELResolver;
import org.activiti.engine.impl.javax.el.ELResolver;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ExpressionManagerConfigTest {

    private ExpressionManagerConfig testObj = new ExpressionManagerConfig(null, null);

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateElResolverVariableScope() {
        // act
        CompositeELResolver actual = (CompositeELResolver) testObj.createElResolver(null);
        // assert
        List<ELResolver> resolvers = (List<ELResolver>) ReflectionTestUtils.getField(actual, "resolvers");
        assertEquals(7, resolvers.size());
    }

}
