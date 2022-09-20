package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.star.app.game.helpers.ObjectPool;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class BotController extends ObjectPool<Bot> {
    private GameController gc;

    public BotController(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected Bot newObject() {
        return new Bot(gc);
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size() ; i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void setup(){
        getActiveElement().activate(MathUtils.random(0, SCREEN_WIDTH),
                MathUtils.random(0, SCREEN_HEIGHT),
                MathUtils.random(-150, 150), MathUtils.random(-150, 150), gc.getLevel());
        System.out.println("Bots count: " + activeList.size());
    }
}
