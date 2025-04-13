import com.raylib.Vector2;

public class Asteroid extends Thing {
    static final Shape asteroidShape = new Shape(new Vector2[] {
            new Vector2(2, 2), new Vector2(-2, 2), new Vector2(-2, -2),
            new Vector2(2, -2)
    });

    Asteroid() {
        super(Asteroid.asteroidShape);
    }

    Asteroid(Vector2 pos) {
        this();
        this.position = pos;
    }
}
