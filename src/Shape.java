import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Optional;

public class Shape {
    final private Vector2[] ref;
    Vector2[] points;
    int size = 0;
    float scale = 20; // Scale to multiply radius and shape by
    float colliderRadius = 2; // Radius that will be used to process collision
    float rotation = 0;

    static final int MAX_SIZE = 32;

    Shape(Vector2[] newPoints) {
        super();
        this.size = newPoints.length;
        this.colliderRadius *= this.scale;

        this.points = new Vector2[MAX_SIZE];
        Vector2 temp[] = new Vector2[MAX_SIZE];
        for (int i = 0; i < this.size; i++) {
            temp[i] = mUtils.vecMul(newPoints[i], this.scale);
        }
        this.ref = temp.clone();
        this.ClonePoints(ref, points);
    }

    void ClonePoints(final Vector2[] from, Vector2[] to) {
        for (int i = 0; i < this.size; i++) {
            to[i] = new Vector2(from[i].getX(), from[i].getY());
        }
    }

    Shape(Vector2[] newPoints, float scale) {
        super();
        this.scale = scale;
        this.size = newPoints.length;
        this.colliderRadius *= this.scale;

        this.points = new Vector2[MAX_SIZE];
        Vector2 temp[] = new Vector2[MAX_SIZE];
        for (int i = 0; i < this.size; i++) {
            temp[i] = mUtils.vecMul(newPoints[i], this.scale);
        }
        this.ref = temp.clone();
        this.ClonePoints(ref, points);
    }

    void ApplyRotation() {
        float sin = (float) Math.sin(this.rotation);
        float cos = (float) Math.cos(this.rotation);

        for (int i = 0; i < this.size; i++) {
            this.points[i]
                    .setX(this.ref[i].getX() * cos - this.ref[i].getY() * sin);
            this.points[i]
                    .setY(this.ref[i].getX() * sin + this.ref[i].getY() * cos);

            if (mUtils.LogLevelToInt(GLOBALS.LOG_LEVEL) >= mUtils
                    .LogLevelToInt(LogLevel.TRACE)) {
                System.out.printf("New point %d pos: %f:%f\n", i,
                        this.points[i].getX(), this.points[i].getY());
                System.out.printf("Ref point %d pos: %f:%f\n\n", i,
                        this.ref[i].getX(), this.ref[i].getY());
            }
        }
    }
}
