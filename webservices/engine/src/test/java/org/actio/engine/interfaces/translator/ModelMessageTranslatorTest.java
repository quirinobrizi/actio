package org.actio.engine.interfaces.translator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.actio.commons.message.model.ModelMessage;
import org.activiti.engine.repository.Model;
import org.junit.Test;

public class ModelMessageTranslatorTest {

    private ModelMessageTranslator testObj = new ModelMessageTranslator();
    private Model model = mock(Model.class);

    @Test
    public void testTranslate() {
        when(model.getId()).thenReturn("id");
        when(model.getName()).thenReturn("name");
        when(model.getKey()).thenReturn("key");
        when(model.getCategory()).thenReturn("category");
        when(model.getVersion()).thenReturn(10);
        when(model.getMetaInfo()).thenReturn("metaInfo");
        when(model.getDeploymentId()).thenReturn("deploymentId");
        when(model.getTenantId()).thenReturn("tennantId");
        // act
        ModelMessage actual = testObj.translate(model, "definition", "svg");
        // assert
        assertEquals("id", actual.getId());
        assertEquals("name", actual.getName());
        assertEquals("key", actual.getKey());
        assertEquals("category", actual.getCategory());
        assertEquals(10, actual.getVersion().intValue());
        assertEquals("metaInfo", actual.getMetaInfo());
        assertEquals("deploymentId", actual.getDeploymentId());
        assertEquals("tennantId", actual.getTenantId());
        assertEquals("definition", actual.getDefinition());
        assertEquals("svg", actual.getSvg());

    }

}
