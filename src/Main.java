
import static com.raylib.Raylib.*;

import static com.raylib.Raylib.CameraMode.CAMERA_ORBITAL;
import static com.raylib.Raylib.CameraProjection.CAMERA_PERSPECTIVE;

import com.raylib.Camera2D;
import com.raylib.Vector2;
import com.raylib.jextract.rlRenderBatch;
import com.raylib.jextract.rlVertexBuffer;
import java.lang.ArrayIndexOutOfBoundsException;

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

    static final int MAX_SIZE = 32;

    Shape(Vector2[] points) {
        ref = points;
        size = ref.length;
        this.points = ref;
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
        speed = new Vector2(1, 0);
        this.shape = shape;
    }

    void Draw() {
        if (this.shape == null)
            throw new NullPointerException();
        if (this.shape.size < 1)
            return;
        Vector2 startPos = this.shape.points[this.shape.size - 1];
        Vector2 endPos = this.shape.points[0];
        drawLineV(startPos, endPos, RAYWHITE);
        for (int i = 1; i < this.shape.size; i++) {
            startPos = endPos;
            endPos = this.shape.points[i];
            drawLineV(startPos, endPos, RAYWHITE);
        }
    }
};

class LogicMaster {
    StaticList<Thing> objList;
    int playerScore;

    static final Shape player = new Shape(new Vector2[] {
            new Vector2(1, 1),
            new Vector2(2, 2),
            new Vector2(1, 2) });

    LogicMaster() {
        objList = new StaticList<Thing>();
        playerScore = 0;
    }

    void CreateAsteroid() {
        objList.Push(new Thing(LogicMaster.player));
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
        joel.objList.Get(0).position = new Vector2(2, 2);
        System.out.println(joel.objList.GetLen());
        try {
            System.out.println(joel.objList.Get(0));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.exit(-1);
        }
        ;
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);
            beginMode2D(camera);
            drawGrid(2000, 1.0f);

            joel.Render();

            endMode2D();
            drawFPS(20, 20);
            endDrawing();

        }
        closeWindow();
    }
}
