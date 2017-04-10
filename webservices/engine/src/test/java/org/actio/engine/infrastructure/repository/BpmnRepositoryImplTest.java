package org.actio.engine.infrastructure.repository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
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
    private ModelQuery modelQuery;
    @Mock
    private ProcessDefinition processDefinition1;
    @Mock
    private ProcessDefinition processDefinition2;
    @Mock
    private ProcessDefinition processDefinition3;
    @Mock
    private Model model;

    private String key1 = "key1";
    private String key2 = "key2";

    @Test
    public void testGetAll() {
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.orderByProcessDefinitionKey()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.asc()).thenReturn(processDefinitionQuery);
        List<ProcessDefinition> processDefinitions = Arrays.asList(processDefinition1, processDefinition2, processDefinition3);
        when(processDefinitionQuery.list()).thenReturn(processDefinitions);
        when(repositoryService.createModelQuery()).thenReturn(modelQuery);
        when(modelQuery.modelKey(anyString())).thenReturn(modelQuery);
        when(modelQuery.singleResult()).thenReturn(model);
        when(model.hasEditorSource()).thenReturn(true);
        when(repositoryService.getModelEditorSource(anyString())).thenReturn(new byte[] {});
        when(model.hasEditorSourceExtra()).thenReturn(true);
        when(repositoryService.getModelEditorSourceExtra(anyString())).thenReturn(new byte[] {});

        when(processDefinition1.getId()).thenReturn("1");
        when(processDefinition1.getKey()).thenReturn(key1);
        when(processDefinition1.getName()).thenReturn("name");
        when(processDefinition1.getVersion()).thenReturn(1);

        when(processDefinition2.getId()).thenReturn("2");
        when(processDefinition2.getKey()).thenReturn(key1);
        when(processDefinition2.getName()).thenReturn("name");
        when(processDefinition2.getVersion()).thenReturn(2);

        when(processDefinition3.getId()).thenReturn("3");
        when(processDefinition3.getKey()).thenReturn(key2);
        when(processDefinition3.getName()).thenReturn("name");
        when(processDefinition3.getVersion()).thenReturn(1);
    }

}
