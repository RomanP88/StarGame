package ru.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.star.app.game.GameController;
import ru.star.app.game.WorldRenderer;
import ru.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gc;
    private WorldRenderer renderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        gc = new GameController(batch);
        renderer = new WorldRenderer(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        renderer.render();
    }
}
