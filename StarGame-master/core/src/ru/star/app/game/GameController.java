package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.star.app.game.Weapon.WeaponOwner;
import ru.star.app.screen.ScreenManager;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static ru.star.app.screen.ScreenManager.ScreenType.GAME_OVER;

public class GameController {
    private Background bg;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private BotController botController;
    private InfoController infoController;
    private Vector2 tempVector;
    private boolean pause;
    private Stage stage;
    private int level;
    private float timer;
    private Music music;
    private Sound destroyAsteroid;
    private Sound destroyBot;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public BotController getBotController() {
        return botController;
    }

    public Background getBg() {
        return bg;
    }

    public Hero getHero() {
        return hero;
    }

    public Stage getStage() {
        return stage;
    }

    public float getTimer() {
        return timer;
    }

    public GameController(SpriteBatch batch) {
        this.bg = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController();
        this.powerUpsController = new PowerUpsController();
        this.infoController = new InfoController();
        this.botController = new BotController(this);
        this.hero = new Hero(this);
        this.tempVector = new Vector2();
        this.particleController = new ParticleController();
        this.pause = false;
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.level = 0;
        Gdx.input.setInputProcessor(stage);
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.destroyAsteroid = Assets.getInstance().getAssetManager().get("audio/explose2.mp3");
        this.destroyBot = Assets.getInstance().getAssetManager().get("audio/explose.mp3");
        this.music.setLooping(true);
        this.music.setVolume(0.4f);
        this.music.play();
    }

