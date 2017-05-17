package org.actio.engine.infrastructure.activiti.translator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.actio.engine.domain.model.bpmn.process.Instance;
import org.actio.engine.domain.model.bpmn.process.InstanceState;
import org.actio.engine.infrastructure.activiti.translator.ExecutionEntityTranslator;
import org.actio.engine.infrastructure.activiti.translator.JobEntityTranslator;
import org.actio.engine.infrastructure.activiti.translator.TaskEntityTranslator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionEntityTranslatorTest {

    @Mock
    private JobEntityTranslator jobEntityTranslator;
    @Mock
    private TaskEntityTranslator taskEntityTranslator;

    @InjectMocks
    private ExecutionEntityTranslator testObj;

    @Mock
    private ExecutionEntity entity;
    @Mock
    private ExecutionEntity entity2;
    @Mock
    private ExecutionEntity entity3;

    @Test
    public void testTranslateCollectionOfExecutionEntity() {
        Map<String, Object> variables = new HashMap<>();
        Date now = new Date();
        ArrayList<JobEntity> jobs = new ArrayList<>();
        ArrayList<TaskEntity> tasks = new ArrayList<>();

        when(entity.getId()).thenReturn("id");
        when(entity.getProcessVariables()).thenReturn(variables);
        when(entity.isEnded()).thenReturn(true);
        when(entity.isSuspended()).thenReturn(false);
        when(entity.getLockTime()).thenReturn(now);
        when(entity.getJobs()).thenReturn(jobs);
        when(entity.getTasks()).thenReturn(tasks);

        when(entity2.getId()).thenReturn("id2");
        when(entity2.getProcessVariables()).thenReturn(variables);
        when(entity2.isEnded()).thenReturn(false);
        when(entity2.isSuspended()).thenReturn(true);
        when(entity2.getLockTime()).thenReturn(now);
        when(entity2.getJobs()).thenReturn(jobs);
        when(entity2.getTasks()).thenReturn(tasks);

        when(entity3.getId()).thenReturn("id3");
        when(entity3.getProcessVariables()).thenReturn(variables);
        when(entity3.isEnded()).thenReturn(false);
        when(entity3.isSuspended()).thenReturn(false);
        when(entity3.getLockTime()).thenReturn(now);
        when(entity3.getJobs()).thenReturn(jobs);
        when(entity3.getTasks()).thenReturn(tasks);

        // act
        Collection<Instance> actual = testObj.translate(Arrays.asList(entity, entity2, entity3));
        // verify
        verify(jobEntityTranslator, times(3)).translate(jobs);
        verify(taskEntityTranslator, times(3)).translate(tasks);
        // assert
        assertEquals(3, actual.size());
        for (Instance instance : actual) {
            if ("id".equals(instance.getInstanceId())) {
                assertEquals(InstanceState.TERMINATED, instance.getInstanceState());
            }
            if ("id2".equals(instance.getInstanceId())) {
                assertEquals(InstanceState.SUSPENDED, instance.getInstanceState());
            }
            if ("id3".equals(instance.getInstanceId())) {
                assertEquals(InstanceState.ACTIVE, instance.getInstanceState());
            }
            assertEquals(now, instance.getStartDate());
            assertEquals(jobs, new ArrayList<>(instance.getJobs()));
            assertEquals(tasks, new ArrayList<>(instance.getTasks()));
            assertEquals(variables, instance.getVariables());
        }
    }

}
