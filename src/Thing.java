import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

final class colider {
    static boolean CheckCollide(Thing a, Thing b) {

        float length = (float) Math.sqrt(
                Math.pow(b.position.getX() - a.position.getX(), 2)
                        + Math.pow(b.position.getY() - a.position.getY(), 2));

        if (a.shape.colliderRadius + b.shape.colliderRadius < length)
            return false;
        return true;
    }

    static int RunCollider(StaticList<Thing> list, int index) {
        int collisionCount = 0;
        Thing first = list.Get(index);
        Thing second;
        for (int i = index + 1; i < list.GetLen(); i++) {
            second = list.Get(index);

            // This is an optimization, so we don't have to check the rest of the list
            // if the current object is out of reach.
            // It assumes that the list is sorted by X
            if (second.shape.colliderRadius + first.shape.colliderRadius < second.position.getX()
                    + first.position.getX())
                break;

            if (!CheckCollide(first, second))
                continue;
            collisionCount++;
        }
        return collisionCount;
    }
}

public class Thing {
    float rotateSpeed;
    Vector2 position;
    Vector2 speed;
    Shape shape;
    float heading;

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
                mUtils.vecMul(
                        this.speed, getFrameTime()));
        rotationDelta = this.shape.rotation - this.heading;
        if (rotationDelta != 0) {
            this.shape.rotation = this.heading;
            this.shape.ApplyRotation();
        }
    }

    void Draw() {
        if (this.shape == null)
            throw new NullPointerException();
        if (this.shape.size < 2)
            return;

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
