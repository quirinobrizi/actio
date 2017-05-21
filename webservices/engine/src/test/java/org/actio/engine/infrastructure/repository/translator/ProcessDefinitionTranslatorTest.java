package org.actio.engine.infrastructure.repository.translator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.infrastructure.repository.translator.ExecutionEntityTranslator;
import org.actio.engine.infrastructure.repository.translator.JobEntityTranslator;
import org.actio.engine.infrastructure.repository.translator.ProcessDefinitionTranslator;
import org.actio.engine.infrastructure.repository.translator.TaskEntityTranslator;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessDefinitionTranslatorTest {

    @Mock
    private RuntimeService runtimeService;
    @Mock
    private RepositoryService repositoryService;
    @Mock
    private HistoryService historyService;
    @Mock
    private JobEntityTranslator jobEntityTranslator;
    @Mock
    private TaskEntityTranslator taskEntityTranslator;

    @InjectMocks
    private ProcessDefinitionTranslator testObj;

    @Mock
    private ModelQuery modelQuery;
    @Mock
    private ProcessInstanceQuery processInstanceQuery;
    @Mock
    private HistoricProcessInstanceQuery historicProcessInstanceQuery;
    @Mock
    private ProcessDefinition processDefinition1;
    @Mock
    private ProcessDefinition processDefinition2;
    @Mock
    private ProcessDefinition processDefinition3;
    @Mock
    private Model model;
    @Mock
    private ExecutionEntity processInstance;
    @Mock
    private ExecutionEntityTranslator executionEntityTranslator;
    @Mock
    private HistoricProcessInstance historicProcessInstance;

    private String key1 = "key1";
    private String key2 = "key2";

    @Mock
    private Instance instance;

    @Test
    public void testGetAll() {
        List<ProcessDefinition> processDefinitions = Arrays.asList(processDefinition1, processDefinition2, processDefinition3);

        when(repositoryService.createModelQuery()).thenReturn(modelQuery);
        when(modelQuery.modelKey(anyString())).thenReturn(modelQuery);
        when(modelQuery.orderByModelVersion()).thenReturn(modelQuery);
        when(modelQuery.desc()).thenReturn(modelQuery);
        when(modelQuery.singleResult()).thenReturn(model);
        when(model.getId()).thenReturn("123");
        when(model.hasEditorSource()).thenReturn(true);
        when(repositoryService.getModelEditorSource(anyString())).thenReturn(new byte[] {});
        when(model.hasEditorSourceExtra()).thenReturn(true);
        when(repositoryService.getModelEditorSourceExtra(anyString())).thenReturn(new byte[] {});
        when(runtimeService.createProcessInstanceQuery()).thenReturn(processInstanceQuery);
        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(historicProcessInstanceQuery);

        when(historicProcessInstanceQuery.includeProcessVariables()).thenReturn(historicProcessInstanceQuery);
        when(historicProcessInstanceQuery.finished()).thenReturn(historicProcessInstanceQuery);
        when(historicProcessInstanceQuery.processDefinitionId(anyString())).thenReturn(historicProcessInstanceQuery);
        List<HistoricProcessInstance> historicProcessIntances = Arrays.asList(historicProcessInstance);
        when(historicProcessInstanceQuery.list()).thenReturn(historicProcessIntances);
        when(historicProcessInstance.getId()).thenReturn("hpi_id");

        List<ProcessInstance> processInstances = Arrays.asList(processInstance);
        when(processInstanceQuery.includeProcessVariables()).thenReturn(processInstanceQuery);
        when(processInstanceQuery.processDefinitionId(anyString())).thenReturn(processInstanceQuery);
        when(processInstanceQuery.list()).thenReturn(processInstances);
        when(processInstance.getId()).thenReturn("piid");
        when(processInstance.getProcessVariables()).thenReturn(new HashMap<>());
        when(processInstance.isEnded()).thenReturn(false);
        when(processInstance.isSuspended()).thenReturn(false);

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

        when(executionEntityTranslator.translate(processInstance)).thenReturn(instance);

        // act
        List<Bpmn> actual = testObj.translate(processDefinitions);
        // assert
        assertEquals(2, actual.size());

        Bpmn bpmn1 = actual.get(0);
        Bpmn bpmn2 = actual.get(1);
        assertEquals(2, bpmn1.getVersions().size());
        assertEquals(1, bpmn2.getVersions().size());
        assertEquals("key1", bpmn1.getId());
        assertEquals("key2", bpmn2.getId());
    }

}
