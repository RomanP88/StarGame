package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;

public class PowerUps implements Poolable {
    public enum Type {
        MEDKIT(0), MONEY(1), AMMOS(2);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private Vector2 position;
    private Vector2 velocity;
    private Type type;
    private Circle hitArea;
    private boolean active;
    private TextureRegion texture;
    private float time;

    public PowerUps() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hitArea = new Circle(position, 16);
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public float getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate(){
        active = false;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - 30, position.y - 30, 32, 32);
    }

    public void activate(float x, float y, Type type){
        this.type = type;
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        this.velocity.nor().scl(50.0f);
        this.active = true;
        this.time = 0.0f;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        time += dt;
        if (time >= 7.0f) {
            deactivate();
        }
    }

}
