package ru.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.star.app.screen.utils.Assets;

public class Weapon {
    public enum WeaponOwner{
        HERO, BOT;
    }

    private int maxBullets;
    private int currBullets;
    private BulletController bulletController;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private WeaponOwner owner;
    private Sound shootSound;

    private Vector3[] slots;

    public Weapon(BulletController bulletController, float firePeriod, int damage, float bulletSpeed, int maxBullets, WeaponOwner owner, Vector3[] slots) {
        this.maxBullets = maxBullets;
        this.currBullets = maxBullets;
        this.bulletController = bulletController;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.slots = slots;
        this.owner = owner;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurrBullets() {
        return currBullets;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public WeaponOwner getOwner() {
        return owner;
    }

    public int increaseAmmo(int amount){
        int oldAmmo = currBullets;
        currBullets += amount;
        if(currBullets > maxBullets){
            currBullets = maxBullets;
        }
        return currBullets - oldAmmo;
    }

    public void fire(Vector2 position, Vector2 velocity, float angle) {
        if (currBullets > 0) {
            currBullets--;
            shootSound.play();
            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = position.x + slots[i].x * MathUtils.cosDeg(angle + slots[i].y);
                y = position.y + slots[i].x * MathUtils.sinDeg(angle + slots[i].y);
                vx = velocity.x + bulletSpeed * MathUtils.cosDeg(angle + slots[i].z);
                vy = velocity.y + bulletSpeed * MathUtils.sinDeg(angle + slots[i].z);
                bulletController.setup(x, y, vx, vy, owner, damage);
            }
        }
    }
}
