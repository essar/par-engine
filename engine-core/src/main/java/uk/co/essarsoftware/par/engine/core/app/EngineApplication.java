package uk.co.essarsoftware.par.engine.core.app;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.core.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.core.events.TaskFactory;
import uk.co.essarsoftware.par.game.Game;
import uk.co.essarsoftware.par.game.PlayerList;

@SpringBootApplication
//@EnableAsync
public class EngineApplication
{

    
    @Bean
    public EngineEventQueue initEngineEventQueue() {

        return new EngineEventQueue();
        
    }

    @Bean
    public DiscardPile initDiscardPile() {

        return new DiscardPile();

    }

    @Bean
    public DrawPile initDrawPile() {

        return new DrawPile();
        
    }

    @Bean
    public Game initGame() {

        return new Game(UUID.randomUUID().toString());
        
    }

    @Bean
    public PlayerList initPlayerList() {

        return new PlayerList();
        
    }

    @Bean
    public TaskFactory initTaskFactory() {

        return new TaskFactory();

    }

    public static void main(String[] args) {

        // Run application
        SpringApplication.run(EngineApplication.class, args);

    }
}
