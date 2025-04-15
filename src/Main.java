
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

final class Collider {
    static boolean CheckCollide(Thing a, Thing b) {

        float length = (float) Math.sqrt(
                Math.pow(b.position.getX() - a.position.getX(), 2)
                        + Math.pow(b.position.getY() - a.position.getY(), 2));

        if (a.shape.colliderRadius + b.shape.colliderRadius < length)
            return false;
        return true;
    }

    static int RunCollider(StaticList<Thing> list, int index) {
        int collisionCount = 0;
        Thing first = list.Get(index);
        Thing second;
        for (int i = index + 1; i < list.GetLen(); i++) {
            second = list.Get(index);

            // This is an optimization, so we don't have to check the rest of the list
            // if the current object is out of reach.
            // It assumes that the list is sorted by X
            if (second.shape.colliderRadius + first.shape.colliderRadius < second.position.getX()
                    + first.position.getX())
                break;

            if (!CheckCollide(first, second))
                continue;
            collisionCount++;
        }
        return collisionCount;
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

            drawText(String.format("Rotation: %f", joel.playerRef.shape.rotation),
                    20, 40, 18, RAYWHITE);
            endDrawing();

        }
        closeWindow();
    }
}
