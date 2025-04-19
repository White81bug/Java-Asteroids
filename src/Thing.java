import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

abstract public class Thing {
    float rotateSpeed; // The angular momentum to which object will be adjusted
                      // (with respect to frame delta) in the next update
    Vector2 position;
    Vector2 speed;     // Speed indeed
    Shape shape;       // The shape obj of this one
    float heading;     // Where the object is headed
    int priority;      // To decide from which object to call collision processing function from
    float mass;

    Thing(Shape shape) {
        this.rotateSpeed = 0;
        this.position = new Vector2(0, 0);
        this.speed = new Vector2(0, 0);
        this.shape = shape;
    }

    void Update() {
        float rotationDelta;
        this.heading = mUtils.RollOver(this.heading, 0, mUtils.TAU);
        this.position = mUtils.vecAdd(this.position,
                mUtils.vecMul(this.speed, getFrameTime()));
        rotationDelta = this.shape.rotation - this.heading;
        if (rotationDelta != 0) {
            this.shape.rotation = this.heading;
            this.shape.ApplyRotation();
        }
    }

    abstract void OnCollision(Thing other);

    void Draw() {
        if (this.shape == null)
            throw new NullPointerException();
        if (this.shape.size < 2)
            return;
        // drawCircleV(this.position, this.shape.colliderRadius, RED);

        Vector2 startPos = mUtils.vecAdd(this.shape.points[this.shape.size - 1],
                this.position);
        Vector2 endPos = mUtils.vecAdd(this.shape.points[0], this.position);

        drawLineV(startPos, endPos, RAYWHITE);
        for (int i = 1; i < this.shape.size; i++) {
            startPos = endPos;
            endPos = mUtils.vecAdd(this.shape.points[i], this.position);
            drawLineV(startPos, endPos, RAYWHITE);
        }
    }
};
