
import static com.raylib.Raylib.*;

import static com.raylib.Raylib.CameraMode.CAMERA_ORBITAL;
import static com.raylib.Raylib.CameraProjection.CAMERA_PERSPECTIVE;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;
class GLOBALS {
    static final int MIN_WORLD_POS = 0;
    static final int MAX_WORLD_POS = 1024;
}

class Renderer extends GLOBALS {
    static final int GRID_STEP = (MAX_WORLD_POS - MIN_WORLD_POS) / 40;
    static final com.raylib.Color GRID_COLOR = new com.raylib.Color((byte) 100, (byte) 100, (byte) 100, (byte) 100);
    static final com.raylib.Color BORDER_COLOR = new com.raylib.Color((byte) 0xa0, (byte) 0xa0, (byte) 0xa0,
            (byte) 0xff);

    static void drawGrid() {
        for (int i = MIN_WORLD_POS + GRID_STEP; i < MAX_WORLD_POS; i += GRID_STEP) {
            drawLine(i, MIN_WORLD_POS, i, MAX_WORLD_POS, GRID_COLOR);
            drawLine(MIN_WORLD_POS, i, MAX_WORLD_POS, i, GRID_COLOR);
        }
        drawLine(MIN_WORLD_POS, MIN_WORLD_POS, MIN_WORLD_POS, MAX_WORLD_POS, BORDER_COLOR);
        drawLine(MIN_WORLD_POS, MAX_WORLD_POS, MAX_WORLD_POS, MAX_WORLD_POS, BORDER_COLOR);
        drawLine(MAX_WORLD_POS, MAX_WORLD_POS, MAX_WORLD_POS, MIN_WORLD_POS, BORDER_COLOR);
        drawLine(MAX_WORLD_POS, MIN_WORLD_POS, MIN_WORLD_POS, MIN_WORLD_POS, BORDER_COLOR);
    }
}

class mUtils {
    static Vector2 vecAdd(Vector2 a, Vector2 b) {
        return new Vector2(a.getX() + b.getX(), a.getY() + b.getY());
    }

    static Vector2 vecMul(Vector2 a, float b) {
        return new Vector2(a.getX() * b, a.getY() * b);
    }

}

class StaticList<T> {
    static final int ARRAY_SIZE = 1024;
    private Object[] list;
    private int real_len;

    StaticList() {
        list = new Object[StaticList.ARRAY_SIZE];
        real_len = 0;
    }

    void Push(T obj) {
        list[real_len] = obj;
        real_len++;
    }

    int GetLen() {
        return real_len;
    }

    T Get(int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0) {
            System.err.printf("Got i == %d when real_len == %d\n", i, real_len);
            throw new ArrayIndexOutOfBoundsException();
        }
        return (T) list[i];
    }

    T Pop(int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0 || real_len <= i) {
            throw new ArrayIndexOutOfBoundsException();
        }
        T tmp = (T) list[i];
        list[i] = null;
        return tmp;
    }

}

class Shape {
    private Vector2[] ref;
    Vector2[] points;
    int size = 0;
    float scale = 20;

    static final int MAX_SIZE = 32;

    Shape(Vector2[] newPoints) {
        this.ref = newPoints;
        this.size = ref.length;
        this.points = this.ref;
        for (int i = 0; i < this.size; i++) {
            this.points[i] = mUtils.vecMul(this.ref[i], this.scale);
        }
    }
}

class Thing {
    float rotateSpeed;
    float heading;
    Vector2 position;
    Vector2 speed;
    Shape shape;

    Thing(Shape shape) {
        rotateSpeed = 0;
        heading = 0;
        position = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        this.shape = shape;
    }

    void Draw() {
        if (this.shape == null)
            throw new NullPointerException();
        if (this.shape.size < 1)
            return;
        Vector2 startPos = mUtils.vecAdd(this.shape.points[this.shape.size - 1], this.position);
        Vector2 endPos = mUtils.vecAdd(this.shape.points[0], this.position);

        drawLineV(startPos, endPos, RAYWHITE);
        for (int i = 1; i < this.shape.size; i++) {
            startPos = endPos;
            endPos = mUtils.vecAdd(this.shape.points[i], this.position);
            drawLineV(startPos, endPos, RAYWHITE);
        }

    }
};

class LogicMaster {
    StaticList<Thing> objList;
    int playerScore;

    static final Shape asteroid = new Shape(new Vector2[] {
            new Vector2(2, 2),
            new Vector2(-2, 2),
            new Vector2(-2, -2),
            new Vector2(2, -2)
    });

    static final Shape player = new Shape(new Vector2[] {
            new Vector2(0, -2),
            new Vector2(-2, 2),
            new Vector2(0, 1),
            new Vector2(2, 2)
    });

    LogicMaster() {
        objList = new StaticList<Thing>();
        playerScore = 0;
    }

    void CreateAsteroid() {
        objList.Push(new Thing(LogicMaster.asteroid));
    }

    void Render() {
        for (int i = 0; i < objList.GetLen(); i++) {
            Thing obj = null;
            try {
                obj = objList.Get(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            if (obj == null)
                continue;

            try {
                obj.Draw();
            } catch (NullPointerException e) {

                System.out.printf("Got a null on i == %d\n", i);
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

public class Main {
    public static void main(String args[]) {
        initWindow(1280, 800, "Demo");
        setTargetFPS(60);

        Camera2D camera = new Camera2D(
                new Vector2(0, 0), // offset
                new Vector2(-10, -10), // target
                0, // rotation
                2f // zoom
        );
        LogicMaster joel = new LogicMaster();

        joel.CreateAsteroid();
        joel.objList.Get(0).position = new Vector2(200, 200);
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);
            beginMode2D(camera);
            Renderer.drawGrid();

            joel.Render();

            endMode2D();
            drawFPS(20, 20);
            endDrawing();

        }
        closeWindow();
    }
}
