
import static com.raylib.Raylib.*;

import static com.raylib.Raylib.CameraMode.CAMERA_ORBITAL;
import static com.raylib.Raylib.CameraProjection.CAMERA_PERSPECTIVE;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

class ArrayOutOfBounds extends java.lang.Exception {
    ArrayOutOfBounds() {
        super();
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

    T Get(int i) throws ArrayOutOfBounds {
        if (i < 0) {
            System.err.printf("Got i == %d when real_len == %d\n", i, real_len);
            throw new ArrayOutOfBounds();
        }
        return (T) list[i];
    }

    T Pop(int i) throws ArrayOutOfBounds {
        if (i < 0 || real_len <= i) {
            throw new ArrayOutOfBounds();
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
//делаем игрока
class Player{
    Vector2 position;
    Vector2 speed;
    float heading;

    Player()
    {
        position = new Vector2(400, 225);
        speed = new Vector2(0, 0);
        heading = 0;
    }
    void Update()
    {
        position.setX(position.getX() + speed.getX());
        position.setY(position.getY() + speed.getY());
        if(speed.getX() != 0 || speed.getY() != 0) heading = (float) Math.atan2(speed.getY(), speed.getX());

    }
    void Draw(){

        float size = 20;
        Vector2 nose = new Vector2(size, 0);
        Vector2 left = new Vector2(-size * 0.5f, size * 0.5f);
        Vector2 right = new Vector2(-size * 0.5f, -size * 0.5f);

        Vector2 p1 = rotateAndTranslate(nose, heading, position);
        Vector2 p2 = rotateAndTranslate(left, heading, position);
        Vector2 p3 = rotateAndTranslate(right, heading, position);

        drawTriangle(p2, p1, p3, RAYWHITE);
    }

    Vector2 rotateAndTranslate(Vector2 point, float angle, Vector2 origin) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float x = point.getX() * cos - point.getY() * sin;
        float y = point.getX() * sin + point.getY() * cos;

        return new Vector2(origin.getX() + x, origin.getY() + y);
    }
}

class LogicMaster {
    StaticList<Thing> objList;
    int playerScore;
    Player player;

    LogicMaster() {
        objList = new StaticList<Thing>();
        playerScore = 0;
        player = new Player();
    }

    void CreateAsteroid() {
        objList.Push(new Thing());
    }

    void HandleInput() {
        float moveSpeed = 2.5f;
        player.speed.setX(0);
        player.speed.setY(0);

        if (isKeyDown(KEY_W)) player.speed.setY(player.speed.getY()-moveSpeed);
        if (isKeyDown(KEY_S)) player.speed.setY(player.speed.getY()+moveSpeed);
        if (isKeyDown(KEY_A)) player.speed.setX(player.speed.getX() - moveSpeed);
        if (isKeyDown(KEY_D)) player.speed.setX(player.speed.getX()+moveSpeed);
    }
    void Update(){
        HandleInput();
        player.Update();
    }
    void Render() {
        player.Draw();
    }
}

public class Main {
    public static void main(String args[]) {
        initWindow(800, 450, "Demo");
        setTargetFPS(60);
        LogicMaster joel = new LogicMaster();

        Camera2D camera = new Camera2D(
                new Vector2(0, 0), // offset
               joel.player.position, // target
                0, // rotation
                0.5f // zoom
        );


        joel.CreateAsteroid();

        System.out.println(joel.objList.GetLen());
        try {
            System.out.println(joel.objList.Get(0));
        } catch (ArrayOutOfBounds e) {
            System.exit(-1);
        }
        ;
        while (!windowShouldClose()) {
            joel.Update();

            camera.setTarget(joel.player.position);

            beginDrawing();
            clearBackground(BLACK);
            //Не знаю почему, но если это "раскомментить" оно перестаёт отрисовывать игрока.
            //beginMode2D(camera);
            //drawGrid(2000, 1.0f);
            //drawRectangleV(new Vector2(0, 0), new Vector2(100, 100), RAYWHITE);

            joel.Render();

            //endMode2D();
            drawFPS(20, 20);
            endDrawing();

        }
        closeWindow();
    }
}
