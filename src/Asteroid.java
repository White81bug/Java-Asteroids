import com.raylib.Vector2;

public class Asteroid extends Thing {
    static final Shape asteroidShape = new Shape(new Vector2[]{
            new Vector2(2, 2), new Vector2(-2, 2), new Vector2(-2, -2),
            new Vector2(2, -2)
    });

    void OnHit() {
        this.askToDie = true;
    }

    void OnCollision(Thing other) {
        Collider.Bounce(this, other);
    }

    Asteroid() {
        super(Asteroid.asteroidShape);
        priority = 20; // To decide from which object to call collision processing function from
    }

    Asteroid(Vector2 pos) {
        this();
        this.position = pos;

        Vector2 direction = mUtils.vecSub(new Vector2(GLOBALS.MAX_WORLD_POS / 2f, GLOBALS.MAX_WORLD_POS / 2f), pos);
        float length = (float) Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY());
        direction = new Vector2(direction.getX() / length, direction.getY() / length); // normalize

        this.speed = mUtils.vecMul(direction, 6f);//need to set up speed for appropriate movespeed
    }
}
