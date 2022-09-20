package ru.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.star.app.game.Background;
import ru.star.app.game.Hero;
import ru.star.app.screen.utils.Assets;


public class
GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private BitmapFont font48;
    private BitmapFont font24;
    private Background bg;
    private StringBuilder sb;
    private Hero defeatedHero;
    private Music music;

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.bg = new Background();
        this.sb = new StringBuilder();
    }

    @Override
    public void show() {
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.music = Assets.getInstance().getAssetManager().get("audio/music.mp3");
        this.music.setLooping(true);
        this.music.play();
    }

    public void update(float dt) {
        if(Gdx.input.justTouched()){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        sb.setLength(0);
        sb.append("Your score: ").append(defeatedHero.getScore()).append("\n");
        sb.append("Your money: ").append(defeatedHero.getCoins()).append("\n");
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1);
        batch.begin();
        bg.render(batch);
        font72.draw(batch, "GameOver", 0, 600, ScreenManager.SCREEN_WIDTH, Align.center, false);
        font48.draw(batch, sb, 0, 400, ScreenManager.SCREEN_WIDTH, Align.center, false);
        font24.draw(batch, "Tap on screen to return to main menu", 0, 40, ScreenManager.SCREEN_WIDTH, Align.center, false);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
    }
}
