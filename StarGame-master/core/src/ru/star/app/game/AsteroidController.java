package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private static final int BREAK_ASTEROID_COUNT = 3;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void setup(float x, float y, float vx, float vy, float scale, int level) {
        getActiveElement().activate(x, y, vx, vy, scale, level);
    }

    public void breakAsteroid(float x, float y, float scale, int level) {
        for (int i = 0; i < BREAK_ASTEROID_COUNT; i++) {

            setup(x, y,
                    MathUtils.random(-150, 150),
                    MathUtils.random(-150, 150), scale, level
            );
        }

    }
}
