package org.actio.engine.infrastructure.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.infrastructure.repository.BpmnRepositoryImpl;
import org.actio.engine.infrastructure.repository.jpa.ErrorEventRepository;
import org.actio.engine.infrastructure.repository.storable.ErrorEventStorable;
import org.actio.engine.infrastructure.repository.translator.ErrorEventStorableTranslator;
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
    @Mock
    private ErrorEventRepository errorEventRepository;
    @Mock
    private ErrorEventStorableTranslator errorEventStorableTranslator;

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
    @Mock
    private Bpmn bpmn;

    @Test
    public void testGet() {
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.processDefinitionKey(anyString())).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.latestVersion()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.singleResult()).thenReturn(processDefinition1);
        when(processDefinitionTranslator.translate(processDefinition1)).thenReturn(bpmn);
        ArrayList<ErrorEventStorable> errors = new ArrayList<>();
        when(errorEventRepository.findAllByProcessDefinitionKey(anyString())).thenReturn(errors);
        when(errorEventStorableTranslator.translate(errors)).thenReturn(new HashSet<>());
        // act
        Bpmn actual = testObj.get(BpmnId.newInstance("bpmn-id"));
        // verify
        verify(processDefinitionQuery).processDefinitionKey("bpmn-id");
        // assert
        assertEquals(bpmn, actual);
    }

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
