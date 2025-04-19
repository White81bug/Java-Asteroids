
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;
import static com.raylib.Raylib.MouseButton.*;

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

class CameraController {
    private Vector2 target = new Vector2(0, 0);
    private float zoom = 2.0f;
    private boolean followPlayer = true;

    Camera2D camera = new Camera2D();

    void Update(Vector2 targetOriginal) {

        if (isKeyPressed(KEY_C)) {
            followPlayer = !followPlayer;
        }

        float wheel = getMouseWheelMove();
        if (wheel != 0.0f) {
            zoom += wheel * 0.1f;
            zoom = Math.max(0.1f, Math.min(zoom, 5.0f));
        }

        if (isMouseButtonDown(MOUSE_BUTTON_RIGHT)) {
            if (followPlayer)
                followPlayer = false;
            Vector2 delta = getMouseDelta();
            delta = mUtils.vecMul(delta, -1.0f / zoom);
            target = mUtils.vecAdd(target, delta);
        }

        if (followPlayer) {
            target = new Vector2(targetOriginal.getX(), targetOriginal.getY());
        }

        camera.setOffset(
                new Vector2(getScreenWidth() / 2.0f, getScreenHeight() / 2.0f));
        camera.setTarget(target);
        camera.setRotation(0);
        camera.setZoom(zoom);
    }

    Camera2D getCamera() {
        return camera;
    }

    void DrawStatus() {
        drawText(followPlayer ? "Camera: FOLLOW [C]" : "Camera: FREE [C]", 20,
                20, 20, GREEN);
    }
}

class LogicMaster {

    Player playerRef = null;

    //         (x1,y1)
    //         |\
    //         | \
    //         |  \ sqrt((x2-x1)^2 + (y2-y1)^2) = r1+r2
    // |y2-y1| |   \
    //         |    \
    //         |   X \
    // (x1,y2) +------+ (x2,y2)
    //          |x2-x1|

    StaticList<Thing> objList;

    LogicMaster() {
        objList = new StaticList<Thing>();
    }

    Asteroid CreateAsteroid(Vector2 position) {
        return (Asteroid) objList.Push(new Asteroid(position));
    }

    Player CreatePlayer(Vector2 position) {
        this.playerRef = (Player) objList.Push(new Player(position));
        return this.playerRef;
    }

    void RunLogic() {
        this.objList.Sort();
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
                Collider.RunCollider(this.objList, i);
                obj.Update();
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
        initWindow(1280, 800, "Fuck this shit");
        setTargetFPS(60);

        Camera2D camera = new Camera2D(
                //changed initial coords for camera. Without this change player in top-right corner
                new Vector2(getScreenWidth() / 2.0f, getScreenHeight() / 2.0f),
                new Vector2(0, 0), 0, 2f);
        LogicMaster joel = new LogicMaster();
        CameraController camCtrl = new CameraController();

        joel.CreatePlayer(new Vector2(200, 200));
        joel.CreateAsteroid(new Vector2(200, 100));
        while (!windowShouldClose()) {
            camCtrl.Update(joel.playerRef.position);
            beginDrawing();
            clearBackground(BLACK);
            beginMode2D(camCtrl.getCamera());
            Renderer.drawGrid();

            joel.RunLogic();

            endMode2D();
            drawFPS(20, 20);

            drawText(
                    String.format("Rotation: %f",
                            joel.playerRef.shape.rotation),
                    20, 40, 18, RAYWHITE);
            endDrawing();

        }
        closeWindow();
    }
}
