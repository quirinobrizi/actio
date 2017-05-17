package org.actio.engine.infrastructure.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.actio.engine.infrastructure.activiti.command.StartProcessInstanceCommand;
import org.actio.engine.infrastructure.activiti.translator.ExecutionEntityTranslator;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartProcessInstanceTest {

    @Mock
    private RuntimeServiceImpl runtimeService;

    @Mock
    private ExecutionEntityTranslator executionEntityTranslator;

    @InjectMocks
    private StartProcessInstance testObj;

    @Mock
    private CommandExecutor commandExecutor;

    @Mock
    private ExecutionEntity processInstance;

    @SuppressWarnings("unchecked")
    @Test
    public void testExecute() {
        when(runtimeService.getCommandExecutor()).thenReturn(commandExecutor);
        when(commandExecutor.execute(any(StartProcessInstanceCommand.class))).thenReturn(processInstance);

        Map<String, Object> inputs = new HashMap<>();
        // act
        ExecutionEntity actual = testObj.execute("bpmnid", inputs);
        // verify
        verify(commandExecutor).execute(any(StartProcessInstanceCommand.class));
        // assert
        assertEquals(processInstance, actual);
    }

    @Test
    public void testType() {
        assertEquals(Command.Type.START_PROCESS_INSTANCE, testObj.type());
    }

}
