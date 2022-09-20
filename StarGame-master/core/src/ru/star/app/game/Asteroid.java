package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Asteroid implements Poolable {
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int hpMax;
    private int hp;
    private float angle;
    private float rotationSpeed;
    private Circle hitArea;
    private float scale;
    private int damage;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public int getDamage() {
        return damage;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Asteroid() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hitArea = new Circle();
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public void activate(float x, float y, float vx, float vy, float scale, int level) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.angle = MathUtils.random(0, 360);
        this.active = true;
        this.hpMax = (int) (10 * scale) + level;
        this.damage = level + (int) (scale * level);
        this.hp = hpMax;
        this.scale = scale;
        this.hitArea.setPosition(x, y);
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
        this.rotationSpeed = MathUtils.random(-180, 180);
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
        checkBorder();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, scale, scale, angle);
    }

    private void checkBorder() {
        if (position.x < -hitArea.radius) {
            position.x = SCREEN_WIDTH + hitArea.radius;
        } else if (position.x > SCREEN_WIDTH + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if (position.y < -hitArea.radius) {
            position.y = SCREEN_HEIGHT + hitArea.radius;
        } else if (position.y > SCREEN_HEIGHT + hitArea.radius) {
            position.y = -hitArea.radius;
        }
        hitArea.setPosition(position);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            return true;
        } else {
            return false;
        }
    }

}
