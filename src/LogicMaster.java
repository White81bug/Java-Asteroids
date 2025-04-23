import com.raylib.Vector2;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.KEY_EQUAL;
import static com.raylib.Raylib.KeyboardKey.KEY_SPACE;
import static com.raylib.Raylib.getFrameTime;

class LogicMaster {

    float timeSinceLastSpawn = 0.0f;
    int asteroidCount = 0;

    private Difficulty difficulty;
    private int score = 0;

    static LogicMaster runningLM_ptr = null;

    Player playerRef = null;
    CameraController camCtl = null;

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

    LogicMaster(Difficulty difficulty) {
        objList = new StaticList<Thing>();
        LogicMaster.difficulty = difficulty;
        LogicMaster.runningLM_ptr = this;
        this.camCtl = new CameraController();
    }

    Asteroid CreateAsteroid(Vector2 position) {
        return (Asteroid) objList.Push(new Asteroid(position));
    }

    static void AddScore() {

        score += difficulty.getScoreValue();
    }

    Vector2 RandomEdgePosition() {
        int edge = (int) (Math.random() * 4); // 0: top, 1: right, 2: bottom, 3: left

        float x = 0, y = 0;

        final float fixedSpawnRadius = 60f;
        float offset = fixedSpawnRadius + 20f;

        switch (edge) {
            case 0: // top
                x = (float) (Math.random()
                        * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                y = GLOBALS.MIN_WORLD_POS + offset;
                break;
            case 1: // right
                x = GLOBALS.MAX_WORLD_POS - offset;
                y = (float) (Math.random()
                        * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                break;
            case 2: // bottom
                x = (float) (Math.random()
                        * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
                y = GLOBALS.MAX_WORLD_POS - offset;
                break;
            case 3: // left
                x = GLOBALS.MIN_WORLD_POS + offset;
                y = (float) (Math.random()
                        * (GLOBALS.MAX_WORLD_POS - 2 * offset)) + offset;
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
            this.asteroidCount++;
            CreateAsteroid(new Vector2(300, 300));
        }

        this.camCtl.Update(this.playerRef.position);

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
                drawText(String.format("%d\n%d", i, obj.priority),
                        (int) obj.position.getX(), (int) obj.position.getY(),
                        18, RAYWHITE);
                obj.Draw();
            } catch (NullPointerException e) {
                System.out.printf("Got a null on i == %d\n", i);
                e.printStackTrace();
                System.exit(-1);
            }
        }
        timeSinceLastSpawn += getFrameTime();
        if (timeSinceLastSpawn >= spawnCooldown
                && asteroidCount < difficulty.getMaxAsteroids()) {
            CreateAsteroid(RandomEdgePosition()).speed =
                    new Vector2((float) (Math.random() - .5) * 400,
                            (float) (Math.random() - .5) * 400); // Was it really THAT difficult?

            timeSinceLastSpawn = 0.0f;
        }

        for (int i = 0; i < objList.GetLen(); i++) {
            Thing obj = obj = objList.Get(i);
            if (obj.askToDie) {
                if (obj instanceof Asteroid)
                    this.asteroidCount--;
                objList.Pop(i);
            }
        }
        objList.CleanupMemory();

    }

    void resetScore() {
        score = 0;
    }
}
