package engine;

import engine.input.special.Keyboard;
import engine.input.special.Mouse;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public interface IGameLogic<AgentId>
{
    public void update(GameEngine<AgentId> ge, Mouse mouse, Keyboard keyboard);
}
