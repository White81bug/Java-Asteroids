
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;
import static com.raylib.Raylib.MouseButton.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

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

    float spawnCooldown = 2.0f;
    float timeSinceLastSpawn = 0.0f;

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

    Asteroid CreateAsteroid(Vector2 position, float mass) {
        return (Asteroid) objList.Push(new Asteroid(position, mass));
    }

    Asteroid CreateAsteroid(Vector2 position) {
        return (Asteroid) CreateAsteroid(position, 1.f);
    }

    Vector2 RandomEdgePosition() {
        int edge = (int) (Math.random() * 4); // 0: top, 1: right, 2: bottom, 3: left

        float x = 0, y = 0;

        float asteroidBuffer = 20f;
        float asteroidRadius = Asteroid.asteroidShape.colliderRadius;
        float offset = asteroidRadius + asteroidBuffer;

        switch (edge) {
            case 0: // top
                x = (float) (Math.random() * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                y = GLOBALS.MIN_WORLD_POS + offset;
                break;
            case 1: // right
                x = GLOBALS.MAX_WORLD_POS - offset;
                y = (float) (Math.random() * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                break;
            case 2: // bottom
                x = (float) (Math.random() * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                y = GLOBALS.MAX_WORLD_POS - offset;
                break;
            case 3: // left
                x = GLOBALS.MIN_WORLD_POS + offset;
                y = (float) (Math.random() * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                break;
        }
        return new Vector2(x, y);
    }

    Player CreatePlayer(Vector2 position) {
        this.playerRef = (Player) objList.Push(new Player(position));
        return this.playerRef;
    }

    Bullet CreateBullet() {
        return (Bullet) objList.Push(new Bullet(this.playerRef));
    }

    void RunLogic() {
        this.objList.Sort();

        if (isKeyPressed(KEY_SPACE)) {
            CreateBullet();
        }

        if (isKeyPressed(KEY_EQUAL)) {
            CreateAsteroid(new Vector2(300, 300));
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

                if (obj.askToDie) {
                    continue;
                }
                Collider.RunCollider(this.objList, i);
                obj.Update();
                drawText(
                        String.format("%d\n%d", i, obj.priority),
                        (int) obj.position.getX(), (int) obj.position.getY(), 18, RAYWHITE);
                obj.Draw();
            } catch (NullPointerException e) {
                System.out.printf("Got a null on i == %d\n", i);
                e.printStackTrace();
                System.exit(-1);
            }
        }
        timeSinceLastSpawn += getFrameTime();
        if (timeSinceLastSpawn >= spawnCooldown) {
            CreateAsteroid(RandomEdgePosition());
            timeSinceLastSpawn = 0.0f;
        }

        for (int i = 0; i < objList.GetLen(); i++) {
            Thing obj = obj = objList.Get(i);
            if (obj.askToDie)
                objList.Pop(i);
        }
        objList.CleanupMemory();

    }
}

public class Main {
    public static void main(String args[]) {
        GameState state = GameState.MENU;

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
            beginDrawing();
            clearBackground(BLACK);

            switch (state) {
                case MENU:
                    drawText("ASTEROIDS GAME", 500, 300, 40, RAYWHITE);
                    drawText("Press [ENTER] to Start", 500, 350, 20, GRAY);
                    if (isKeyPressed(KEY_ENTER)) {
                        joel = new LogicMaster(); // logic restart
                        joel.CreatePlayer(new Vector2(200, 200));
                        joel.CreateAsteroid(new Vector2(200, 100));
                        state = GameState.GAME;
                    }
                    break;

                case GAME:
                    camCtrl.Update(joel.playerRef.position);
                    beginMode2D(camCtrl.getCamera());
                    Renderer.drawGrid();

                    joel.RunLogic();

                    endMode2D();
                    drawFPS(20, 20);
                    drawText(String.format("Rotation: %f", joel.playerRef.shape.rotation), 20, 40, 18, RAYWHITE);

                    if (joel.playerRef.askToDie) {
                        state = GameState.GAME_OVER;
                    }
                    break;

                case GAME_OVER:
                    drawText("GAME OVER", 550, 300, 40, RED);
                    drawText("Press [ENTER] to go to Menu", 500, 350, 20, GRAY);
                    if (isKeyPressed(KEY_ENTER)) {
                        state = GameState.MENU;
                    }
                    break;
            }

            endDrawing();

        }
        closeWindow();
    }
}
