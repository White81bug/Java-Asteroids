
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;


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

    static boolean checkCollision(Vector2 a, Vector2 b, float radius) {
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();
        return dx * dx + dy * dy <= radius * radius;
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

    Vector2[] getRotatedPoints(float angle) {
        Vector2[] rotated = new Vector2[this.size];
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        for (int i = 0; i < this.size; i++) {
            float x = this.ref[i].getX() * this.scale;
            float y = this.ref[i].getY() * this.scale;

            float rotatedX = x * cos - y * sin;
            float rotatedY = x * sin + y * cos;

            rotated[i] = new Vector2(rotatedX, rotatedY);
        }

        return rotated;
    }
}

class Thing {
    float rotateSpeed;
    float heading;
    Vector2 position;
    Vector2 speed;
    Shape shape;
    float radius = 20;

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

        Vector2[] rotated = this.shape.getRotatedPoints(-this.heading);

        Vector2 startPos = mUtils.vecAdd(rotated[this.shape.size - 1], this.position);
        Vector2 endPos = mUtils.vecAdd(rotated[0], this.position);

        drawLineV(startPos, endPos, RAYWHITE);
        for (int i = 1; i < this.shape.size; i++) {
            startPos = endPos;
            endPos = mUtils.vecAdd(rotated[i], this.position);
            drawLineV(startPos, endPos, RAYWHITE);
        }

    }
};

class LogicMaster {
    StaticList<Thing> objList;
    int playerScore;
    Player player;
    static StaticList<Bullet> bullets;

    static final Shape asteroid = new Shape(new Vector2[] {
            new Vector2(2, 2),
            new Vector2(-2, 2),
            new Vector2(-2, -2),
            new Vector2(2, -2)
    });
    static {
        asteroid.scale = 1;
    }

    static final Shape playerShape = new Shape(new Vector2[] {
            new Vector2(0, -2),
            new Vector2(-2, 2),
            new Vector2(0, 1),
            new Vector2(2, 2)
    });
    static {
        playerShape.scale = 1;
    }
    static final Shape bulletShape = new Shape(new Vector2[] {
            new Vector2(0, -0.5f),
            new Vector2(0.5f, 0.5f),
            new Vector2(-0.5f, 0.5f)
    });
    static {
        bulletShape.scale = 1;
    }

    LogicMaster() {
        objList = new StaticList<Thing>();
        playerScore = 0;
        player = new Player(LogicMaster.playerShape);
        bullets = new StaticList<>();
    }

    void CreateAsteroid(Vector2 position) {
        Thing asteroidObj = new Thing(LogicMaster.asteroid);
        asteroidObj.radius = 16;
        asteroidObj.position = position;
        objList.Push(asteroidObj);
    }

    static void RequestBullet(Vector2 position, float heading) {
        bullets.Push(new Bullet(position, heading, bulletShape));
    }

    void Update() {
        player.UpdatePlayerPosition();

        for (int i = 0; i < bullets.GetLen(); i++) {
            Bullet b = bullets.Get(i);
            if (b == null)
                continue;

            b.UpdatePosition();

            for (int j = 0; j < objList.GetLen(); j++) {
                Thing obj = objList.Get(j);
                if (obj == null)
                    continue;

                if (mUtils.checkCollision(b.position, obj.position, b.radius + obj.radius)) {
                    bullets.Pop(i);
                    objList.Pop(j);
                    break;
                }
            }

            if (b.isOffscreen())
                bullets.Pop(i);
        }
    }

    void Render() {
        player.Draw();
        for (int i = 0; i < bullets.GetLen(); i++) {
            Bullet b = bullets.Get(i);
            if (b != null)
                b.Draw();
        }
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

        joel.CreateAsteroid(new Vector2(200, 100));
        while (!windowShouldClose()) {
            joel.Update();
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
