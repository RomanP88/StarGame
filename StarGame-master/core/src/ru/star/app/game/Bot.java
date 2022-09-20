package ru.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Bot extends Ship implements Poolable {
    private boolean active;
    private final float ATTACK_RADIUS = 400.0f;
    private Circle attackArea;
    private Vector2 tempVec;

    public Bot(GameController gc) {
        super(gc, Weapon.WeaponOwner.BOT);
        this.texture = Assets.getInstance().getAtlas().findRegion("bot");
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hpMax = 10;
        this.hp = hpMax;
        this.hitArea = new Circle(position, 28.0f);
        this.attackArea = new Circle(position, ATTACK_RADIUS);
        this.tempVec = new Vector2();
    }

    public void setAngle(float x, float y){
        this.angle = (float) Math.toDegrees((Math.atan2(y, x)));
    }

    public Circle getAttackArea() {
        return attackArea;
    }

    @Override
    public boolean isActive() {
        return active;
    }
    public void activate(float x, float y, float vx, float vy, int level) {
        System.out.println("Bot activate");
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.hp = this.hpMax;
        this.currWeapon = MathUtils.random(level);
        this.angle = MathUtils.random(0, 360);
        this.SHIP_SPEED = MathUtils.random(150, 300);
        this.active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt){
        super.update(dt);

        tempVec.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVec.angleDeg();
        if (gc.getHero().getPosition().dst(position) > 300) {
            accelerate(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 25;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f,
                        1.2f, 0.2f,
                        0.0f, 0.5f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 0.0f);
            }
        }
        if (gc.getHero().getPosition().dst(position) < ATTACK_RADIUS) {
            tryToFire();
        }
    }

    @Override
    protected void checkBorder() {
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
        attackArea.setPosition(position);
    }
}
