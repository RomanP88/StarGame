package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.star.app.game.Weapon.WeaponOwner;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Ship {
    protected GameController gc;
    protected TextureRegion texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float angle;
    protected int hpMax;
    protected int hp;
    protected Circle hitArea;
    protected float SHIP_SPEED;
    protected Weapon weapon;
    protected Weapon[] weapons;
    protected int currWeapon;
    protected float fireTimer;

    public Ship(GameController gc, WeaponOwner owner) {
        this.gc = gc;
        this.position = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.SHIP_SPEED = 500.0f;
        this.hpMax = 100;
        this.hp = hpMax;
        this.hitArea = new Circle(position, 28.0f);
        this.currWeapon = 0;
        createWeapon(owner);
        this.weapon = weapons[0];
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    protected void tryToFire() {
        if (fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            weapon.fire(position, velocity, angle);
        }
    }

    protected void createWeapon(WeaponOwner owner) {
        weapons = new Weapon[]{
                new Weapon(gc.getBulletController(), 0.4f, 1, 300, 100, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0)
                        }),
                new Weapon(gc.getBulletController(), 0.3f, 1, 400, 150, owner,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0)
                        }),
                new Weapon(gc.getBulletController(), 0.2f, 1, 500, 200, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, -90, -10)
                        }),
                new Weapon(gc.getBulletController(), 0.1f, 1, 500, 200, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, -90, -10)
                        }),
                new Weapon(gc.getBulletController(), 0.2f, 2, 600, 250, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(gc.getBulletController(), 0.1f, 2, 700, 300, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(gc.getBulletController(), 0.1f, 5, 800, 350, owner,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 15),
                                new Vector3(28, 90, 25),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -15),
                                new Vector3(28, -90, -25)
                        })
        };
    }

    protected void checkBorder() {
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        } else if (position.x > SCREEN_WIDTH - 32) {
            position.x = SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5f;
        } else if (position.y > SCREEN_HEIGHT - 32) {
            position.y = SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }
        this.hitArea.setPosition(position);
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        float stopKoef = 1.0f - dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);
        checkBorder();
    }


    public boolean isAlive(){
        return hp > 0;
    }

    protected void accelerate(float dt) {
        velocity.x += MathUtils.cosDeg(angle) * SHIP_SPEED * dt;
        velocity.y += MathUtils.sinDeg(angle) * SHIP_SPEED * dt;
    }

    protected void brake(float dt){
        velocity.x -= MathUtils.cosDeg(angle) * (SHIP_SPEED / 2) * dt;
        velocity.y -= MathUtils.sinDeg(angle) * (SHIP_SPEED / 2) * dt;
    }
}
