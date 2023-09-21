package uk.co.essarsoftware.par.engine.core.app;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.core.tasks.TaskFactory;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.plays.PlaySet;

@SpringBootApplication
@ComponentScan(basePackages = {
    "uk.co.essarsoftware.par.engine.actions",
    "uk.co.essarsoftware.par.engine.core.app",
    "uk.co.essarsoftware.par.engine.events",
    "uk.co.essarsoftware.par.engine.players"
})
//@EnableAsync
public class EngineApplication
{

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
    public PlaySet initPlaySet() {

        return new PlaySet();
        
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
