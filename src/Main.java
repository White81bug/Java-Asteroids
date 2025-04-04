
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

class Thing {
    float rotateSpeed;
    float heading;
    Vector2 position;
    Vector2 speed;

    Thing() {
        rotateSpeed = 0;
        heading = 0;
        position = new Vector2(0, 0);
        speed = new Vector2(0, 0);
    }
};

class LogicMaster {
    StaticList<Thing> objList;
    int playerScore;

    LogicMaster() {
        objList = new StaticList<Thing>();
        playerScore = 0;
    }

    void CreateAsteroid() {
        objList.Push(new Thing());
    }
}

public class Main {
    public static void main(String args[]) {
        initWindow(800, 450, "Demo");
        setTargetFPS(60);

        Camera2D camera = new Camera2D(
                new Vector2(0, 0), // offset
                new Vector2(0, 0), // target
                0, // rotation
                0.5f // zoom
        );
        LogicMaster joel = new LogicMaster();

        joel.CreateAsteroid();
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);
            beginMode2D(camera);
            drawGrid(2000, 1.0f);
            drawRectangleV(new Vector2(0, 0), new Vector2(100, 100), RAYWHITE);
            endMode2D();
            drawFPS(20, 20);
            endDrawing();

        }
        closeWindow();
    }
}
