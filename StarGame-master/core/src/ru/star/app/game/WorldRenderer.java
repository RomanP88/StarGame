package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.star.app.screen.ScreenManager;
import ru.star.app.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
    }

    public void render() {
        batch.begin();
        gc.getBg().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getParticleController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getInfoController().render(batch, font32);
        gc.getBotController().render(batch);
        gc.getHero().render(batch);
        gc.getHero().renderGUI(batch, font32);
        gc.getHero().drawPowerShield(batch);
        if (gc.getTimer() < 3) {
            font32.draw(batch, "LEVEL " + gc.getLevel(), ScreenManager.HALF_SCREEN_WIDTH - 30, ScreenManager.HALF_SCREEN_HEIGHT);
        }
        batch.end();
        gc.getStage().draw();
    }
}
