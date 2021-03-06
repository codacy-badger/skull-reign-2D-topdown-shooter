package model.projectiles;

import controller.animation.AnimationClip;
import controller.animation.SpriteSheet;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import model.GameObject;
import model.enemies.Enemy;
import model.wall.Wall;
import view.GameViewManager;

public class Projectile extends GameObject {

    private boolean skull;
    private ProjectileType projectileType;
    private double angle;
    private double scale = 1.0;
    private AnimationClip animationClip;
    private Boolean enemyProjectile;
    private static final float playerSpeedMultiplier = 1.4f;

    private float speed;

    Projectile(Point2D spawnPoint, ProjectileType projectileType, double angle, Boolean enemyProjectile, boolean skull) {
        this(spawnPoint, projectileType, angle, enemyProjectile);
        this.skull = true;
    }

    Projectile(Point2D spawnPoint, ProjectileType projectileType, double angle, Boolean enemyProjectile) {
        super(projectileType.getURL());
        this.projectileType = projectileType;
        this.speed = projectileType.getSPEED();
        this.angle = angle;
        this.enemyProjectile = enemyProjectile;

        float speedMultiplier = enemyProjectile ? 1 : playerSpeedMultiplier;
        this.speed = projectileType.getSPEED() * speedMultiplier;

        if (projectileType.isANIMATED()) {
            this.animated = true;
            SpriteSheet spriteSheet = new SpriteSheet(projectileType.getURL(), 0);
            animationClip = new AnimationClip(spriteSheet,
                    spriteSheet.getFrameCount() * 1.2f,
                    false,
                    AnimationClip.INF_REPEATS,
                    this);
            animationClip.animate();
        }
        spawnProjectile(spawnPoint, angle);
    }

    public void spawnProjectile(Point2D spawnPoint, double angle) {
        setLayoutX(spawnPoint.getX());
        setLayoutY(spawnPoint.getY());
        Rotate rotate = new Rotate(angle, 0, 0);
        getTransforms().add(rotate);
    }

    public void addSpeed(float speed) {
        this.speed = this.speed + speed;
    }

    public void setDmgScale(double scale) {
        this.scale = 1 + scale / 100;
        setScaleX(this.scale);
        setScaleY(this.scale);
    }

    public double getDamage() {
        return projectileType.getDAMAGE() * (2 * scale - 1);
    }

    private void move() {
        setLayoutY(getLayoutY() + Math.sin(Math.toRadians(angle)) * speed);
        setLayoutX(getLayoutX() + Math.cos(Math.toRadians(angle)) * speed);
    }

    private void checkCollision_entity() {
        if (enemyProjectile) {
            if (isIntersects(GameViewManager.getPlayer())) {
                GameViewManager.getPlayer().takeDmg(projectileType.getDAMAGE());
                GameViewManager.getMainPane().removeFromGamePane(this);
            }
        } else {
            for (Enemy enemy : GameViewManager.getInstance().getEnemyArrayList()) {
                if (isIntersects(enemy)) {
                    enemy.takeDmg(getDamage());
                    GameViewManager.getMainPane().removeFromGamePane(this);
                }
            }
        }
    }

    private void checkCollision_border() {
        if ((getLayoutY() > GameViewManager.HEIGHT || getLayoutY() < 0)
                && (getLayoutX() > GameViewManager.WIDTH || getLayoutX() < 0)) {
            GameViewManager.getMainPane().removeFromGamePane(this);
        }
    }

    private void checkCollision_wall() {
        for (Wall wall : GameViewManager.getInstance().getWallArrayList()) {
            if (isIntersects(wall) && !skull) {
                GameViewManager.getMainPane().removeFromGamePane(this);
            }
        }
    }

    @Override
    public void update() {
        move();

        if (animated) {
            animationClip.animate();
        }
        checkCollision_entity();
        checkCollision_border();
        checkCollision_wall();
    }

    @Override
    public Node[] getChildren() {
        return null;
    }
}