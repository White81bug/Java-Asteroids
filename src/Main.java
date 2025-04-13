
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

    static boolean checkCollision(Vector2 a, Vector2 b, float radius) {
        Vector2 dif = mUtils.vecSub(a, b);
        dif = mUtils.vecMul(dif, dif);
        return dif.getX() + dif.getY() <= (radius + radius) * (radius + radius);
    }

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
        initWindow(1280, 800, "Fuck this shit");
        setTargetFPS(60);

        Camera2D camera = new Camera2D(new Vector2(0, 0), // offset
                new Vector2(-10, -10), // target
                0, // rotation
                2f // zoom
        );
        LogicMaster joel = new LogicMaster();

        joel.CreatePlayer(new Vector2(200, 200));
        joel.CreateAsteroid(new Vector2(200, 100));
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);
            beginMode2D(camera);
            Renderer.drawGrid();

            joel.RunLogic();

            endMode2D();
            drawFPS(20, 20);
            endDrawing();

        }
        closeWindow();
    }
}
