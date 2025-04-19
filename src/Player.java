import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

class Bullet extends Thing {
    static final Shape bulletShape = new Shape(new Vector2[] {
            new Vector2(0, -0.5f), new Vector2(0.5f, 0.5f),
            new Vector2(-0.5f, 0.5f)
    });

    float speed = 8.0f;

    void OnCollision(Thing other) {
        this.askToDie = true;
    }

    Bullet(Player player) {
        super(Bullet.bulletShape);
        this.position = player.position;
        this.shape.rotation = player.shape.rotation;
        this.speed = 8.0f;
    }

    void Update() {
        super.Update();
    }
}

class Player extends Thing {
    //    *
    //   / \
    //  / * \
    // * / \ *
    //
    static final Shape playerShape = new Shape(new Vector2[] {
            new Vector2(0, -2), new Vector2(-2, 2), new Vector2(0, 1),
            new Vector2(2, 2)
    });

    final int priority = 20;      // To decide from which object to call collision processing function from
    final float moveSpeed = 3.5f;
    final float rotSpeed = 2.5f;

    Player() {
        super(Player.playerShape);
        this.position = new Vector2(100, 100);
    }

    Player(Vector2 pos) {
        this();
        this.position = pos;
    }

    void OnCollision(Thing other) {
        Collider.Bounce(this, other);
    }

    void Update() {
        float input = 0;

        if (isKeyDown(KEY_W))
            input += moveSpeed;
        if (isKeyDown(KEY_S))
            input -= moveSpeed;

        if (isKeyDown(KEY_A))
            this.heading -= rotSpeed * getFrameTime();
        if (isKeyDown(KEY_D))
            this.heading += rotSpeed * getFrameTime();

        if (isKeyPressed(KEY_ZERO)) {
            this.position.setX(0);
            this.position.setY(0);
            this.speed.setX(0);
            this.speed.setY(0);
        }

        // This is a hack to get the correct player orientation
        // Assuming that the 0th element in the array is the most front one
        // we can use that to extrapolate the direction of movement required for us.
        // Keep in mind that it NEEDS to be normalized
        // otherwise we will mess with the movement speed, and we don't want that
        float length =
                (float) Math.sqrt(Math.pow(this.shape.points[0].getX(), 2)
                        + Math.pow(this.shape.points[0].getY(), 2));
        this.speed.setX(this.speed.getX()
                + input * (this.shape.points[0].getX() / length));
        this.speed.setY(this.speed.getY()
                + input * (this.shape.points[0].getY() / length));
        super.Update();
    }
}
