package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link EventProcessorExecutorService}.
 * @author @essar
 */
public class EventProcessorExecutorServiceTests
{

    private ExecutorService mockExecutor;

    @BeforeEach
    public void mockExecutor() {

        mockExecutor = mock(ExecutorService.class);

    }

    @Test
    public void testCreateEventProcessorServiceWithDefaultExecutor() {

        EventProcessorExecutorService service = new EventProcessorExecutorService();
        assertNotNull(service, "Expected service to be instantiated");
        
    }

    @Test
    public void testIsShutdownFalseWhenExecutorIsRunning() {

        when(mockExecutor.isShutdown()).thenReturn(false);
        EventProcessorExecutorService service = new EventProcessorExecutorService(mockExecutor);
        assertFalse(service.isShutdown(), "Expected service to not be shutdown");
        
    }

    @Test
    public void testIsShutdownTrueWhenExecutorIsShutdown() {

        when(mockExecutor.isShutdown()).thenReturn(true);
        EventProcessorExecutorService service = new EventProcessorExecutorService(mockExecutor);
        assertTrue(service.isShutdown(), "Expected service to be shutdown");
        
    }

    @Test
    public void testIsShutdownCallsIsShutdown() {

        EventProcessorExecutorService service = new EventProcessorExecutorService(mockExecutor);
        service.isShutdown();
        verify(mockExecutor).isShutdown();
        
    }
 
    @Test
    public void testExecuteCallsExecute() {

        EventProcessorExecutorService service = new EventProcessorExecutorService(mockExecutor);
        service.execute(() -> {});
        verify(mockExecutor).execute(any(Runnable.class));

    }

    @Test
    public void testStopProcessorCallsShutdown() {

        EventProcessorExecutorService service = new EventProcessorExecutorService(mockExecutor);
        service.stopProcessor();
        verify(mockExecutor).shutdown();

    }
}
