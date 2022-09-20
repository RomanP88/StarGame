package ru.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Background {

    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Star() {
            position = new Vector2(MathUtils.random(-200, SCREEN_WIDTH + 200), MathUtils.random(-200, SCREEN_HEIGHT + 200));
            velocity = new Vector2(MathUtils.random(-40, -5), 0);
            scale = Math.abs(velocity.x / 40f) * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1f) * dt;
            position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1f) * dt;
            if (position.x < -200) {
                position.x = SCREEN_WIDTH + 200;
                position.y = MathUtils.random(0, SCREEN_HEIGHT);
            }
        }
    }

    private final int START_COUNT = 600;
    private GameController gc;
    private Texture textureCosmos;
    private TextureRegion textureStar;
    private Star[] stars;

    public Background(GameController gc) {
        this.gc = gc;
        prepareBackground();
    }

    public Background() {
        this.gc = null;
        prepareBackground();
    }

    private void prepareBackground() {
        textureCosmos = new Texture("images/bg.png");
        stars = new Star[START_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        if(textureStar == null){
            textureStar = Assets.getInstance().getAtlas().findRegion("star16");
        }
        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
                    16, 16, stars[i].scale, stars[i].scale, 0);
            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
                        16, 16, stars[i].scale * 3, stars[i].scale * 3, 0);
            }
        }
    }

    public void update(float dt) {
        if(this.gc != null) {
            for (int i = 0; i < stars.length; i++) {
                stars[i].update(dt);
            }
        }
    }

    public void dispose() {
        textureCosmos.dispose();
    }
}
