package uk.co.essarsoftware.par.engine.core.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import uk.co.essarsoftware.par.engine.core.app.CardsService;
import uk.co.essarsoftware.par.engine.core.app.players.PlayersService;

@Service
public class TaskFactory
{

    @Autowired
    private CardsService cards;
   
    @Autowired
    private PlayersService players;

    @Bean
    public StartRoundTask startRoundTask() {

        return new StartRoundTask(cards, players);
        //return new StartRoundTask();

    }
}
