package src;

import static com.raylib.Raylib.*;

import com.raylib.Vector2;

public class Asteroid extends Thing {
    static final float ROUGHNESS = .5f;
    static final float HORIZONTAL_BIAS = .5f;
    float angularMomentum;

    static Vector2[] GenShape() {
        final Vector2 ref = new Vector2(2, 2);
        final float step = mUtils.TAU / Shape.MAX_SIZE;
        Vector2[] shape = new Vector2[Shape.MAX_SIZE];
        Vector2 correction = new Vector2();
        Vector2 randOffset = new Vector2();

        for (int i = 0; i < Shape.MAX_SIZE; i++) {
            // Even float math is faster than creating and accessing a new var for `step * i`
            correction.x((float) Math.sin(step * i));
            correction.y((float) Math.cos(step * i));
            randOffset.x((float) Math.random() * Asteroid.ROUGHNESS
                    * Asteroid.HORIZONTAL_BIAS - .5f);
            randOffset.y((float) Math.random() * Asteroid.ROUGHNESS - .5f);
            shape[i] =
                    mUtils.vecMul(mUtils.vecAdd(ref, randOffset), correction);
        }
        return shape;
    }

    void OnHit() {
        LogicMaster.AddScore();
        this.askToDie = true;

    }

    void OnCollision(Thing other) {
        Collider.Bounce(this, other);
    }

    void Update() {
        this.heading += this.angularMomentum;
        super.Update();
    }

    Asteroid() {
        super(new Shape(Asteroid.GenShape()));
        this.priority = 20; // To decide from which object to call collision processing function from
    }

    Asteroid(Vector2 pos, float mass) {

        super(new Shape(Asteroid.GenShape(), Shape.DEFAULT_SCALE * mass));
        this.priority = 20; // To decide from which object to call collision processing function from
        this.position = pos;
        this.angularMomentum = ((float) Math.random() - .5f) / 10.f;

        this.mass = mass;
    }

    Asteroid(Vector2 pos) {
        this(pos, 1.f);
    }

    void Draw() {
        if (GLOBALS.DEBUG)
            drawText(String.format("%f", this.angularMomentum),
                    (int) this.position.x(), (int) this.position.y() + 52, 18,
                    RAYWHITE);
        super.Draw();
    }
}
