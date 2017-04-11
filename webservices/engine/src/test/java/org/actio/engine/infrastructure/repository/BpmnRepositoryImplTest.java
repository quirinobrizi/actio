package org.actio.engine.infrastructure.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.infrastructure.repository.translator.ProcessDefinitionTranslator;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BpmnRepositoryImplTest {

    @Mock
    private RepositoryService repositoryService;
    @Mock
    private ProcessDefinitionTranslator processDefinitionTranslator;

    @InjectMocks
    private BpmnRepositoryImpl testObj;

    @Mock
    private ProcessDefinitionQuery processDefinitionQuery;
    @Mock
    private ProcessDefinition processDefinition1;
    @Mock
    private ProcessDefinition processDefinition2;
    @Mock
    private ProcessDefinition processDefinition3;

    @Test
    public void testGetAll() {
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.orderByProcessDefinitionKey()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.asc()).thenReturn(processDefinitionQuery);
        List<ProcessDefinition> processDefinitions = Arrays.asList(processDefinition1, processDefinition2, processDefinition3);
        when(processDefinitionQuery.list()).thenReturn(processDefinitions);
        List<Bpmn> bpmns = new ArrayList<>();
        when(processDefinitionTranslator.translate(processDefinitions)).thenReturn(bpmns);
        // act
        List<Bpmn> actual = testObj.getAll();
        // assert
        assertEquals(bpmns, actual);
    }

}
