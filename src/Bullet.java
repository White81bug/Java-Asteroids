import com.raylib.Vector2;

public class Bullet extends Thing {
    static final Shape bulletShape = new Shape(new Vector2[] {
            new Vector2(0, -2f), new Vector2(2f, 2f), new Vector2(-2f, 2f)
    }, (float) 5);

    static final float START_SPEED = 16.0f;

    void OnCollision(Thing other) {
        other.OnHit();
        this.askToDie = true;
    }

    void OnHit() {
        this.askToDie = true;
    }

    Bullet(Player player) {
        super(Bullet.bulletShape);
        priority = 100;
        this.position = mUtils.vecAdd(player.position,
                mUtils.vecMul(player.shape.points[0], new Vector2(2, 2)));
        this.shape.rotation = player.shape.rotation;
        this.speed = mUtils.vecAdd(mUtils.vecMul(
                new Vector2(Bullet.START_SPEED, Bullet.START_SPEED),
                player.shape.points[0]), player.speed);
    }

    void Update() {
        super.Update();
    }
}
