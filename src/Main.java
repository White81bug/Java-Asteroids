
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

class StaticList<T> {
    static final int ARRAY_SIZE = 1024;
    private Object[] list;
    private int real_len;

    StaticList() {
        list = new Object[StaticList.ARRAY_SIZE];
        real_len = 0;
    }

    T Push(T obj) {
        list[real_len] = obj;
        real_len++;
        return obj;
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



        drawLineV(startPos, endPos, RAYWHITE);
        for (int i = 1; i < this.shape.size; i++) {
            startPos = endPos;
            endPos = mUtils.vecAdd(rotated[i], this.position);
            drawLineV(startPos, endPos, RAYWHITE);
        }

    }

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