    public int getLevel() {
        return level;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = !pause;
            hero.getShop().setVisible(pause);
        }
        if (!hero.getShop().isVisible() && pause) {
            pause = false;
        }
        infoController.update(dt);
        stage.act(dt);
        if (!pause) {
            bg.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            powerUpsController.update(dt);
            if (asteroidController.getActiveList().size() == 0 && botController.getActiveList().size() == 0) {
                level++;
                createAsteroids(level);
                createBots(level);
                timer = 0.0f;
            }
            asteroidController.update(dt);
            botController.update(dt);
            hero.update(dt);
            timer += dt;
        }
        checkMagnetic();
        checkCollision();
        checkShipCollision();
        checkPickPowerUps();
        checkBotBullet();
        checkBots();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(GAME_OVER, hero);
        }
    }

    public void createAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            asteroidController.setup(MathUtils.random(0, SCREEN_WIDTH),
                    MathUtils.random(0, SCREEN_HEIGHT),
                    MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1, level);
        }
    }

    public void createBots(int count) {
        for (int i = 0; i < count; i++) {
            botController.setup();
        }
    }

    private void checkPickPowerUps() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps powerUps = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(powerUps.getHitArea())) {
                hero.pickupPowerUps(powerUps);
                particleController.getEffectBuilder().takePowerUpsEffect(powerUps);
            }
        }
    }

    private void checkShipCollision() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(asteroid.getHitArea())) {
                float dst = asteroid.getPosition().dst(hero.getPosition());
                float halfOverlaps = (asteroid.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(asteroid.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverlaps);
                asteroid.getPosition().mulAdd(tempVector, -halfOverlaps);
                float sumScale = hero.getHitArea().radius * 2 + asteroid.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector,
                        200 * asteroid.getHitArea().radius / sumScale);
                asteroid.getVelocity().mulAdd(tempVector,
                        -200 * hero.getHitArea().radius / sumScale);
                if (asteroid.takeDamage(2)) {
                    powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y, 0.1f);
                    float scale = asteroid.getScale() - 0.3f;
                    if (scale >= 0.39) {
                        asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale, level);
                        destroyAsteroid.play();
                    } else {
                        destroyBot.play();
                    }
                    hero.addScore(asteroid.getHpMax() * 50);
                }
                infoController.setup(hero.getPosition().x, hero.getPosition().y, "HP -" + asteroid.getDamage(), Color.RED);
                hero.takeDamage(asteroid.getDamage());
            }
        }
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            for (int j = 0; j < botController.getActiveList().size(); j++) {
                Bot b = botController.getActiveList().get(j);

                if (b.getHitArea().overlaps(a.getHitArea())) {
                    float dst = a.getPosition().dst(b.getPosition());
                    float halfOverLen = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2.0f;
                    tempVector.set(b.getPosition()).sub(a.getPosition()).nor();
                    b.getPosition().mulAdd(tempVector, halfOverLen);
                    a.getPosition().mulAdd(tempVector, -halfOverLen);

                    float sumScl = b.getHitArea().radius * 2 + a.getHitArea().radius;
                    b.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                    a.getVelocity().mulAdd(tempVector, -200.0f * b.getHitArea().radius / sumScl);

                    a.takeDamage(1);
                    b.takeDamage(level);
                }
            }
        }
    }

    private void checkMagnetic() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps powerUps = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticArea().overlaps(powerUps.getHitArea())) {
                float dst = powerUps.getPosition().dst(hero.getPosition());
                float speed = (powerUps.getHitArea().radius + hero.getMagneticArea().radius - dst) / (level * 10.0f);
                tempVector.set(hero.getPosition()).sub(powerUps.getPosition()).nor();
                powerUps.getPosition().mulAdd(tempVector, speed);
            }
        }
    }

    private void checkBots() {
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot bot = botController.getActiveList().get(i);
            if (bot.getAttackArea().overlaps(hero.getHitArea())) {
                tempVector.set(hero.getPosition()).sub(bot.getPosition()).nor();
                bot.setAngle(tempVector.x, tempVector.y);
                bot.tryToFire();
            }
        }
    }

    private void checkCollision() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet bullet = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidController.getActiveList().get(j);
                if (asteroid.getHitArea().contains(bullet.getPosition())) {
                    particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4),
                            bullet.getPosition().y + MathUtils.random(-4, 4),
                            bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.1f,
                            3.0f, 2.0f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 0.0f);
                    bullet.deactivate();
                    if (asteroid.takeDamage(bullet.getDamage())) {
                        powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y, 0.25f);
                        float scale = asteroid.getScale() - 0.3f;
                        if (scale >= 0.39) {
                            asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale, level);
                            destroyAsteroid.play();
                        } else {
                            destroyBot.play();
                        }
                        if(bullet.getOwner() == WeaponOwner.HERO) {
                            hero.addScore(asteroid.getHpMax() * 100);
                        }
                    }
                    break;
                }
            }
            if(bullet.getOwner() == WeaponOwner.HERO) {
                for (int j = 0; j < botController.getActiveList().size(); j++) {
                    Bot bot = botController.getActiveList().get(j);
                    if (bot.getHitArea().contains(bullet.getPosition())) {
                        bot.takeDamage(bullet.getDamage());
                        infoController.setup(bot.getPosition().x, bot.getPosition().y, "HP -" + bullet.getDamage(), Color.ORANGE);
                        bullet.deactivate();
                        if (!bot.isAlive()) {
                            hero.addScore(300);
                            hero.increaseCoin(50);
                            bot.deactivate();
                            destroyBot.play();
                        }
                    }
                }
            }
        }
    }

    public void checkBotBullet() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet bullet = bulletController.getActiveList().get(i);
            if (bullet.getOwner() != WeaponOwner.BOT) {
                continue;
            }
            if (hero.getCurrentShieldPower() > 0) {
                if (hero.getShieldArea().contains(bullet.getPosition())) {
                    hero.takeDamage(bullet.getDamage());
                    infoController.setup(hero.getPosition().x, hero.getPosition().y, "HP -" + bullet.getDamage(), Color.RED);
                    bullet.deactivate();
                }
            } else {
                if (hero.getHitArea().contains(bullet.getPosition())) {
                    hero.takeDamage(bullet.getDamage());
                    infoController.setup(hero.getPosition().x, hero.getPosition().y, "HP -" + bullet.getDamage(), Color.RED);
                    bullet.deactivate();
                }
            }
        }
    }
}

