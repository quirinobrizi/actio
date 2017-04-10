package org.actio.engine.infrastructure.repository;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
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
    private RuntimeService runtimeService;
    @Mock
    private RepositoryService repositoryService;

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

    private String key1 = "key1";
    private String key2 = "key2";

    @Test
    public void testGetAll() {
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.orderByProcessDefinitionKey()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.asc()).thenReturn(processDefinitionQuery);
        List<ProcessDefinition> processDefinitions = Arrays.asList(processDefinition1, processDefinition2, processDefinition3);
        when(processDefinitionQuery.list()).thenReturn(processDefinitions);
        when(processDefinition1.getKey()).thenReturn(key1);
        when(processDefinition1.getName()).thenReturn("name");
        when(processDefinition2.getKey()).thenReturn(key1);
        when(processDefinition2.getName()).thenReturn("name");
        when(processDefinition3.getKey()).thenReturn(key2);
        when(processDefinition3.getName()).thenReturn("name");
    }

}
