package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link Action}.
 * @author @essar
 */
public class ActionTests
{
    
    @Test
    public void testGetActionSequenceReturnsExpectedInteger() {

        Action<?> testAction = new TestAction();
        assertEquals(1, testAction.getActionSequence(), "ActionSequence is expected value");

    }
    
    @Test
    public void testGetPlayerIDReturnsExpectedString() {

        Action<?> testAction = new TestAction();
        assertEquals("test-player", testAction.getPlayerID(), "PlayerID is expected value");

    }

    @Test
    public void testGetRequestIDReturnsExpectedInteger() {

        Action<?> testAction = new TestAction();
        assertEquals("test-request", testAction.getRequestID(), "RequestID is expected value");

    }

    private static class TestAction extends Action<String>
    {
        private TestAction() {

            super("test-request", 1, "test-player");

        }

        @Override
        public String getResult() {
            return "test-result";
        }

        @Override
        public void runAction(Player player) {
            
            // Do nothing

        }
    }
}
