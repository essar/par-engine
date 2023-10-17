package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test cases for {@link InfoController}.
 * @author @essar
 */
@ExtendWith(SpringExtension.class)
public class InfoControllerTests
{

    private InfoController underTest;

    @BeforeEach
    public void initInfoController() {

        underTest = new InfoController();
        
    }

    @Test
    public void testGetHealthReturnsStatusOK() {

        Map<String, String> response = underTest.getHealth().block();
        assertEquals("OK", response.get("status"), "Expected status OK");

    }

    @Test
    public void testGetHealthReturnsDefaultVersion() {

        Map<String, String> response =  underTest.getVersion().block();
        assertEquals("1.0.0", response.get("version"), "Expected default version");
        
    }
}
