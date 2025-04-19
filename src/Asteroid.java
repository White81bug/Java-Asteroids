import com.raylib.Vector2;

public class Asteroid extends Thing {
    static final Shape asteroidShape = new Shape(new Vector2[] {
            new Vector2(2, 2), new Vector2(-2, 2), new Vector2(-2, -2),
            new Vector2(2, -2)
    });

    final int priority = 10;      // To decide from which object to call collision processing function from

    void OnHit() {
        this.askToDie = true;
    }

    void OnCollision(Thing other) {
        Collider.Bounce(this, other);
    }

    Asteroid() {
        super(Asteroid.asteroidShape);
    }

    Asteroid(Vector2 pos) {
        this();
        this.position = pos;
    }
}
