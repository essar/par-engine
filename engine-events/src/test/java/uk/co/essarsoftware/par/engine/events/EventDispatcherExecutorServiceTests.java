package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link EventDispatcherExecutorService}.
 * @author @essar
 */
public class EventDispatcherExecutorServiceTests
{

    private ExecutorService mockExecutor;

    @BeforeEach
    public void mockExecutor() {

        mockExecutor = mock(ExecutorService.class);

    }

    @Test
    public void testCreateEventDispatcherServiceWithDefailtExecutor() {

        EventDispatcherExecutorService service = new EventDispatcherExecutorService();
        assertNotNull(service, "Expected service to be instantiated");
        
    }

    @Test
    public void testIsShutdownFalseWhenExecutorIsRunning() {

        when(mockExecutor.isShutdown()).thenReturn(false);
        EventDispatcherExecutorService service = new EventDispatcherExecutorService(mockExecutor);
        assertFalse(service.isShutdown(), "Expected service to not be shutdown");
        
    }

    @Test
    public void testIsShutdownTrueWhenExecutorIsShutdown() {

        when(mockExecutor.isShutdown()).thenReturn(true);
        EventDispatcherExecutorService service = new EventDispatcherExecutorService(mockExecutor);
        assertTrue(service.isShutdown(), "Expected service to be shutdown");
        
    }

    @Test
    public void testIsShutdownCallsIsShutdown() {

        EventDispatcherExecutorService service = new EventDispatcherExecutorService(mockExecutor);
        service.isShutdown();
        verify(mockExecutor).isShutdown();
        
    }
 
    @Test
    public void testStartDispatcherCallsSubmit() {

        Runnable dispatcherProcess = () -> {};
        EventDispatcherExecutorService service = new EventDispatcherExecutorService(mockExecutor);
        service.startDispatcher(dispatcherProcess);
        verify(mockExecutor).submit(dispatcherProcess);

    }

    @Test
    public void testStopDispatcherCallsShutdown() {

        EventDispatcherExecutorService service = new EventDispatcherExecutorService(mockExecutor);
        service.stopDispatcher();
        verify(mockExecutor).shutdown();

    }
}
