package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.star.app.game.PowerUps.Type;
import ru.star.app.game.helpers.ObjectPool;
import ru.star.app.screen.utils.Assets;

public class PowerUpsController extends ObjectPool<PowerUps> {
    private TextureRegion[][] textures;

    public PowerUpsController() {
        this.textures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("powerups"))
                .split(60, 60);

    }
    @Override
    protected PowerUps newObject() {
        return new PowerUps();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            PowerUps p = activeList.get(i);
            int frameIndex = (int)(p.getTime() / 0.1f) % textures[p.getType().index].length;
            batch.draw(textures[p.getType().index][frameIndex], p.getPosition().x - 30, p.getPosition().y - 30);
        }
    }

    public void setup(float x, float y, float probability) {
        if (MathUtils.random() <= probability) {
            getActiveElement().activate(x, y, Type.values()[MathUtils.random(0, 2)]);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
