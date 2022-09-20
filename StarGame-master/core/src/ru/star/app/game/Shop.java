package ru.star.app.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.star.app.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;

    public Shop(Hero hero) {
        this.hero = hero;
        font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(0, 0, 0.5f, 1);
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        final TextButton btnClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this;
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
            }
        });
        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(340, 340);
        this.addActor(btnClose);

        final TextButton btnHpMax = new TextButton("HP MAX", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
                                 @Override
                                 public void changed(ChangeEvent event, Actor actor) {
                                     thisShop.hero.upgrade(Hero.HeroUpgrade.HPMAX); // Если указать просто  hero получаем Variable 'hero' is accessed from within inner class, needs to be declared final
                                 }
                             }
        );


        btnHpMax.setPosition(20, 300);
        this.addActor(btnHpMax);

        final TextButton btnHp = new TextButton("HP", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
                                 @Override
                                 public void changed(ChangeEvent event, Actor actor) {
                                     thisShop.hero.upgrade(Hero.HeroUpgrade.HP); // Если указать просто  hero получаем Variable 'hero' is accessed from within inner class, needs to be declared final
                                 }
                             }
        );


        btnHp.setPosition(20, 200);
        this.addActor(btnHp);

        final TextButton btnWeapon = new TextButton("WEAPON", textButtonStyle);
        btnWeapon.addListener(new ChangeListener() {
                                 @Override
                                 public void changed(ChangeEvent event, Actor actor) {
                                     thisShop.hero.upgrade(Hero.HeroUpgrade.WEAPON); // Если указать просто  hero получаем Variable 'hero' is accessed from within inner class, needs to be declared final
                                 }
                             }
        );


        btnWeapon.setPosition(120, 300);
        this.addActor(btnWeapon);

        final TextButton btnMagnetic = new TextButton("MAGNET", textButtonStyle);
        btnMagnetic.addListener(new ChangeListener() {
                                 @Override
                                 public void changed(ChangeEvent event, Actor actor) {
                                     thisShop.hero.upgrade(Hero.HeroUpgrade.MAGNETIC); // Если указать просто  hero получаем Variable 'hero' is accessed from within inner class, needs to be declared final
                                 }
                             }
        );


        btnMagnetic.setPosition(120, 200);
        this.addActor(btnMagnetic);

        final TextButton btnBullet = new TextButton("BULLET", textButtonStyle);
        btnBullet.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        thisShop.hero.upgrade(Hero.HeroUpgrade.BULLET); // Если указать просто  hero получаем Variable 'hero' is accessed from within inner class, needs to be declared final
                                    }
                                }
        );
        btnBullet.setPosition(20, 100);
        this.addActor(btnBullet);

        this.setPosition(20, 20);
        setVisible(false);
        skin.dispose();
    }
}
