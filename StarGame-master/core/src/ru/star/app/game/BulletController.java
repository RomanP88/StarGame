package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.star.app.game.Weapon.WeaponOwner;
import ru.star.app.game.helpers.ObjectPool;
import ru.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion bulletTexture;
    private GameController gc;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController(GameController gc) {
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size() ; i++) {
            Bullet bullet = activeList.get(i);
            batch.draw(bulletTexture,
                    bullet.getPosition().x - 16,
                    bullet.getPosition().y - 16);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size() ; i++) {
            Bullet bullet = activeList.get(i);
            bullet.update(dt);
            float bx = bullet.getPosition().x;
            float by = bullet.getPosition().y;
            for (int j = 0; j < 3; j++) {
                gc.getParticleController().getEffectBuilder().createBulletTrace(bullet);
            }
        }
        checkPool();
    }

    public void setup(float x, float y, float vx, float vy, WeaponOwner owner, int damage) {
        getActiveElement().activate(x, y, vx, vy, owner, damage);
    }
}
